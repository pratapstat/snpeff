package ca.mcgill.mcb.pcingola.snpSift;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import ca.mcgill.mcb.pcingola.fileIterator.DbNsfpEntry;
import ca.mcgill.mcb.pcingola.fileIterator.DbNsfpFileIterator;
import ca.mcgill.mcb.pcingola.fileIterator.SeekableBufferedReader;
import ca.mcgill.mcb.pcingola.fileIterator.VcfFileIterator;
import ca.mcgill.mcb.pcingola.util.Gpr;
import ca.mcgill.mcb.pcingola.util.Timer;
import ca.mcgill.mcb.pcingola.vcf.FileIndexChrPos;
import ca.mcgill.mcb.pcingola.vcf.VcfEntry;

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
public class SnpSiftCmdAnnotateSortedDbNsfp extends SnpSift {

	public static final String VCF_INFO_PREFIX = "dbNSFP_";
	public static final String DEFAULT_FIELDS_NAMES_TO_ADD = "Ensembl_transcriptid,Uniprot_acc,Interpro_domain,SIFT_score,Polyphen2_HVAR_pred,GERP++_NR,GERP++_RS,29way_logOdds,1000Gp1_AF,1000Gp1_AFR_AF,1000Gp1_EUR_AF,1000Gp1_AMR_AF,1000Gp1_ASN_AF,ESP6500_AA_AF,ESP6500_EA_AF";

	protected Map<String, String> fieldsToAdd;
	protected Map<String, String> fieldsDescription;
	protected boolean annotateAll; // Annotate empty fields as well?
	protected String dbNsfpFileName;
	protected String vcfFileName;
	protected int count = 0;
	protected int countAnnotated = 0;
	protected DbNsfpFileIterator dbNsfpFile;
	protected FileIndexChrPos indexDb;
	protected VcfFileIterator vcfFile;
	protected DbNsfpEntry currentDbEntry;
	protected String prevChr = null;
	protected String fieldsNamesToAdd;

	public SnpSiftCmdAnnotateSortedDbNsfp(String args[]) {
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
		for (String key : fieldsToAdd.keySet())
			vcfFile.getVcfHeader().addLine("##INFO=<ID=" + VCF_INFO_PREFIX + key + ",Number=A,Type=String,Description=\"" + fieldsToAdd.get(key) + "\">");
	}

	/**
	 * Annotate a VCF entry
	 * @param vcf
	 * @throws IOException
	 */
	public void annotate(VcfEntry vcf) throws IOException {
		if (currentDbEntry == null) {
			// Read DB entry
			currentDbEntry = dbNsfpFile.next();
			if (currentDbEntry == null) { return; }
			if (prevChr == null) prevChr = currentDbEntry.getChromosomeName();
		}

		//---
		// Seek to new chromosome in DB file?
		//---
		String chr = vcf.getChromosomeName();
		if (!chr.equals(prevChr)) {
			if (debug) System.err.println("Seeking to chromosome '" + chr + "'");

			// Get to the beginning of the new chromosome
			long start = indexDb.getStart(chr);

			// No such chromosome?
			if (start < 0) {
				warn("Chromosome '" + chr + "' not found in database.");
				return;
			}

			// Seek
			dbNsfpFile.seek(start);
			prevChr = chr;
			currentDbEntry = null;
		}

		//---
		// Seek to position in db (within chr)
		//---
		int count = 1;
		if (debug) System.err.println("Looking for " + vcf.getChromosomeName() + ":" + vcf.getStart() + ". Current DB: " + currentDbEntry.getChromosomeName() + ":" + currentDbEntry.getStart() + " : ");
		while (true) {
			if (currentDbEntry == null) {
				currentDbEntry = dbNsfpFile.next();
				if (currentDbEntry == null) return; // Test for EOF in database
			}

			// Passed through chromosome without finding position OR position not annotated?
			if (!currentDbEntry.getChromosomeName().equals(vcf.getChromosomeName()) || vcf.getStart() < currentDbEntry.getStart()) return;

			// Found the entry
			if (vcf.getStart() == currentDbEntry.getStart()) break;
			if (debug) Gpr.showMark(count++, 100);
			currentDbEntry = null;
		}

		//---
		// Add all INFO fields that refer to this allele
		//---
		boolean annotated = false;
		StringBuilder info = new StringBuilder();
		for (String fieldKey : fieldsToAdd.keySet()) {
			boolean empty = true;
			info.setLength(0);

			// For each ALT
			for (String alt : vcf.getAlts()) {
				Map<String, String> values = currentDbEntry.getAltAlelleValues(alt);
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
				vcf.addInfo(VCF_INFO_PREFIX + fieldKey, infoStr);
				annotated = true;
			}
		}

		currentDbEntry = null;
		if (annotated) countAnnotated++;
	}

