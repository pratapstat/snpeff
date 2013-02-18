package ca.mcgill.mcb.pcingola.snpSift;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import ca.mcgill.mcb.pcingola.fileIterator.SeekableBufferedReader;
import ca.mcgill.mcb.pcingola.fileIterator.VcfFileIterator;
import ca.mcgill.mcb.pcingola.util.Timer;
import ca.mcgill.mcb.pcingola.vcf.FileIndexChrPos;
import ca.mcgill.mcb.pcingola.vcf.VcfEntry;
import ca.mcgill.mcb.pcingola.vcf.VcfHeader;
import ca.mcgill.mcb.pcingola.vcf.VcfInfo;

/**
 * Annotate a VCF file with ID from another VCF file (database)

 * Note: Assumes both the VCF file AND the database file are sorted. 
 *       Each VCF entry should be sorted according to position. 
 *       Chromosome order does not matter (e.g. all entries for chr10 can be before entries for chr2). 
 *       But entries for the same chromosome should be together.   
 * 
 * @author pcingola
 *
 */
public class SnpSiftCmdAnnotateSorted extends SnpSift {

	public static final int SHOW = 10000;
	public static final int SHOW_LINES = 100 * SHOW;

	protected boolean suppressOutput = false; // Do not show output (used for debugging and test cases)
	protected boolean useInfoField; // Use all info fields
	protected int countBadRef = 0;
	protected String chrPrev = "";
	protected String vcfDbFileName;
	protected String vcfFileName;
	protected String infoFields[]; // Use only info fields
	protected VcfEntry latestVcfDb = null;
	protected HashMap<String, String> dbId = new HashMap<String, String>();
	protected HashMap<String, String> dbInfo = new HashMap<String, String>();
	protected FileIndexChrPos indexDb;
	protected VcfFileIterator vcfFile, vcfDbFile;

	public SnpSiftCmdAnnotateSorted(String args[]) {
		super(args, "annotate");
	}

	protected SnpSiftCmdAnnotateSorted(String args[], String command) {
		super(args, command);
	}

	/**
	 * Add annotation form database into VCF entry's INFO field
	 * @param vcf
	 */
	protected boolean addAnnotation(VcfEntry vcf) {
		boolean annotated = false;

		// Is this change in db?
		String id = findDbId(vcf);
		String infoStr = useInfoField ? findDbInfo(vcf) : null;

		if (id != null) {
			// Get unique ids (the ones not already present in vcf.id)
			id = uniqueIds(id, vcf.getId());
			if (!id.isEmpty()) { // Skip if no new ids found
				annotated = true;

				// Add ID
				if (!vcf.getId().isEmpty()) id = vcf.getId() + ";" + id;
				vcf.setId(id);
			}
		}

		// Add INFO fields
		if (infoStr != null) {
			vcf.addInfo(infoStr);
			annotated = true;
		}

		return annotated;
	}

	/**
	 * Add 'key->id' entries to 'db'
	 * @param vcfDb
	 */
	void addDb(VcfEntry vcfDb) {
		for (int i = 0; i < vcfDb.getAlts().length; i++) {
			String key = key(vcfDb, i);
			dbId.put(key, vcfDb.getId()); // Add ID field

			// Add INFO fields to DB?
			if (useInfoField) {

				if (infoFields == null) {
					// Add all INFO fields
					dbInfo.put(key, vcfDb.getInfoStr()); // Add INFO field
				} else {
					// Add some INFO fields
					StringBuilder infoSb = new StringBuilder();

					// Add all fields
					for (String fieldName : infoFields) {
						String val = vcfDb.getInfo(fieldName);

						// Any value? => Add
						if (val != null) {
							if (infoSb.length() > 0) infoSb.append(";");
							infoSb.append(fieldName + "=" + val);
						}
					}

					dbInfo.put(key, infoSb.toString());
				}
			}
		}
	}

	/**
	 * Build headers to add
	 */
	@Override
	protected List<String> addHeader() {
		List<String> newHeaders = super.addHeader();

		// Read database header and add INFO fields to the output vcf header
		if (useInfoField) {
			VcfFileIterator vcfDb = new VcfFileIterator(vcfDbFileName);
			VcfHeader vcfDbHeader = vcfDb.readHeader();
			for (VcfInfo vcfInfo : vcfDbHeader.getVcfInfo())
				newHeaders.add(vcfInfo.toString());
		}

		return newHeaders;
	}

