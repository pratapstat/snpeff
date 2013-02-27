package ca.mcgill.mcb.pcingola.snpSift.caseControl;

import java.util.ArrayList;
import java.util.List;

import ca.mcgill.mcb.pcingola.fileIterator.VcfFileIterator;
import ca.mcgill.mcb.pcingola.ped.PedPedigree;
import ca.mcgill.mcb.pcingola.ped.TfamEntry;
import ca.mcgill.mcb.pcingola.snpSift.SnpSift;
import ca.mcgill.mcb.pcingola.util.Timer;
import ca.mcgill.mcb.pcingola.vcf.VcfEntry;
import ca.mcgill.mcb.pcingola.vcf.VcfGenotype;

/**
 * Count number of cases and controls
 * 
 * @author pablocingolani
 */
public class SnpSiftCmdCaseControl extends SnpSift {

	public static final int SHOW_EVERY = 1000;

	public static final String VCF_INFO_CASE = "Cases";
	public static final String VCF_INFO_CONTROL = "Controls";

	Boolean caseControl[];
	String tfamFile;
	String groups;
	String fileName;
	PedPedigree pedigree;

	public SnpSiftCmdCaseControl(String args[]) {
		super(args, "casecontrol");
	}

	/**
	 * Lines to be added to VCF header
	 * @return
	 */
	@Override
	protected List<String> addHeader() {
		List<String> addh = super.addHeader();
		addh.add("##INFO=<ID=" + VCF_INFO_CASE + ",Number=3,Type=Integer,Description=\"Number of variants in cases: Hom, Het, Count\">");
		addh.add("##INFO=<ID=" + VCF_INFO_CONTROL + ",Number=3,Type=Integer,Description=\"Number of variants in controls: Hom, Het, Count\">");
		return addh;
	}

	/**
	 * Annotate VCF entry
	 * @param vcfEntry
	 */
	void annotate(VcfEntry vcfEntry) {
		int casesHom = 0, casesHet = 0, cases = 0;
		int ctrlHom = 0, ctrlHet = 0, ctrl = 0;

		// Count genotypes
		int idx = 0;
		for (VcfGenotype gt : vcfEntry) {
			if ((caseControl[idx] != null) && gt.isVariant()) {
				if (caseControl[idx]) {
					// Case sample
					if (gt.isHomozygous()) casesHom++;
					else casesHet++;

					cases += gt.getGenotypeCodeIgnoreMissing();
				} else {
					//Control sample
					if (gt.isHomozygous()) ctrlHom++;
					else ctrlHet++;

					ctrl += gt.getGenotypeCodeIgnoreMissing();
				}
			}

			idx++;
		}

		// Add info fields
		vcfEntry.addInfo(VCF_INFO_CASE, casesHom + "," + casesHet + "," + cases);
		vcfEntry.addInfo(VCF_INFO_CONTROL, ctrlHom + "," + ctrlHet + "," + ctrl);
	}

	@Override
	protected void handleVcfHeader(VcfFileIterator vcf) {
		if (!vcf.isHeadeSection()) return;
		super.handleVcfHeader(vcf); // Add lines and print header

		// Parse pedigree from TFAM?
		if (pedigree != null) {
			List<String> sampleIds = vcf.getVcfHeader().getSampleNames();
			caseControl = new Boolean[sampleIds.size()];

			int idx = 0, errors = 0;
			for (String sid : sampleIds) {
				TfamEntry tfam = pedigree.get(sid); // Find TFAM entry for this sample
				if (tfam == null) {
					System.err.println("WARNING: Sample ID '" + sid + "' has no entry in pedigree form TFAM file '" + tfamFile + "'");
					errors++;
				} else {
					// Assign case, control or missing
					if (tfam.isMissing()) caseControl[idx] = null;
					else caseControl[idx] = tfam.isCase();
				}
				idx++;
			}

			// Abort?
			if (errors > 0) throw new RuntimeException("VCF samples are missing in TFAM file.");
		}

		// Sanity check
		if (caseControl.length != vcf.getVcfHeader().getSampleNames().size()) throw new RuntimeException("Number of case control entries specified does not match number of samples in VCF file");

		// Show 
		if (debug) {
			System.err.println("\tSample\tCase");
			int idx = 0;
			for (String sid : vcf.getVcfHeader().getSampleNames())
				System.err.println("\t" + sid + "\t" + caseControl[idx++]);
		}

	}

	/**
	 * Is this a valid 'groups' string?
	 * @param groupsStr
	 * @return
	 */
	boolean isGroupString(String groupsStr) {
		return groupsStr.replace('+', ' ').replace('-', ' ').replace('0', ' ').trim().isEmpty();
	}

	@Override
	public void parse(String[] args) {
		if (args.length <= 0) usage(null);

		for (int argc = 0; argc < args.length; argc++) {
			if (args[argc].startsWith("-")) {
				if (args[argc].equals("-tfam")) tfamFile = args[++argc];
				else if ((groups == null) && isGroupString(args[argc])) groups = args[argc];
				else usage("Unkown parameter '" + args[argc] + "'");
			} else if ((groups == null) && isGroupString(args[argc])) groups = args[argc];
			else if (fileName == null) fileName = args[argc];
			else usage("Unkown parameter '" + args[argc] + "'");
		}

		// Sanity check
		if (fileName == null) usage("Missing paramter 'file.vcf'");
		if ((groups == null) && (tfamFile == null)) usage("You must provide either a 'group' string or a TFAM file");
	}

	/**
	 * Parse group string
	 */
	void parseCaseControlString() {
		char chars[] = groups.toCharArray();
		caseControl = new Boolean[chars.length];
		for (int i = 0; i < chars.length; i++) {
			if (chars[i] == '+') caseControl[i] = true;
			else if (chars[i] == '-') caseControl[i] = false;
			else caseControl[i] = null;
		}
	}

	/**
	 * Parse from TFAM file
	 */
	void parseCaseControlTfam() {
		pedigree = new PedPedigree(tfamFile); // Here we just load the file
	}

	/**
	 * Load a file compare calls
	 * 
	 * @param vcfFile
	 */
	@Override
	public void run() {
		run(false);
	}

	/**
	 * Run 
	 * @param createList : Is true , create a list of VcfEntries (used in test cases)
	 * @return A list of VcfEntry is createList is true
	 */
	public List<VcfEntry> run(boolean createList) {
		showHeader = !createList;
		ArrayList<VcfEntry> list = new ArrayList<VcfEntry>();
		if (verbose) Timer.showStdErr("Annotating number of cases and controls : '" + fileName + "'");

		if (tfamFile != null) parseCaseControlTfam();
		else parseCaseControlString();

		// Read all vcfEntries
		VcfFileIterator vcf = new VcfFileIterator(fileName);

		for (VcfEntry vcfEntry : vcf) {
			handleVcfHeader(vcf); // Handle header stuff
			annotate(vcfEntry); // Annotate

			// Show
			if (createList) list.add(vcfEntry);
			else System.out.println(vcfEntry);
		}

		if (verbose) Timer.showStdErr("Done.");
		return list;
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

		System.err.println("Usage: java -jar " + SnpSift.class.getSimpleName() + ".jar caseControl [-v] <CaseControlString> file.vcf");
		System.err.println("Where:");
		System.err.println("\t<CaseControlString> : A string of {'+', '-', '0'}, one per sample, to identify two groups (case='+', control='-', neutral='0')");
		System.err.println("\t -tfam file.tfam    : A TFAM file having case/control informations (phenotype colmun)");
		System.err.println("\tfile.vcf            : A VCF file (variants and genotype data)");
		System.exit(1);
	}
}
