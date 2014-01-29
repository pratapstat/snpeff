package ca.mcgill.mcb.pcingola.snpSift;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import ca.mcgill.mcb.pcingola.fileIterator.DbNsfpEntry;
import ca.mcgill.mcb.pcingola.fileIterator.DbNsfpFileIterator;
import ca.mcgill.mcb.pcingola.fileIterator.GuessTableTypes;
import ca.mcgill.mcb.pcingola.fileIterator.VcfFileIterator;
import ca.mcgill.mcb.pcingola.util.Gpr;
import ca.mcgill.mcb.pcingola.util.Timer;
import ca.mcgill.mcb.pcingola.vcf.VcfEntry;
import ca.mcgill.mcb.pcingola.vcf.VcfInfoType;

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
public class SnpSiftCmdDbNsfp extends SnpSift {

	public static final String VCF_INFO_PREFIX = "dbNSFP_";
	public static final String DEFAULT_FIELDS_NAMES_TO_ADD = "Ensembl_transcriptid,Uniprot_acc,Interpro_domain,SIFT_score,Polyphen2_HVAR_pred,GERP++_NR,GERP++_RS,29way_logOdds,1000Gp1_AF,1000Gp1_AFR_AF,1000Gp1_EUR_AF,1000Gp1_AMR_AF,1000Gp1_ASN_AF,ESP6500_AA_AF,ESP6500_EA_AF";

	protected Map<String, String> fieldsToAdd;
	protected Map<String, String> fieldsDescription;
	protected Map<String, String> fieldsType;
	protected boolean annotateAll; // Annotate empty fields as well?
	protected boolean collapseRepeatedValues; // Collapse values if repeated?
	protected String dbNsfpFileName;
	protected String vcfFileName;
	protected int count = 0;
	protected int countAnnotated = 0;
	protected DbNsfpFileIterator dbNsfpFile;
	protected VcfFileIterator vcfFile;
	protected DbNsfpEntry currentDbEntry;
	protected String prevChr = null;
	protected String fieldsNamesToAdd;

	public SnpSiftCmdDbNsfp(String args[]) {
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
		for (String fieldName : fieldsToAdd.keySet()) {
			// Get type
			String type = fieldsType.get(fieldName);
			if (type == null) {
				System.err.println("WARNING: Cannot find type for field '" + fieldName + "', using 'String'.");
				type = VcfInfoType.String.toString();
			}

			vcfFile.getVcfHeader().addLine("##INFO=<ID=" + VCF_INFO_PREFIX + fieldName + ",Number=A,Type=" + type + ",Description=\"" + fieldsToAdd.get(fieldName) + "\">");
		}
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
			dbNsfpFile.seek(chr);
			prevChr = chr;
			currentDbEntry = null;
		}

		//---
		// Seek to position in db (within chr)
		//---
		if (debug) System.err.println("Looking for " + vcf.getChromosomeName() + ":" + vcf.getStart() + ". Current DB: " + (currentDbEntry == null ? "null" : currentDbEntry.getChromosomeName() + ":" + currentDbEntry.getStart()));
		while (true) {
			if (currentDbEntry == null) {
				currentDbEntry = dbNsfpFile.next();
				if (currentDbEntry == null) return; // Test for EOF in database
			}

			// Passed through chromosome without finding position OR position not annotated?
			if (!currentDbEntry.getChromosomeName().equals(vcf.getChromosomeName()) || vcf.getStart() < currentDbEntry.getStart()) return;

			// Found the entry
			if (vcf.getStart() == currentDbEntry.getStart()) break;
			if (debug) Gpr.debug("Current Db Entry:" + currentDbEntry.getChromosomeName() + ":" + currentDbEntry.getStart() + "\tLooking for: " + vcf.getChromosomeName() + ":" + vcf.getStart());

			// OK, jump to next entry
			currentDbEntry = null;
			dbNsfpFile.seek(vcf.getChromosomeName(), vcf.getStart());
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
				// Are there any values to annotate?
				if (currentDbEntry.hasValues(alt)) {

					// Map<String, String> values = currentDbEntry.getAltAlelleValues(alt);
					String val = currentDbEntry.getCsv(alt, fieldKey);

					if (val == null) {
						// No value: Don't add		
					} else if (val.isEmpty() || val.equals(".")) {
						// Empty: Mark as 'empty'
						empty = true;
					} else {
						if (info.length() > 0) info.append(',');
						info.append(val);
						empty = false;
					}
				}
			}

			if (annotateAll || !empty) {
				String infoStr = info.toString();
				if (infoStr.isEmpty()) infoStr = ".";
				infoStr = infoStr.replace(';', ',').replace('\t', '_').replace(' ', '_'); // Make sure all characters are valid for VCF field

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
			if (fieldsDescription.get(filedName) == null) System.err.println("WARNING: Field (column) '" + filedName + "' does not have an approriate file descriptor.");

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
	 * Initialize default values
	 */
	@Override
	public void init() {
		fieldsToAdd = new HashMap<String, String>();
		fieldsType = new HashMap<String, String>();
		fieldsDescription = new HashMap<String, String>();
		annotateAll = false;
		collapseRepeatedValues = true;
	}

	/**
	 * Initialize annotation process
	 * @throws IOException
	 */
	public void initAnnotate() throws IOException {
		// Guess data types from table information
		GuessTableTypes guessTableTypes = new GuessTableTypes(dbNsfpFileName);
		guessTableTypes.guessTypes();
		if (!guessTableTypes.parsedHeader()) throw new RuntimeException("Could not parse header from file '" + dbNsfpFileName + "'");
		VcfInfoType types[] = guessTableTypes.getTypes();
		String fieldNames[] = guessTableTypes.getFieldNames();
		for (int i = 0; i < fieldNames.length; i++) {
			String type = (types[i] != null ? types[i].toString() : "String");
			fieldsType.put(fieldNames[i], type);
			fieldsDescription.put(fieldNames[i], "Field '" + fieldNames[i] + "' from dbNSFP");
		}

		// Open VCF file
		vcfFile = new VcfFileIterator(vcfFileName);

		// Check and open dbNsfp
		dbNsfpFile = new DbNsfpFileIterator(dbNsfpFileName);
		dbNsfpFile.setCollapseRepeatedValues(collapseRepeatedValues);

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
			else if (arg.equalsIgnoreCase("-noCollapse")) {
				collapseRepeatedValues = false;
				annotateAll = true;
			} else if (dbNsfpFileName == null) dbNsfpFileName = arg;
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

		StringBuilder sb = new StringBuilder();
		for (String f : DEFAULT_FIELDS_NAMES_TO_ADD.split(","))
			sb.append("\t                - " + f + "\n");

		// Show error
		showVersion();
		System.err.println("Usage: java -jar " + SnpSift.class.getSimpleName() + ".jar " + command + " [-q|-v] [-a] dbNSFP.txt file.vcf > newFile.vcf\n" //
				+ "Options:\n" //
				+ "\t-a          : Annotate fields, even if the database has an empty value (annotates using '.' for empty).\n" //
				+ "\t-noCollapse : Collapse repeated values from dbNSFP (implies '-a'). Default: '" + collapseRepeatedValues + "'.\n" //
				+ "\t-f          : A comma sepparated list of fields to add.\n" //
				+ "\t              Default fields to add:\n" + sb //
				+ "\n" //
		);
		System.exit(1);
	}
}