	/**
	 * Check that all fields to add are available
	 * @throws IOException
	 */
	public void checkFieldsToAdd() throws IOException {
		// Check that all fields have a descriptor (used in VCF header)
		for (String filedName : dbNsfpFile.getFieldNames())
			if (fieldsDescription.get(filedName) == null) System.err.println("WARNING: Filed (column) '" + filedName + "' does not have an approriate file descriptor.");

		// Check that all "field to add" are in the database
		for (String fieldKey : fieldsToAdd.keySet())
			if (!dbNsfpFile.hasField(fieldKey)) fatalError("dbNsfp does not have field '" + fieldKey + "' (file '" + dbNsfpFileName + "')");
	}

	/**
	 * Finish up annotation process
	 */
	public void endAnnotate() {
		vcfFile.close();
		dbNsfpFile.close();
	}

	/**
	 * Index a VCF file
	 * @param fileName
	 * @return
	 */
	private FileIndexChrPos index(String fileName) {
		if (verbose) System.err.println("Index: " + fileName);
		FileIndexChrPos fileIndex = new FileIndexChrPos(fileName);
		fileIndex.setVerbose(verbose);
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
		fieldsToAdd = new HashMap<String, String>();

		/**
		 * All available fields. This is from the DbNSFP README file
		 * Reference: http://dbnsfp.houstonbioinformatics.org/dbNSFPzip/dbNSFP2.0b3.readme.txt
		 * FIXME: It is a terrible idea to hard-code this...I will regret it soon.
		 */
		fieldsDescription = new HashMap<String, String>();
		// The first four fields are not used:
		fieldsDescription.put("chr", "chromosome number");
		fieldsDescription.put("pos(1-coor)", "physical position on the chromosome as to hg19 (1-based coordinate)");
		fieldsDescription.put("ref", "reference nucleotide allele (as on the + strand)");
		fieldsDescription.put("alt", "alternative nucleotide allele (as on the + strand)");
		fieldsDescription.put("aaref", "reference amino acid");
		fieldsDescription.put("aaalt", "alternative amino acid");
		fieldsDescription.put("hg18_pos(1-coor)", "physical position on the chromosome as to hg18 (1-based coordinate)");
		fieldsDescription.put("genename", "gene name");
		fieldsDescription.put("Uniprot_acc", "Uniprot accession number. Multiple entries separated by ';'.");
		fieldsDescription.put("Uniprot_id", "Uniprot ID number. Multiple entries separated by ';'.");
		fieldsDescription.put("Uniprot_aapos", "amino acid position as to Uniprot. Multiple entries separated by ';'.");
		fieldsDescription.put("Interpro_domain", "domain or conserved site on which the variant locates. Domain annotations come from Interpro database. The number in the brackets following a specific domain is the count of times Interpro assigns the variant position to that domain, typically coming from different predicting databases. ");
		fieldsDescription.put("cds_strand", "coding sequence (CDS) strand (+ or -)");
		fieldsDescription.put("refcodon", "reference codon");
		fieldsDescription.put("SLR_test_statistic", "SLR test statistic for testing natural selection on codons. A negative value indicates negative selection, and a positive value indicates positive selection. Larger magnitude of the value suggests stronger evidence.");
		fieldsDescription.put("codonpos", "position on the codon (1, 2 or 3)");
		fieldsDescription.put("fold-degenerate", "degenerate type (0, 2 or 3)");
		fieldsDescription.put("Ancestral_allele", "Ancestral allele (based on 1000 genomes reference data). ACTG : high-confidence call, actg : low-confindence call, N : failure, - : the extant species contains an insertion at this postion, . : no coverage in the alignment");
		fieldsDescription.put("Ensembl_geneid", "Ensembl gene id");
		fieldsDescription.put("Ensembl_transcriptid", "Ensembl transcript ids (separated by ';')");
		fieldsDescription.put("aapos", "amino acid position as to the protein '-1' if the variant is a splicing site SNP (2bp on each end of an intron)");
		fieldsDescription.put("SIFT_score", "SIFT score, If a score is smaller than 0.05 the corresponding NS is predicted as 'D(amaging)'; otherwise it is predicted as 'T(olerated)'.");
		fieldsDescription.put("Polyphen2_HDIV_score", "Polyphen2 score based on HumDiv, i.e. hdiv_prob. The score ranges from 0 to 1, and the corresponding prediction is 'probably damaging' if it is in [0.957,1]; 'possibly damaging' if it is in [0.453,0.956]; 'benign' if it is in [0,0.452]. Score cutoff for binary classification is 0.5, i.e. the prediction is 'neutral' if the score is smaller than 0.5 and 'deleterious' if the score is larger than 0.5. Multiple entries separated by ';'.");
		fieldsDescription.put("Polyphen2_HDIV_pred", "Polyphen2 prediction based on HumDiv, 'D' ('porobably damaging'), 'P' ('possibly damaging') and 'B' ('benign'). Multiple entries separated by ';'.");
		fieldsDescription.put("Polyphen2_HVAR_score", "Polyphen2 score based on HumVar, i.e. hvar_prob. The score ranges from 0 to 1, and the corresponding prediction is 'probably damaging' if it is in [0.909,1]; 'possibly damaging' if it is in [0.447,0.908]; 'benign' if it is in [0,0.446]. Score cutoff for binary classification is 0.5, i.e. the prediction is 'neutral' if the score is smaller than 0.5 and 'deleterious' if the score is larger than 0.5. Multiple entries separated by ';'.");
		fieldsDescription.put("Polyphen2_HVAR_pred", "Polyphen2 prediction based on HumVar, 'D' ('porobably damaging'), 'P' ('possibly damaging') and 'B' ('benign'). Multiple entries separated by ';'.");
		fieldsDescription.put("LRT_score", "LRT score");
		fieldsDescription.put("LRT_pred", "LRT prediction, D(eleterious), N(eutral) or U(nknown)");
		fieldsDescription.put("MutationTaster_score", "MutationTaster score");
		fieldsDescription.put("MutationTaster_pred", "MutationTaster prediction, 'A' ('disease_causing_automatic'), 'D' ('disease_causing'), 'N' ('polymorphism') or 'P' ('polymorphism_automatic')");
		fieldsDescription.put("GERP++_NR", "GERP++ neutral rate");
		fieldsDescription.put("GERP++_RS", "GERP++ RS score, the larger the score, the more conserved the site.");
		fieldsDescription.put("phyloP", "PhyloP score, the larger the score, the more conserved the site.");
		fieldsDescription.put("29way_pi", "The estimated stationary distribution of A, C, G and T at the site, using SiPhy algorithm based on 29 mammals genomes. ");
		fieldsDescription.put("29way_logOdds", "SiPhy score based on 29 mammals genomes. The larger the score, the more conserved the site.");
		fieldsDescription.put("LRT_Omega", "estimated nonsynonymous-to-synonymous-rate ratio (¦Ø, reported by LRT)");
		fieldsDescription.put("UniSNP_ids", "rs numbers from UniSNP, which is a cleaned version of dbSNP build 129, in format: rs number1;rs number2;...");
		fieldsDescription.put("1000Gp1_AC", "Alternative allele counts in the whole 1000 genomes phase 1 (1000Gp1) data.");
		fieldsDescription.put("1000Gp1_AF", "Alternative allele frequency in the whole 1000Gp1 data.");
		fieldsDescription.put("1000Gp1_AFR_AC", "Alternative allele counts in the 1000Gp1 African descendent samples.");
		fieldsDescription.put("1000Gp1_AFR_AF", "Alternative allele frequency in the 1000Gp1 African descendent samples.");
		fieldsDescription.put("1000Gp1_EUR_AC", "Alternative allele counts in the 1000Gp1 European descendent samples.");
		fieldsDescription.put("1000Gp1_EUR_AF", "Alternative allele frequency in the 1000Gp1 European descendent samples.");
		fieldsDescription.put("1000Gp1_AMR_AC", "Alternative allele counts in the 1000Gp1 American descendent samples.");
		fieldsDescription.put("1000Gp1_AMR_AF", "Alternative allele frequency in the 1000Gp1 American descendent samples.");
		fieldsDescription.put("1000Gp1_ASN_AC", "Alternative allele counts in the 1000Gp1 Asian descendent samples.");
		fieldsDescription.put("1000Gp1_ASN_AF", "Alternative allele frequency in the 1000Gp1 Asian descendent samples.");
		//		fieldsDescription.put("ESP5400_AA_AF", "Alternative allele frequency in the Afrian American samples of the NHLBI GO Exome Sequencing Project (ESP5400 data set).");
		//		fieldsDescription.put("ESP5400_EA_AF", "Alternative allele frequency in the European American samples of the NHLBI GO Exome Sequencing Project (ESP5400 data set).");
		fieldsDescription.put("ESP6500_AA_AF", "Alternative allele frequency in the Afrian American samples of the NHLBI GO Exome Sequencing Project (ESP6500 data set).");
		fieldsDescription.put("ESP6500_EA_AF", "Alternative allele frequency in the European American samples of the NHLBI GO Exome Sequencing Project (ESP6500 data set).");
		fieldsDescription.put("MutationAssessor_score", "MutationAssessor functional impact combined score");
		fieldsDescription.put("MutationAssessor_pred", "MutationAssessor functional impact of a variant : predicted functional (high, medium), predicted non-functional (low, neutral)");
		fieldsDescription.put("FATHMM_score", "FATHMM default score (weighted for human inherited-disease mutations with Disease Ontology); If a score is smaller than -1.5 the corresponding NS is predicted as D(AMAGING); otherwise it is predicted as T(OLERATED). If there's more than one scores associated with the same NS due to isoforms, the smallest score (most damaging) was used.");
	}

