package ca.mcgill.mcb.pcingola.snpSift;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import ca.mcgill.mcb.pcingola.fileIterator.DbNSFPEntry;
import ca.mcgill.mcb.pcingola.fileIterator.DbNSFPFileIterator;
import ca.mcgill.mcb.pcingola.fileIterator.SeekableBufferedReader;
import ca.mcgill.mcb.pcingola.fileIterator.VcfFileIterator;
import ca.mcgill.mcb.pcingola.util.Timer;
import ca.mcgill.mcb.pcingola.vcf.VcfEntry;
import ca.mcgill.mcb.pcingola.vcf.VcfFileIndexIntervals;

/**
 * Annotate a VCF file with dbNSFP. 
 * 
 * The dbNSFP is an integrated database of functional predictions from multiple algorithms for the comprehensive 
 * collection of human non-synonymous SNPs (NSs). Its current version (ver 1.1) is based on CCDS version 20090327 
 * and includes a total of 75,931,005 NSs. It compiles prediction scores from four prediction algorithms (SIFT, 
 * Polyphen2, LRT and MutationTaster), two conservation scores (PhyloP and GERP++) and other related information.
 *   
 * References:
 *  
 * 		http://sites.google.com/site/jpopgen/dbNSFP
 * 
 * 		Paper: Liu X, Jian X, and Boerwinkle E. 2011. dbNSFP: a lightweight database of human non-synonymous SNPs and their
 * 		functional predictions. Human Mutation. 32:894-899.
 * 
 * @author lletourn
 * 
 */
public class SnpSiftCmdAnnotateSortedDbNSFP extends SnpSift {

	public static final String KEY_PREFIX = "dbnsfp";
	private static final Map<String, String> alleleSpecificFieldsToAdd;
	private static final Map<String, String> positionSpecificFieldsToAdd;

	protected boolean annotateAll; // Annotate empty fields as well?
	protected String dbNSFPFileName;
	protected String vcfFileName;
	protected int count = 0;
	protected int countAnnotated = 0;
	protected DbNSFPFileIterator dbNSFPFile;
	protected VcfFileIndexIntervals indexDb;
	protected VcfFileIterator vcfFile;
	protected DbNSFPEntry currentEntry;
	protected String prevChr = null;

	static {
		positionSpecificFieldsToAdd = new HashMap<String, String>();
		positionSpecificFieldsToAdd.put("Ensembl_transcriptid", "Ensembl transcript ids (separated by ',')");
		positionSpecificFieldsToAdd.put("Uniprot_acc", "Uniprot accession number. (separated by ',')");
		positionSpecificFieldsToAdd.put("Interpro_domain", " domain or conserved site on which the variant locates. Domain annotations come from Interpro database. The number in the brackets following a specific domain is the count of times Interpro assigns the variant position to that domain, typically coming from different predicting databases. Multiple entries. (separated by ',')");

		alleleSpecificFieldsToAdd = new HashMap<String, String>();
		alleleSpecificFieldsToAdd.put("SIFT_score", "SIFT score, If a score is smaller than 0.05 the corresponding NS is predicted as 'D(amaging)' otherwise it is predicted as 'T(olerated)'");
		alleleSpecificFieldsToAdd.put("Polyphen2_HVAR_pred", "Polyphen2 prediction based on HumVar, 'D' ('probably damaging'), 'P' ('possibly damaging') and 'B' ('benign') (separated by ',')");
		alleleSpecificFieldsToAdd.put("GERP++_NR", "GERP++ neutral rate");
		alleleSpecificFieldsToAdd.put("GERP++_RS", "GERP++ RS score, the larger the score, the more conserved the site.");
		alleleSpecificFieldsToAdd.put("29way_logOdds", "SiPhy score based on 29 mammals genomes. The larger the score, the more conserved the site.");
		alleleSpecificFieldsToAdd.put("1000Gp1_AF", "Alternative allele frequency in the whole 1000Gp1 data.");
		alleleSpecificFieldsToAdd.put("1000Gp1_AFR_AF", "Alternative allele frequency in the 1000Gp1 African descendent samples.");
		alleleSpecificFieldsToAdd.put("1000Gp1_EUR_AF", "Alternative allele frequency in the 1000Gp1 European descendent samples.");
		alleleSpecificFieldsToAdd.put("1000Gp1_AMR_AF", "Alternative allele frequency in the 1000Gp1 American descendent samples.");
		alleleSpecificFieldsToAdd.put("1000Gp1_ASN_AF", "Alternative allele frequency in the 1000Gp1 Asian descendent samples.");
		alleleSpecificFieldsToAdd.put("ESP5400_AA_AF", "Alternative allele frequency in the Afrian American samples of the NHLBI GO Exome Sequencing Project (ESP5400 data set).");
		alleleSpecificFieldsToAdd.put("ESP5400_EA_AF", "Alternative allele frequency in the European American samples of the NHLBI GO Exome Sequencing Project (ESP5400 data set).");
	}

