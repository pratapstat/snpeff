package ca.mcgill.mcb.pcingola.snpSift;

import java.util.ArrayList;
import java.util.List;

import ca.mcgill.mcb.pcingola.fileIterator.VcfFileIterator;
import ca.mcgill.mcb.pcingola.ped.PedPedigree;
import ca.mcgill.mcb.pcingola.ped.TfamEntry;
import ca.mcgill.mcb.pcingola.stats.CountByType;
import ca.mcgill.mcb.pcingola.util.Timer;
import ca.mcgill.mcb.pcingola.vcf.VcfEntry;
import ca.mcgill.mcb.pcingola.vcf.VcfGenotype;

/**
 * Annotate if a variant is 'private'. I.e. only represented within a family (or group)
 *  
 * @author pcingola
 */
public class SnpSiftCmdPrivate extends SnpSift {

	public static final String INFO_PRIVATE_NAME = "Private";

	boolean headerSummary = true;
	String tfamFile, vcfFile; // File names
	String[] sampleNum2group;
	List<String> sampleIds; // Sample IDs
	PedPedigree pedigree;

	public SnpSiftCmdPrivate(String[] args) {
		super(args, "private");
	}

	/**
	 * Parse a single VCF entry
	 * @param ve
	 */
	boolean annotate(VcfEntry ve) {
		String privateGorup = privateGroup(ve);

		// Is there a private group?
		if (privateGorup != null) {
			ve.addInfo(INFO_PRIVATE_NAME, privateGorup);
			return true;
		}
		return false;
	}

	/**
	 * Load all data
	 */
	void loadTfam() {
		if (verbose) Timer.showStdErr("Loading TFAM file '" + tfamFile + "'");
		pedigree = new PedPedigree();
		pedigree.loadTfam(tfamFile);
	}

	@Override
	public void parse(String[] args) {
		if (args.length <= 0) usage(null);

		int nonOpts = -1;

		for (int argc = 0; argc < args.length; argc++) {
			if ((nonOpts < 0) && args[argc].startsWith("-")) {
				// Argument starts with '-'? (all default arguments are processed by SnpSift
				usage("Unknown option '" + args[argc] + "'");
			} else if (tfamFile == null) tfamFile = args[argc];
			else if (vcfFile == null) vcfFile = args[argc];

		}

		// Sanity check
		if (tfamFile == null) usage("Missing paramter 'file.tped'");
		if (vcfFile == null) usage("Missing paramter 'file.vcf'");
	}

	/**
	 * Parse VCF header to get sample IDs
	 * @param vcf
	 */
	List<String> parseSampleIds(VcfFileIterator vcf) {
		// Get sample names
		sampleIds = vcf.getSampleNames();

		// Initialize sampleNum2group mapping
		CountByType countByGroup = new CountByType();
		sampleNum2group = new String[sampleIds.size()];
		int sampleNum = 0, missing = 0;
		for (String id : sampleIds) {
			TfamEntry tfam = pedigree.get(id);
			String groupId = "";

			if (tfam == null) {
				missing++;
				System.err.println("WARNING: VCF sample '" + id + "' not found in TFAM file.");
			} else groupId = tfam.getFamilyId();

			// Assign group
			sampleNum2group[sampleNum] = groupId;
			countByGroup.inc(groupId);
			sampleNum++;
		}

		if (missing == sampleIds.size()) throw new RuntimeException("All samples are missing in TFAM file!");

		// Show counts by group
		if (verbose) System.err.println("Counts by group:\nGroup\tCount\n" + countByGroup);

		return sampleIds;
	}

	/**
	 * Name of the group, if this variant private. Null otherwise
	 * 
	 * @param ve
	 */
	String privateGroup(VcfEntry ve) {
		String groupPrev = null;
		int sampleNum = 0;

		for (VcfGenotype gen : ve) {
			// Is this genotype a variant?
			if (gen.isVariant()) {
				String group = sampleNum2group[sampleNum]; // Group for this genotype

				// Analyze groups
				if (group.isEmpty()) {
					// Nothing to do
				} else if (groupPrev == null) groupPrev = group;
				else if (!group.equals(groupPrev)) return null; // Variant present in another group? Then it is not private!
			}

			sampleNum++;
		}

		return groupPrev; // If there was at least one variant, then it was private
	}

	/**
	 * Run annotations
	 */
	@Override
	public void run() {
		run(false);
	}

	/**
	 * Run annotations. Create a list of VcfEntries if 'createList' is true (use for test cases)
	 */
	public List<VcfEntry> run(boolean createList) {
		// Load Tfam file
		loadTfam();

		// Read VCF
		if (verbose) Timer.showStdErr("Annotating VCF file '" + vcfFile + "'");
		int countLines = 0, countAnnotated = 0;
		ArrayList<VcfEntry> vcfEntries = new ArrayList<VcfEntry>();
		VcfFileIterator vcf = new VcfFileIterator(vcfFile);
		for (VcfEntry ve : vcf) {
			// Read header info
			if (vcf.isHeadeSection()) {
				// Get sameple names
				sampleIds = parseSampleIds(vcf);

				// Update header
				vcf.getVcfHeader().addLine("##INFO=<ID=" + INFO_PRIVATE_NAME + ",Number=1,Type=String,Description=\"If the variant is private (i.e. belongs only to one group or family) the group name is shown. Groups from file = '" + tfamFile + "'\">");

				// Show header
				if (!createList) System.out.println(vcf.getVcfHeader());
			}

			if (annotate(ve)) countAnnotated++;
			countLines++;

			// Show (or add to list)
			if (createList) vcfEntries.add(ve);
			else System.out.println(ve);
		}

		if (verbose) Timer.showStdErr("Done.\n\tVCF entries: " + countLines + "\n\tVCF entries annotated: " + countAnnotated);
		return vcfEntries;
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

		System.err.println("Usage: java -jar " + SnpSift.class.getSimpleName() + ".jar private file.tfam file.vcf");
		System.err.println("Where:");
		System.err.println("\tfile.tfam  : File with genotypes and groups information (in PLINK's TFAM format)");
		System.err.println("\tfile.vcf   : A VCF file (variants and genotype data)");
		System.exit(1);
	}
}