	/**
	 * Annoptate a VCF entry
	 * @param vcf
	 * @throws IOException
	 */
	public boolean annotate(VcfEntry vcf) throws IOException {
		// Do we have to seek in db file?
		String chr = vcf.getChromosomeName();
		if (!chr.equals(chrPrev)) {
			// Get to the beginning of the new chromosome
			long start = indexDb.getStart(chr);

			// No such chromosome?
			if (start < 0) {
				warn("Chromosome '" + chr + "' not found in database.");
				return false;
			}

			// Seek 
			vcfDbFile.seek(start);
			latestVcfDb = null;
			if (verbose) Timer.showStdErr("Chromosome: '" + chr + "'");
		}
		chrPrev = chr;

		// Read database up to this point
		readDb(vcf);

		// Create annotation
		return addAnnotation(vcf);
	}

	protected void clear() {
		dbId.clear();
		if (useInfoField) dbInfo.clear();
	}

	/**
	 * Finish up annotation process
	 */
	public void endAnnotate() {
		vcfDbFile.close(); // We have to close vcfDbFile because it was opened using a BufferedReader (this sets autoClose to 'false')
	}

	/**
	 * Find an ID for this VCF entry
	 * @param vcf
	 * @return
	 */
	protected String findDbId(VcfEntry vcf) {
		if (dbId.isEmpty()) return null;

		for (int i = 0; i < vcf.getAlts().length; i++) {
			String key = key(vcf, i);
			String id = dbId.get(key);
			if (id != null) return id;
		}
		return null;
	}

	/**
	 * Find INFO field for this VCF entry
	 * @param vcf
	 * @return
	 */
	protected String findDbInfo(VcfEntry vcf) {
		if (dbInfo.isEmpty()) return null;

		for (int i = 0; i < vcf.getAlts().length; i++) {
			String key = key(vcf, i);

			// Get info field annotations
			String info = dbInfo.get(key);

			// Any annotations?
			if ((info != null) && (!info.isEmpty())) return info;
		}

		return null;
	}

	/**
	 * Index a VCF file
	 * @param fileName
	 * @return
	 */
	FileIndexChrPos index(String fileName) {
		if (verbose) System.err.println("Index: " + fileName);
		FileIndexChrPos fileIndex = new FileIndexChrPos(fileName);
		fileIndex.setVerbose(verbose);
		fileIndex.setDebug(debug);
		fileIndex.open();
		fileIndex.index();
		fileIndex.close();
		return fileIndex;
	}

	/**
	 * Initialize default values
	 */
	@Override
	public void init() {
		useInfoField = true; // Default: Use INFO fields
	}

	/**
	 * Initialize annotation process
	 * @throws IOException
	 */
	public void initAnnotate() throws IOException {
		// Index and open VCF files
		vcfFile = new VcfFileIterator(vcfFileName);

		// Open and index database
		indexDb = index(vcfDbFileName);
		vcfDbFile = new VcfFileIterator(new SeekableBufferedReader(vcfDbFileName));
	}

	/**
	 * Create a hash key
	 * @param vcfDbEntry
	 * @param altIndex
	 * @return
	 */
	String key(VcfEntry vcfDbEntry, int altIndex) {
		return vcfDbEntry.getChromosomeName() + ":" + vcfDbEntry.getStart() + "_" + vcfDbEntry.getRef() + "/" + vcfDbEntry.getAlts()[altIndex];
	}

	/**
	 * Parse command line arguments
	 */
	@Override
	public void parse(String[] args) {
		if (args.length == 0) usage(null);

		for (int i = 0; i < args.length; i++) {
			String arg = args[i];

			if (arg.equals("-h") || arg.equals("--help")) usage(null);
			else if (arg.equals("-id")) useInfoField = false;
			else if (arg.equals("-info")) {
				useInfoField = true;
				infoFields = args[++i].split(",");
			} else if (vcfDbFileName == null) vcfDbFileName = arg;
			else if (vcfFileName == null) vcfFileName = arg;
		}

		// Sanity check
		if (vcfDbFileName == null) usage("Missing 'database.vcf'");
		if (vcfFileName == null) usage("Missing 'file.vcf'");
	}

