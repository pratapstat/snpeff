package ca.mcgill.mcb.pcingola.snpSift;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import ca.mcgill.mcb.pcingola.collections.AutoHashMap;
import ca.mcgill.mcb.pcingola.fileIterator.VcfFileIterator;
import ca.mcgill.mcb.pcingola.interval.Marker;
import ca.mcgill.mcb.pcingola.interval.SeqChange;
import ca.mcgill.mcb.pcingola.interval.tree.IntervalForest;
import ca.mcgill.mcb.pcingola.snpSift.caseControl.Summary;
import ca.mcgill.mcb.pcingola.util.Gpr;
import ca.mcgill.mcb.pcingola.util.Timer;
import ca.mcgill.mcb.pcingola.vcf.VcfEffect.FormatVersion;
import ca.mcgill.mcb.pcingola.vcf.VcfEntry;
import ca.mcgill.mcb.pcingola.vcf.VcfHeader;
import ca.mcgill.mcb.pcingola.vcf.VcfInfo;

/**
 * Annotate if a variant is 'private'. I.e. only represented within a family (or group)
 *  
 * @author pcingola
 */
public class SnpSiftCmdPrivate extends SnpSift {

	public static FormatVersion formatVersion = null;

	boolean headerSummary = true;
	String tpedFile, bedFile, vcfFile; // File names
	HashMap<String, Boolean> caseControls; // Cases and controls 
	HashMap<String, String> groups; // ID -> Group map
	List<SeqChange> intervals; // Intervals to summarize
	ArrayList<String> groupNamesSorted; // Group names sorted alphabetically
	IntervalForest intForest; // Intervals
	List<String> sampleIds; // Sample IDs
	AutoHashMap<Marker, Summary> summaryByInterval; // Summary by interval
	ArrayList<String> infoFields; // Other info fields to include in summaries (per snp)

	public SnpSiftCmdPrivate(String[] args) {
		super(args, "private");
	}

	/**
	 * Parse a single VCF entry
	 * @param ve
	 */
	void annotate(VcfEntry ve) {

	}

	/**
	 * Load all data
	 */
	void load() {

		throw new RuntimeException("THIS HAS TO BE RE_IMPLEMENTED USING TPED FILE!!!");
	}

	@Override
	public void parse(String[] args) {
		if (args.length <= 0) usage(null);

		int nonOpts = -1;

		for (int argc = 0; argc < args.length; argc++) {
			if ((nonOpts < 0) && args[argc].startsWith("-")) { // Argument starts with '-'?

				if (args[argc].equals("-h") || args[argc].equalsIgnoreCase("-help")) usage(null);
				else if (args[argc].equals("-v")) verbose = true;
				else if (args[argc].equals("-q")) verbose = false;
				else usage("Unknown option '" + args[argc] + "'");

			} else if (tpedFile == null) tpedFile = args[argc];
			else if (bedFile == null) bedFile = args[argc];
			else if (vcfFile == null) vcfFile = args[argc];

		}

		// Sanity check
		if (tpedFile == null) usage("Missing paramter 'file.tped'");
		if (bedFile == null) usage("Missing paramter 'file.bed'");
		if (vcfFile == null) usage("Missing paramter 'file.vcf'");
	}

	/**
	 * Parse DBNSFP fields to add to summary
	 * @param vcf
	 */
	void parseDbNsfpFields(VcfFileIterator vcf) {
		VcfHeader vcfHeader = vcf.getVcfHeader();
		for (VcfInfo vcfInfo : vcfHeader.getVcfInfo())
			if (vcfInfo.getId().startsWith(SnpSiftCmdAnnotateSortedDbNsfp.VCF_INFO_PREFIX)) infoFields.add(vcfInfo.getId());
		Collections.sort(infoFields);

	}

	/**
	 * Parse VCF header to get sample IDs
	 * @param vcf
	 */
	List<String> parseSampleIds(VcfFileIterator vcf) {
		int missingCc = 0, missingGroups = 0;

		// Read IDs
		List<String> sampleIds = vcf.getSampleNames();
		for (String id : sampleIds) {
			if (!caseControls.containsKey(id)) missingCc++;
			if (!groups.containsKey(id)) missingGroups++;
		}

		Timer.showStdErr("Samples missing case/control info: " + missingCc);
		Timer.showStdErr("Samples missing groups info: " + missingGroups);

		// Too many missing IDs? Error
		if (((1.0 * missingCc) / sampleIds.size() > 0.5) || ((1.0 * missingCc) / sampleIds.size() > 0.5)) {
			Timer.showStdErr("Fatal error: Too much missing data!");
			System.exit(1);
		}

		// TODO: Test code, move somewhere else
		VcfHeader vcfHeader = vcf.getVcfHeader();
		for (VcfInfo vcfInfo : vcfHeader.getVcfInfo())
			if (vcfInfo.getId().startsWith(SnpSiftCmdAnnotateSortedDbNsfp.VCF_INFO_PREFIX)) infoFields.add(vcfInfo.getId());
		Collections.sort(infoFields);

		return sampleIds;
	}

	/**
	 * Run summary
	 */
	@Override
	public void run() {
		// Load intervals, phenotypes, etc.
		load();

		// Initialize
		summaryByInterval = new AutoHashMap<Marker, Summary>(new Summary());
		infoFields = new ArrayList<String>();
		headerSummary = true;
		boolean headerVcf = true;

		// Read VCF
		VcfFileIterator vcf = new VcfFileIterator(vcfFile);
		for (VcfEntry ve : vcf) {
			if (headerVcf) {
				// Read header info
				headerVcf = false;
				sampleIds = parseSampleIds(vcf);
				parseDbNsfpFields(vcf);
			}

			if (ve.getAlts().length > 1) Gpr.debug("Ignoring entry with more than one ALT:\t" + ve);
			else annotate(ve);
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

		System.err.println("Usage: java -jar " + SnpSift.class.getSimpleName() + ".jar private file.tped file.vcf");
		System.err.println("Where:");
		System.err.println("\tfile.tped  : File with genotypes and groups informations (groups are in familyId)");
		System.err.println("\tfile.vcf   : A VCF file (variants and genotype data)");
		System.exit(1);
	}
}
