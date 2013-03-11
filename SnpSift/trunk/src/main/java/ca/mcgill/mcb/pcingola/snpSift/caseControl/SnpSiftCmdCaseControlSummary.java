package ca.mcgill.mcb.pcingola.snpSift.caseControl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import ca.mcgill.mcb.pcingola.collections.AutoHashMap;
import ca.mcgill.mcb.pcingola.fileIterator.VcfFileIterator;
import ca.mcgill.mcb.pcingola.interval.Marker;
import ca.mcgill.mcb.pcingola.interval.Markers;
import ca.mcgill.mcb.pcingola.interval.SeqChange;
import ca.mcgill.mcb.pcingola.interval.tree.IntervalForest;
import ca.mcgill.mcb.pcingola.snpEffect.ChangeEffect;
import ca.mcgill.mcb.pcingola.snpSift.SnpSift;
import ca.mcgill.mcb.pcingola.snpSift.SnpSiftCmdAnnotateSortedDbNsfp;
import ca.mcgill.mcb.pcingola.util.Gpr;
import ca.mcgill.mcb.pcingola.util.Timer;
import ca.mcgill.mcb.pcingola.vcf.VcfEffect;
import ca.mcgill.mcb.pcingola.vcf.VcfEffect.FormatVersion;
import ca.mcgill.mcb.pcingola.vcf.VcfEntry;
import ca.mcgill.mcb.pcingola.vcf.VcfGenotype;
import ca.mcgill.mcb.pcingola.vcf.VcfHeader;
import ca.mcgill.mcb.pcingola.vcf.VcfInfo;

/**
 * Summarize a VCF annotated file
 *  
 * @author pcingola
 */
public class SnpSiftCmdCaseControlSummary extends SnpSift {

	public static FormatVersion formatVersion = null;

	public static Boolean CaseControl[] = { true, false };
	public static String VariantsAf[] = { "COMMON", "RARE" };

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

	public SnpSiftCmdCaseControlSummary(String[] args) {
		super(args, "caseControlSummary");
	}

	/**
	 * Load all data
	 */
	void load() {

		//		/**
		//		 * Read cases & controls file
		//		 * Format:	"id\tphenotype\n"
		//		 */
		//		Timer.showStdErr("Reading cases & controls from " + tpedFile);
		//		int countCase = 0, countCtrl = 0;
		//		caseControls = new HashMap<String, Boolean>();
		//		for (String line : Gpr.readFile(tpedFile).split("\n")) {
		//			String rec[] = line.split("\t");
		//
		//			boolean isCase = Gpr.parseIntSafe(rec[1]) == PHENOTYPE_CASE;
		//			caseControls.put(rec[0], isCase); // Add to hash
		//
		//			if (isCase) countCase++;
		//			else countCtrl++;
		//		}
		//		Timer.showStdErr("Total : " + caseControls.size() + " entries. Cases: " + countCase + ", controls: " + countCtrl);
		//
		//		//---
		//		// Read groups
		//		// Format:	"id\tgroupName\n"	 (only one group per sample)
		//		//---
		//		Timer.showStdErr("Reading groups from " + groupsFile);
		//		groups = new HashMap<String, String>();
		//		HashSet<String> groupNames = new HashSet<String>();
		//		CountByType countByType = new CountByType();
		//		for (String line : Gpr.readFile(groupsFile).split("\n")) {
		//			String rec[] = line.split("\t");
		//			String id = rec[0];
		//			String group = rec[1];
		//
		//			if ((group != null) && (!group.isEmpty())) {
		//				groups.put(id, group);
		//				groupNames.add(group);
		//				countByType.inc(group);
		//			}
		//		}
		//		Timer.showStdErr("Total : " + groups.size() + " entries.\n" + countByType);
		//
		//		// Sort group names
		//		groupNamesSorted = new ArrayList<String>();
		//		groupNamesSorted.addAll(groupNames);
		//		Collections.sort(groupNamesSorted);
		//
		//		//---
		//		// Load intervals
		//		//---
		//		Timer.showStdErr("Loading intervals from " + bedFile);
		//		SeqChangeBedFileIterator bed = new SeqChangeBedFileIterator(bedFile);
		//		intervals = bed.load();
		//		Timer.showStdErr("Done. Number of intervals: " + intervals.size());
		//		if (intervals.size() <= 0) {
		//			System.err.println("Fatal error: No intervals!");
		//			System.exit(1);
		//		}
		//
		//		// Create interval forest
		//		Timer.showStdErr("Building interval forest.");
		//		intForest = new IntervalForest();
		//		for (SeqChange sc : intervals)
		//			intForest.add(sc);
		//		intForest.build();

		throw new RuntimeException("THIS HAS TO BE RE_IMPLEMENTED USING TPED FILE!!!");
	}

