package ca.mcgill.mcb.pcingola.snpSift.caseControl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import ca.mcgill.mcb.pcingola.collections.AutoHashMap;
import ca.mcgill.mcb.pcingola.fileIterator.SeqChangeBedFileIterator;
import ca.mcgill.mcb.pcingola.fileIterator.VcfFileIterator;
import ca.mcgill.mcb.pcingola.interval.Marker;
import ca.mcgill.mcb.pcingola.interval.Markers;
import ca.mcgill.mcb.pcingola.interval.SeqChange;
import ca.mcgill.mcb.pcingola.interval.tree.IntervalForest;
import ca.mcgill.mcb.pcingola.snpEffect.ChangeEffect;
import ca.mcgill.mcb.pcingola.snpSift.SnpSift;
import ca.mcgill.mcb.pcingola.stats.CountByType;
import ca.mcgill.mcb.pcingola.util.Gpr;
import ca.mcgill.mcb.pcingola.util.Timer;
import ca.mcgill.mcb.pcingola.vcf.VcfEffect;
import ca.mcgill.mcb.pcingola.vcf.VcfEffect.FormatVersion;
import ca.mcgill.mcb.pcingola.vcf.VcfEntry;
import ca.mcgill.mcb.pcingola.vcf.VcfGenotype;

/**
 * Summarize a VCF annotated file
 *  
 * @author pcingola
 */
public class SnpSiftCmdCaseControlSummary extends SnpSift {

	/*
	 *  From PLINK's manual: Affection status, by default, should be coded:
	 *  		-9 missing 
	 *  		0 missing
	 *  		1 unaffected
	 *  		2 affected
	 */

	public static final int PHENOTYPE_CASE = 2;
	public static final int PHENOTYPE_CONTROL = 1;
	public static final int PHENOTYPE_MISSING = 0;

	public static FormatVersion formatVersion = FormatVersion.FORMAT_SNPEFF_2;

	static Boolean CaseControl[] = { true, false };
	static String VariantsAf[] = { "COMMON", "RARE" };

	String caseControlFile, groupsFile, intervalsFile, vcfFile;
	HashMap<String, Boolean> caseControls;
	HashMap<String, String> groups;
	List<SeqChange> intervals;
	ArrayList<String> groupNamesSorted;
	IntervalForest intForest;

	public SnpSiftCmdCaseControlSummary(String[] args) {
		super(args, "caseControlSummary");
	}

	/**
	 * Load all data
	 */
	void load() {

		/**
		 * Read cases & controls file
		 * Format:	"id\tphenotype\n"
		 */
		Timer.showStdErr("Reading cases & controls from " + caseControlFile);
		int countCase = 0, countCtrl = 0;
		caseControls = new HashMap<String, Boolean>();
		for (String line : Gpr.readFile(caseControlFile).split("\n")) {
			String rec[] = line.split("\t");

			boolean isCase = Gpr.parseIntSafe(rec[1]) == PHENOTYPE_CASE;
			caseControls.put(rec[0], isCase); // Add to hash

			if (isCase) countCase++;
			else countCtrl++;
		}
		Timer.showStdErr("Total : " + caseControls.size() + " entries. Cases: " + countCase + ", controls: " + countCtrl);

		//---
		// Read groups
		// Format:	"id\tgroupName\n"	 (only one group per sample)
		//---
		Timer.showStdErr("Reading groups from " + groupsFile);
		groups = new HashMap<String, String>();
		HashSet<String> groupNames = new HashSet<String>();
		CountByType countByType = new CountByType();
		for (String line : Gpr.readFile(groupsFile).split("\n")) {
			String rec[] = line.split("\t");
			String id = rec[0];
			String group = rec[1];

			groups.put(id, group);
			groupNames.add(group);
			countByType.inc(group);
		}
		Timer.showStdErr("Total : " + groups.size() + " entries.\n" + countByType);

		// Sort group names
		groupNamesSorted = new ArrayList<String>();
		groupNamesSorted.addAll(groupNames);
		Collections.sort(groupNamesSorted);

		//---
		// Load intervals
		//---
		Timer.showStdErr("Loading intervals from " + intervalsFile);
		SeqChangeBedFileIterator bed = new SeqChangeBedFileIterator(intervalsFile);
		intervals = bed.load();
		Timer.showStdErr("Done. Number of intervals: " + intervals.size());
		if (intervals.size() <= 0) {
			System.err.println("Fatal error: No intervals!");
			System.exit(1);
		}

		// Create interval forest
		Timer.showStdErr("Building interval forest.");
		intForest = new IntervalForest();
		for (SeqChange sc : intervals)
			intForest.add(sc);
		intForest.build();
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

			} else if (caseControlFile == null) caseControlFile = args[argc];
			else if (groupsFile == null) groupsFile = args[argc];
			else if (intervalsFile == null) intervalsFile = args[argc];
			else if (vcfFile == null) vcfFile = args[argc];

		}

		// Sanity check
		if (caseControlFile == null) usage("Missing paramter 'caseControlFile'");
		if (groupsFile == null) usage("Missing paramter 'groupsFile'");
		if (intervalsFile == null) usage("Missing paramter 'intervalsFile'");
		if (vcfFile == null) usage("Missing paramter 'vcfFile'");
	}

	/**
	 * Run summary
	 */
	@Override
	public void run() {
		load();

		AutoHashMap<Marker, Summary> summaryByInterval = new AutoHashMap<Marker, Summary>(new Summary());

		// Read VCF
		VcfFileIterator vcf = new VcfFileIterator(vcfFile);
		boolean headerVcf = true, headerSummary = true;
		List<String> sampleIds = null;
		for (VcfEntry ve : vcf) {
			if (headerVcf) {
				//---
				// Read header info
				//---
				headerVcf = false;
				int missingCc = 0, missingGroups = 0;

				//---
				// Read IDs
				//---
				sampleIds = vcf.getSampleNames();
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
			}

			if (ve.getAlts().length > 1) {
				Gpr.debug("Ignoring entry with more than one ALT:\t" + ve);
			} else {
				//---
				// Parse effect fields
				// Get highest functional class 
				//---
				ChangeEffect.FunctionalClass funcClass = ChangeEffect.FunctionalClass.NONE;
				VcfEffect effMax = null;
				StringBuilder effAll = new StringBuilder();
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
					double maf = -1;
					if (ve.hasField("AF")) maf = ve.getInfoFloat("AF"); // Do we have it annotated as AF or MAF?
					else if (ve.hasField("MAF")) maf = ve.getInfoFloat("MAF");
					else {
						int ac = 0, count = 0;
						for (VcfGenotype gen : ve) {
							count += 2;
							ac += gen.getGenotypeCode();
						}
						maf = ((double) ac) / count;
					}
					if (maf > 0.5) maf = 1.0 - maf; // Always use the Minor Allele Frequency

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

					// Show info
					if (headerSummary) {
						System.out.println("chr\tstart\tref\talt\tgene\teffect\taa\t" + summary.toStringTitle(groupNamesSorted) + "\teffect (max)\teffects (all)");
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
							+ "\t" + effMax //
							+ "\t" + effAll //
					);
				}
			}

			//if (vcf.getLineNum() > 1000) break;
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

		System.err.println("Usage: java -jar " + SnpSift.class.getSimpleName() + ".jar ccs [-v] [-q]  caseControlFile groupsFile bedFile vcfFile");
		System.err.println("\t-q       : Be quiet");
		System.err.println("\t-v       : Be verbose");
		System.exit(1);
	}
}