	/**
	 * Initialize annotation process
	 * @throws IOException
	 */
	public void initAnnotate() throws IOException {
		vcfFile = new VcfFileIterator(vcfFileName);

		// Check and open dbNsfp
		dbNsfpFile = new DbNsfpFileIterator(new SeekableBufferedReader(dbNsfpFileName));

		indexDb = index(dbNsfpFileName);
		currentDbEntry = null;

		// No field names specified? Use default
		if (fieldsNamesToAdd == null) fieldsNamesToAdd = DEFAULT_FIELDS_NAMES_TO_ADD;
		for (String fn : fieldsNamesToAdd.split(",")) {
			if (fieldsDescription.get(fn) == null) usage("Error: Field name '" + fn + "' not found");
			fieldsToAdd.put(fn, fieldsDescription.get(fn));
		}
	}

	/**
	 * Parse command line arguments
	 */
	@Override
	public void parse(String[] args) {
		if (args.length == 0) usage(null);

		for (int i = 0; i < args.length; i++) {
			String arg = args[i];

			if (arg.equals("-a")) annotateAll = true;
			else if (arg.equals("-f")) fieldsNamesToAdd = args[++i]; // Filed to be used
			else if (dbNsfpFileName == null) dbNsfpFileName = arg;
			else if (vcfFileName == null) vcfFileName = arg;
		}

		// Sanity check
		if (dbNsfpFileName == null) usage("Missing dbNSFP file");
		if (vcfFileName == null) usage("Missing 'file.vcf'");
	}

