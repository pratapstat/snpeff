package ca.mcgill.mcb.pcingola.snpSift;

import java.util.HashMap;

import ca.mcgill.mcb.pcingola.fileIterator.VcfFileIterator;
import ca.mcgill.mcb.pcingola.util.Timer;
import ca.mcgill.mcb.pcingola.vcf.VcfEntry;

/**
 * Annotate a VCF file with ID from another VCF file (database)
 * 
 * Loads db file in memory, thus it makes no assumption about order.
 * Requires tons of memory
 * 
 * @author pablocingolani
 */
public class SnpSiftCmdAnnotateMem extends SnpSift {

	public static final int SHOW = 10000;
	public static final int SHOW_LINES = 100 * SHOW;

	protected boolean useInfoField; // Use all info fields
	String vcfDb;
	String vcfFile;
	HashMap<String, String> dbId = new HashMap<String, String>();
	HashMap<String, String> dbInfo = new HashMap<String, String>();

	/**
	 * Main
	 * @param args
	 */
	public static void main(String[] args) {
		SnpSiftCmdAnnotateMem vcfAnnotate = new SnpSiftCmdAnnotateMem(args);
		vcfAnnotate.readDb();
		vcfAnnotate.annotate();
	}

	public SnpSiftCmdAnnotateMem(String args[]) {
		super(args, "annotate");
	}

	/**
	 * Annotate entries
	 */
	public void annotate() {
		if (verbose) Timer.showStdErr("Annotating entries from: '" + vcfFile + "'");

		VcfFileIterator vcf = new VcfFileIterator(vcfFile);

		int countAnnotated = 0, count = 0;
		boolean showHeader = true;
		for (VcfEntry vcfEntry : vcf) {

			// Show header?
			if (showHeader) {
				addHeader(vcf);
				String headerStr = vcf.getVcfHeader().toString();
				if (!headerStr.isEmpty()) System.out.println(headerStr);
				showHeader = false;
			}

			// Anything found? => Annotate
			boolean annotated = false;
			for (int i = 0; i < vcfEntry.getAlts().length; i++) {
				String key = key(vcfEntry, i);
				String id = dbId.get(key);

				String info = (useInfoField ? dbInfo.get(key) : null);

				// Add ID
				if (id != null) {
					if (!vcfEntry.getId().isEmpty()) id = vcfEntry.getId() + ";" + id; // Append if there is already an entry
					vcfEntry.setId(id);
					annotated = true;
				}

				// Add INFO strings
				if (info != null) {
					vcfEntry.addInfo(info);
					annotated = true;
				}
			}

			// Show entry
			System.out.println(vcfEntry);

			if (annotated) countAnnotated++;
			count++;
		}

		double perc = (100.0 * countAnnotated) / count;
		if (verbose) Timer.showStdErr("Done." //
				+ "\n\tTotal annotated entries : " + countAnnotated //
				+ "\n\tTotal entries           : " + count //
				+ "\n\tPercent                 : " + String.format("%.2f%%", perc) //
		);
	}

	/**
	 * Initialize default values
	 */
	@Override
	public void init() {
		useInfoField = true; // Default: Use INFO fields
	}

	/**
	 * Create a key for a hash
	 * @param vcfEntry
	 * @return
	 */
	String key(VcfEntry vcfEntry, int altIndex) {
		return vcfEntry.getChromosomeName() + ":" + vcfEntry.getStart() + "_" + vcfEntry.getRef() + "/" + vcfEntry.getAlts()[altIndex];
	}

	/**
	 * Parse command line arguments
	 */
	@Override
	public void parse(String[] args) {
		int argNum = 0;
		if (args.length == 0) usage(null);

		for (String arg : args) {
			if (args[argNum].startsWith("-")) {
				if (args[argNum].equals("-q")) verbose = false;
				else if (args[argNum].equals("-v")) verbose = true;
				else if (arg.equals("-id")) useInfoField = false;
			} else if (vcfDb == null) vcfDb = args[argNum];
			else if (vcfFile == null) vcfFile = args[argNum];

			argNum++;
		}

		// Sanity check
		if (vcfDb == null) usage("Missing 'database.vcf'");
		if (vcfFile == null) usage("Missing 'file.vcf'");
	}

	/**
	 * Read database
	 */
	public void readDb() {
		if (verbose) Timer.showStdErr("Loading database: '" + vcfDb + "'");
		VcfFileIterator dbFile = new VcfFileIterator(vcfDb);

		int count = 1;
		for (VcfEntry vcfEntry : dbFile) {

			for (int i = 0; i < vcfEntry.getAlts().length; i++) {
				String key = key(vcfEntry, i);

				// Add ID
				if (dbId.containsKey(key)) {
					String multipleId = dbId.get(key) + ";" + vcfEntry.getId();
					dbId.put(key, multipleId);
				} else dbId.put(key, vcfEntry.getId());

				// Add INFO fields
				if (useInfoField) dbInfo.put(key, vcfEntry.getInfoStr());
			}

			count++;
			if (verbose) {
				if (count % SHOW_LINES == 0) System.err.print("\n" + count + "\t.");
				else if (count % SHOW == 0) System.err.print('.');
			}
		}

		// Show time
		if (verbose) {
			System.err.println("");
			Timer.showStdErr("Done. Database size: " + dbId.size());
		}
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

		System.err.println("Usage: java -jar " + SnpSift.class.getSimpleName() + ".jar annotate [-q] database.vcf file.vcf > newFile.vcf\n"//
				+ "Options:\n" //
				+ "\t-id\t:\tOnly annotate ID field (do not add INFO field).\n" //
				+ "\t-q\t:\tBe quiet.\n" //
				+ "\t-v\t:\tBe verbose.\n" //
				+ "Note: It is assumed that both files fit in memory." //
		);
		System.exit(1);
	}
}