	/**
	 * Read all db entries up to 'vcf'
	 * @param vcf
	 */
	void readDb(VcfEntry vcf) {
		String chr = vcf.getChromosomeName();
		clear();

		// Add latest to db?
		if (latestVcfDb != null) {
			if (latestVcfDb.getChromosomeName().equals(chr)) {
				if (vcf.getStart() < latestVcfDb.getStart()) return;
				if (vcf.getStart() == latestVcfDb.getStart()) addDb(latestVcfDb);
			}
		}

		// Read more entries from db
		for (VcfEntry vcfDb : vcfDbFile) {
			latestVcfDb = vcfDb;

			String chrDb = vcfDb.getChromosomeName();
			if (!chrDb.equals(chr)) return;

			if (vcf.getStart() < vcfDb.getStart()) return;
			if (vcf.getStart() == vcfDb.getStart()) {
				// Sanity check: Check that references match
				if (!vcf.getRef().equals(vcfDb.getRef()) //
						&& !vcf.getRef().startsWith(vcfDb.getRef()) //  
						&& !vcfDb.getRef().startsWith(vcf.getRef()) //
				) {
					System.err.println("WARNING: Reference is '" + vcfDb.getRef() + "' instead of '" + vcf.getRef() + "' at " + chr + ":" + (vcf.getStart() + 1));
					countBadRef++;
				}

				addDb(vcfDb); // Same position: Add all keys to 'db'
			}
		}
	}

	/**
	 * Annotate each entry of a VCF file
	 * @throws IOException
	 */
	@Override
	public void run() {
		if (verbose) Timer.showStdErr("Annotating entries from: '" + vcfFile + "'");

		try {
			initAnnotate();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		int countAnnotated = 0, count = 0;
		boolean showHeader = true;
		for (VcfEntry vcfEntry : vcfFile) {
			try {
				// Show header?
				if (showHeader) {
					showHeader = false;
					addHeader(vcfFile);
					String headerStr = vcfFile.getVcfHeader().toString();
					if (!headerStr.isEmpty()) System.out.println(headerStr);
				}

				// Annotate
				boolean annotated = annotate(vcfEntry);

				// Show
				if (!suppressOutput) System.out.println(vcfEntry);

				if (annotated) countAnnotated++;
				count++;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		endAnnotate();

		// Show some stats
		if (verbose) {
			double perc = (100.0 * countAnnotated) / count;
			Timer.showStdErr("Done." //
					+ "\n\tTotal annotated entries : " + countAnnotated //
					+ "\n\tTotal entries           : " + count //
					+ "\n\tPercent                 : " + String.format("%.2f%%", perc) //
					+ "\n\tErrors (bad references) : " + countBadRef //
			);
		}
	}

	public void setSuppressOutput(boolean suppressOutput) {
		this.suppressOutput = suppressOutput;
	}

	/** 
	 * IDs from database not present in VCF
	 * @param idStrDb
	 * @param idStrVcf
	 * @return
	 */
	String uniqueIds(String idStrDb, String idStrVcf) {
		StringBuilder sbId = new StringBuilder();
		String idsDb[] = idStrDb.split(";");
		String idsVcf[] = idStrVcf.split(";");

		for (String idDb : idsDb) {
			// Add only if there is no other matching ID in VCF 
			boolean skip = false;
			for (String idVcf : idsVcf)
				skip |= idDb.equals(idVcf);

			// Append ID?
			if (!skip) sbId.append((sbId.length() > 0 ? ";" : "") + idDb);
		}

		return sbId.toString();
	}

	/**
	 * Show usage message
	 * @param msg
	 */
	@Override
	public void usage(String msg) {
		if (msg != null) {
			System.err.println("Error: " + msg);
			showCmd();
		}

		showVersion();
		System.err.println("Usage: java -jar " + SnpSift.class.getSimpleName() + ".jar " + command + " [options] database.vcf file.vcf > newFile.vcf\n" //
				+ "Options:\n" //
				+ "\t-id          : Only annotate ID field (do not add INFO field).\n" //
				+ "\t-info <list> : Annotate using a list of info fields (list is a comma separated list of fields). Default: ALL.\n" //
		);
		System.exit(1);
	}

}