	@Override
	public void run() {
		if (verbose) Timer.showStdErr("Annotating entries from: '" + dbNsfpFileName + "'");

		// Initialize annotations
		try {
			initAnnotate();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		// Annotate VCF file
		boolean showHeader = true;
		for (VcfEntry vcfEntry : vcfFile) {
			try {
				// Show header?
				if (showHeader) {
					// Add VCF header
					addHeader(vcfFile);
					String headerStr = vcfFile.getVcfHeader().toString();
					if (!headerStr.isEmpty()) System.out.println(headerStr);
					showHeader = false;

					// Check that the fields we want to add are actually in the database
					checkFieldsToAdd();
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

		// List of field names and descriptions
		StringBuilder sbFields = new StringBuilder();
		ArrayList<String> fnames = new ArrayList<String>();
		fnames.addAll(fieldsDescription.keySet());
		Collections.sort(fnames);
		for (String fn : fnames)
			sbFields.append(String.format("\t\t%-20s : %s\n", fn, fieldsDescription.get(fn)));

		// Show error
		showVersion();
		System.err.println("Usage: java -jar " + SnpSift.class.getSimpleName() + ".jar " + command + " [-q|-v] [-a] dbNSFP.txt file.vcf > newFile.vcf\n" //
				+ "Options:\n" //
				+ "\t-a : Annotate all fields, even if the database has an empty value.\n" //
				+ "\t-f : A comma sepparated list of fields to add. Default: '" + DEFAULT_FIELDS_NAMES_TO_ADD + "'.\n" //
				+ "\t\tCurrently avaialbe fields are: \n" //
				+ sbFields //
		);
		System.exit(1);
	}
}