	/**
	 * Calculate Minimum allele frequency
	 * @param ve
	 * @return
	 */
	double maf(VcfEntry ve) {
		double maf = -1;
		if (ve.hasField("AF")) maf = ve.getInfoFloat("AF"); // Do we have it annotated as AF or MAF?
		else if (ve.hasField("MAF")) maf = ve.getInfoFloat("MAF");
		else {
			int ac = 0, count = 0;
			for (VcfGenotype gen : ve) {
				count += 2;
				int genCode = gen.getGenotypeCode();
				if (genCode > 0) ac += genCode;
			}
			maf = ((double) ac) / count;
		}
		if (maf > 0.5) maf = 1.0 - maf; // Always use the Minor Allele Frequency
		return maf;
	}

	@Override
	public void parse(String[] args) {
		if (args.length <= 0) usage(null);

		int nonOpts = -1;

		for (int argc = 0; argc < args.length; argc++) {
			if ((nonOpts < 0) && args[argc].startsWith("-")) { // Argument starts with '-' (most of them parse in SnpSift class)
				usage("Unknown option '" + args[argc] + "'");
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
	 * Parse a single VCF entry
	 * @param ve
	 */
	void parseVcfEntry(VcfEntry ve) {
		// Parse effect fields. Get highest functional class 
		ChangeEffect.FunctionalClass funcClass = ChangeEffect.FunctionalClass.NONE;
		VcfEffect effMax = null;
		StringBuilder effAll = new StringBuilder();
		StringBuilder otherInfo = new StringBuilder();
		for (VcfEffect eff : ve.parseEffects(formatVersion)) {
			if ((eff.getFunClass() != null) && (funcClass.ordinal() < eff.getFunClass().ordinal())) {
				funcClass = eff.getFunClass();
				effMax = eff;
			}
			effAll.append(eff + "\t");
		}

		// Ignore 'NONE' functional class
		if (funcClass != ChangeEffect.FunctionalClass.NONE) {
			// Get minor allele frequency
			double maf = maf(ve);

			// Variant type (based on allele frequency)
			String variantAf = "COMMON";
			if (maf < 0.05) variantAf = "RARE";

			// Summary per line
			Summary summary = new Summary();

			// Does this entry intersect any intervals?
			Markers intersect = intForest.query(ve);

			// For all samples, update summaries
			int sampleNum = 0;
			for (String id : sampleIds) {
				String group = groups.get(id);
				Boolean caseControl = caseControls.get(id);

				int count = ve.getVcfGenotype(sampleNum).getGenotypeCode();
				if (count > 0) {
					summary.count(group, caseControl, funcClass, variantAf, count); // Update summary for this variant

					// Update all intersecting intervals
					for (Marker interval : intersect) {
						Summary summaryInt = summaryByInterval.getOrCreate(interval); // Get (or create) summary
						summaryInt.count(group, caseControl, funcClass, variantAf, count); // Update summary 
					}
				}

				sampleNum++;
			}

			// Add other info fields
			for (String oi : infoFields) {
				String val = ve.getInfo(oi);
				if (val != null) otherInfo.append(val + "\t");
				else otherInfo.append("\t");
			}

			//---
			// Show
			//---

			// Show title
			if (headerSummary) {
				System.out.print("chr\tstart\tref\talt\tgene\teffect\taa\t" + summary.toStringTitle(groupNamesSorted));
				for (String oi : infoFields)
					System.out.print(oi + "\t");
				System.out.println("effect (max)\teffects (all)");
				headerSummary = false;
			}

			// Show "per variant" summary
			System.out.println(ve.getChromosomeName() //
					+ "\t" + (ve.getStart() + 1) //
					+ "\t" + ve.getRef() //
					+ "\t" + ve.getAltsStr() //
					+ "\t" + effMax.getGene() //
					+ "\t" + effMax.getEffect() //
					+ "\t" + effMax.getAa() //
					+ "\t" + summary.toString(groupNamesSorted) //
					+ otherInfo //
					+ "\t" + effMax //
					+ "\t" + effAll //
			);
		}
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
			}

			if (ve.getAlts().length > 1) Gpr.debug("Ignoring entry with more than one ALT:\t" + ve);
			else parseVcfEntry(ve);
		}

		// Show summaries by interval
		System.err.println("#Summary by interval\n");
		System.err.println("chr\tstart\tend\tname\t" + (new Summary()).toStringTitle(groupNamesSorted));
		for (SeqChange sc : intervals) {
			Summary summary = summaryByInterval.get(sc);
			if (summary != null) {
				System.err.println(sc.getChromosomeName() //
						+ "\t" + (sc.getStart() + 1) //
						+ "\t" + (sc.getEnd() + 1) //
						+ "\t" + sc.getId() //
						+ "\t" + summary.toString(groupNamesSorted) //
				);
			}
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

		System.err.println("Usage: java -jar " + SnpSift.class.getSimpleName() + ".jar ccs [-v] [-q] file.tfam file.bed file.vcf");
		System.err.println("Where:");
		System.err.println("\tfile.tfam  : File with genotypes and groups informations (groups are in familyId)");
		System.err.println("\tfile.bed   : File with regions of interest (intervals in BED format)");
		System.err.println("\tfile.vcf   : A VCF file (variants and genotype data)");
		System.err.println("\nOptions:");
		System.err.println("\t-q       : Be quiet");
		System.err.println("\t-v       : Be verbose");
		System.exit(1);
	}
}