	/**
	 * Main
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) {
		SnpSiftCmdAnnotateSortedDbNSFP sift = new SnpSiftCmdAnnotateSortedDbNSFP(args);
		sift.run();
	}

	public SnpSiftCmdAnnotateSortedDbNSFP(String args[]) {
		super(args, "dbnsfp");
	}

	/**
	 * Add some lines to header before showing it
	 * 
	 * @param vcfFile
	 */
	@Override
	protected void addHeader(VcfFileIterator vcfFile) {
		super.addHeader(vcfFile);
		for (String key : alleleSpecificFieldsToAdd.keySet())
			vcfFile.addHeader("##INFO=<ID=" + KEY_PREFIX + key + ",Number=A,Type=String,Description=\"" + alleleSpecificFieldsToAdd.get(key) + "\">");

		for (String key : positionSpecificFieldsToAdd.keySet())
			vcfFile.addHeader("##INFO=<ID=" + KEY_PREFIX + key + ",Number=.,Type=String,Description=\"" + positionSpecificFieldsToAdd.get(key) + "\">");
	}

	/**
	 * Annotate a VCF entry
	 * @param vcf
	 * @throws IOException
	 */
	public void annotate(VcfEntry vcf) throws IOException {
		if (currentEntry == null) {
			currentEntry = dbNSFPFile.next();
			if (currentEntry == null) { return; }
			if (prevChr == null) prevChr = currentEntry.getChromosomeName();
		}

		// Do we have to seek to new chromosome in db file?
		String chr = vcf.getChromosomeName();
		if (!chr.equals(prevChr)) {
			// Get to the beginning of the new chromosome
			long start = indexDb.getStart(chr);

			// No such chromosome?
			if (start < 0) {
				warn("Chromosome '" + chr + "' not found in database.");
				return;
			}

			// Seek
			dbNSFPFile.seek(start);
			prevChr = chr;
			currentEntry = null;
		}

		// Seek to chromosomal position in db
		while (true) {
			if (currentEntry == null) {
				currentEntry = dbNSFPFile.next();
				// Test for EOF, passed through chromosome without finding position, position not annotated
				if (currentEntry == null || !currentEntry.getChromosomeName().equals(vcf.getChromosomeName()) || vcf.getStart() < currentEntry.getStart()) { return; }
			}

			if (vcf.getStart() == currentEntry.getStart()) break;
			currentEntry = null;
		}

		// Add all INFO fields that refer to this allele
		boolean annotated = false;
		StringBuilder info = new StringBuilder();
		for (String fieldKey : alleleSpecificFieldsToAdd.keySet()) {
			boolean empty = true;
			info.setLength(0);
			for (String alt : vcf.getAlts()) {
				Map<String, String> values = currentEntry.getAltAlelleValues(alt);
				if (info.length() > 0) info.append(',');

				if (values == null) info.append('.');
				else {
					String val = values.get(fieldKey);
					if (val == null || val.isEmpty() || val.equals(".")) info.append('.');
					else {
						info.append(val);
						empty = false;
					}
				}
			}

			if (annotateAll || !empty) {
				String infoStr = info.toString().replace(';', ',').replace('\t', '_').replace(' ', '_'); // Make sure all characters are valid for VCF field
				vcf.addInfo(KEY_PREFIX + fieldKey, infoStr);
				annotated = true;
			}
		}

		// Add all INFO fields that refer to this position
		for (String fieldKey : positionSpecificFieldsToAdd.keySet()) {
			boolean empty = true;
			info.setLength(0);
			if (vcf.getAlts().length > 0) {
				String alt = vcf.getAlts()[0];
				Map<String, String> values = currentEntry.getAltAlelleValues(alt);

				if (values == null) info.append('.');
				else {
					String val = values.get(fieldKey);
					if (val == null || val.isEmpty() || val.equals(".")) info.append('.');
					else {
						info.append(val);
						empty = false;
					}
				}
			}

			if (annotateAll || !empty) {
				String infoStr = info.toString().replace(';', ',').replace('\t', '_').replace(' ', '_'); // Make sure all characters are valid for VCF field
				vcf.addInfo(KEY_PREFIX + fieldKey, infoStr);
				annotated = true;
			}
		}

		currentEntry = null;
		if (annotated) countAnnotated++;
	}

	/**
	 * Finish up annotation process
	 */
	public void endAnnotate() {
		vcfFile.close();
		dbNSFPFile.close();
	}

	/**
	 * Index a VCF file
	 * @param fileName
	 * @return
	 */
	private VcfFileIndexIntervals index(String fileName) {
		if (verbose) System.err.println("Index: " + fileName);
		VcfFileIndexIntervals vcfFileIndexIntervals = new VcfFileIndexIntervals(fileName);
		vcfFileIndexIntervals.setVerbose(verbose);
		vcfFileIndexIntervals.open();
		vcfFileIndexIntervals.index();
		vcfFileIndexIntervals.close();
		return vcfFileIndexIntervals;
	}

	/**
	 * Initialize annotation process
	 * @throws IOException
	 */
	public void initAnnotate() throws IOException {
		vcfFile = new VcfFileIterator(vcfFileName);
		dbNSFPFile = new DbNSFPFileIterator(new SeekableBufferedReader(dbNSFPFileName));

		indexDb = index(dbNSFPFileName);
		currentEntry = null;
	}

	/**
	 * Parse command line arguments
	 */
	@Override
	public void parse(String[] args) {
		if (args.length == 0) usage(null);

		for (String arg : args) {
			if (arg.equals("-q")) verbose = false;
			else if (arg.equals("-v")) verbose = true;
			else if (arg.equals("-a")) annotateAll = true;
			else if (arg.equals("-h") || arg.equals("--help")) usage(null);
			else if (dbNSFPFileName == null) dbNSFPFileName = arg;
			else if (vcfFileName == null) vcfFileName = arg;

		}

		// Sanity check
		if (dbNSFPFileName == null) usage("Missing dbNSFP file");
		if (vcfFileName == null) usage("Missing 'file.vcf'");

	}

	@Override
	public void run() {
		if (verbose) Timer.showStdErr("Annotating entries from: '" + dbNSFPFileName + "'");

		try {
			initAnnotate();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		boolean showHeader = true;
		for (VcfEntry vcfEntry : vcfFile) {
			try {
				// Show header?
				if (showHeader) {
					addHeader(vcfFile);
					if (!vcfFile.getHeader().isEmpty()) System.out.println(vcfFile.getHeader());
					showHeader = false;
				}

				// Annotate
				annotate(vcfEntry);

				// Show
				System.out.println(vcfEntry);
				count++;
			} catch (IOException e) {
				throw new RuntimeException(e);
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
			);
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
		System.err.println("Usage: java -jar " + SnpSift.class.getSimpleName() + ".jar " + command + " [-q|-v] [-a] dbNSFP.txt file.vcf > newFile.vcf\n" //
				+ "Options:\n" //
				+ "\t-a\t:\tAnnotate all fields, even if the database has an empty value.\n" //
				+ "\t-q\t:\tBe quiet.\n" //
				+ "\t-v\t:\tBe verbose.\n" //
		);
		System.exit(1);
	}
}
