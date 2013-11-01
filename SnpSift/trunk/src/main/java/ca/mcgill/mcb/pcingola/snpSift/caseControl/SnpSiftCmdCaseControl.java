package ca.mcgill.mcb.pcingola.snpSift.caseControl;

import java.util.ArrayList;
import java.util.List;

import ca.mcgill.mcb.pcingola.fileIterator.VcfFileIterator;
import ca.mcgill.mcb.pcingola.ped.PedPedigree;
import ca.mcgill.mcb.pcingola.ped.TfamEntry;
import ca.mcgill.mcb.pcingola.probablility.CochranArmitageTest;
import ca.mcgill.mcb.pcingola.probablility.FisherExactTest;
import ca.mcgill.mcb.pcingola.snpSift.SnpSift;
import ca.mcgill.mcb.pcingola.util.Gpr;
import ca.mcgill.mcb.pcingola.util.Timer;
import ca.mcgill.mcb.pcingola.vcf.VcfEntry;
import ca.mcgill.mcb.pcingola.vcf.VcfGenotype;

/**
 * Count number of cases and controls
 * 
 * @author pablocingolani
 */
public class SnpSiftCmdCaseControl extends SnpSift {

	public static final int SHOW_EVERY = 100;

	public static final String VCF_INFO_CASE = "Cases";
	public static final String VCF_INFO_CONTROL = "Controls";
	public static final String VCF_INFO_CC_GENO = "CC_GENO";
	public static final String VCF_INFO_CC_ALL = "CC_ALL";
	public static final String VCF_INFO_CC_DOM = "CC_DOM";
	public static final String VCF_INFO_CC_REC = "CC_REC";

	protected Boolean caseControl[];
	protected String tfamFile;
	protected String groups;
	protected String vcfFileName;
	protected PedPedigree pedigree;
	String name;
	String posMin = "";
	double pValueMin = 1.0;

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
		addh.add("##INFO=<ID=" + VCF_INFO_CASE + name + ",Number=3,Type=Integer,Description=\"Number of variants in cases: Hom, Het, Count\">");
		addh.add("##INFO=<ID=" + VCF_INFO_CONTROL + name + ",Number=3,Type=Integer,Description=\"Number of variants in controls: Hom, Het, Count\">");
		addh.add("##INFO=<ID=" + VCF_INFO_CC_DOM + name + ",Number=1,Type=Float,Description=\"p-value using dominant model (Fisher exact)\">");
		addh.add("##INFO=<ID=" + VCF_INFO_CC_REC + name + ",Number=1,Type=Float,Description=\"p-value using recessive model (Fisher exact)\">");
		addh.add("##INFO=<ID=" + VCF_INFO_CC_ALL + name + ",Number=1,Type=Float,Description=\"p-value using allele count model (Fisher exact)\">");
		addh.add("##INFO=<ID=" + VCF_INFO_CC_GENO + name + ",Number=1,Type=Float,Description=\"p-value using genotype / codominant model (CochranArmitage)\">");
		return addh;
	}

	/**
	 * Annotate VCF entry
	 * @param vcfEntry
	 */
	void annotate(VcfEntry vcfEntry) {
		int casesHom = 0, casesHet = 0, cases = 0;
		int ctrlHom = 0, ctrlHet = 0, ctrl = 0;
		int nCase[] = new int[3];
		int nControl[] = new int[3];

		// Count genotypes
		int idx = 0;
		for (VcfGenotype gt : vcfEntry) {
			if ((caseControl[idx] != null)) {

				int code = gt.getGenotypeCode();
				int codeMissing = gt.getGenotypeCodeIgnoreMissing();

				if (code >= 0) {
					// Count a/a, a/A and A/A
					if (caseControl[idx]) nCase[code]++;
					else nControl[code]++;
				}

				if (gt.isVariant()) {
					if (caseControl[idx]) {
						// Case sample
						if (gt.isMissing()) ; // Missing? => Do not count
						else if (gt.isHomozygous()) casesHom++;
						else casesHet++;

						cases += codeMissing;
					} else {
						//Control sample
						if (gt.isMissing()) ; // Missing? => Do not count
						else if (gt.isHomozygous()) ctrlHom++;
						else ctrlHet++;

						ctrl += codeMissing;
					}
				}
			}

			idx++;
		}

		// Add info fields
		vcfEntry.addInfo(VCF_INFO_CASE + name, casesHom + "," + casesHet + "," + cases);
		vcfEntry.addInfo(VCF_INFO_CONTROL + name, ctrlHom + "," + ctrlHet + "," + ctrl);

		// Annotate pValues
		vcfEntry.addInfo(VCF_INFO_CC_GENO + name, pValueStr(vcfEntry, pCodominant(nControl, nCase)));
		swapMinorAllele(nControl, nCase); // Swap if minor allele is reference
		vcfEntry.addInfo(VCF_INFO_CC_ALL + name, "" + pValueStr(vcfEntry, pAllelic(nControl, nCase)));
		vcfEntry.addInfo(VCF_INFO_CC_DOM + name, "" + pValueStr(vcfEntry, pDominant(nControl, nCase)));
		vcfEntry.addInfo(VCF_INFO_CC_REC + name, "" + pValueStr(vcfEntry, pRecessive(nControl, nCase)));
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
					caseControl[idx] = null;
				} else {
					// Assign case, control or missing
					if (tfam.isMissing()) caseControl[idx] = null;
					else caseControl[idx] = tfam.isCase();
				}
				idx++;
			}

			// Abort?
			if (errors > (sampleIds.size() / 2)) throw new RuntimeException("VCF samples are missing in TFAM file. Too many errors, aboting!");
		}

		// Sanity check
		if (caseControl.length != vcf.getVcfHeader().getSampleNames().size()) throw new RuntimeException("Number of case control entries specified does not match number of samples in VCF file");

		// Show details 
		if (debug) {
			System.err.println("\tSample\tCase");
			int idx = 0;
			for (String sid : vcf.getVcfHeader().getSampleNames())
				System.err.println("\t" + sid + "\t" + caseControl[idx++]);
		}

		// Show overview 
		if (verbose) {
			int countCase = 0, countCtrl = 0, countIgnored = 0;
			for (Boolean cc : caseControl) {
				if (cc == null) countIgnored++;
				else if (cc) countCase++;
				else countCtrl++;
			}
			Timer.showStdErr("Total : " + caseControl.length + " entries. Cases: " + countCase + ", controls: " + countCtrl + ", ignored: " + countIgnored);
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

	/**
	 * Allelic model: Count number of SNPs
	 * @param nControl
	 * @param nCase
	 * @return
	 */
	protected double pAllelic(int nControl[], int nCase[]) {
		int k = 2 * nCase[2] + nCase[1];
		int N = 2 * (nControl[0] + nControl[1] + nControl[2] + nCase[0] + nCase[1] + nCase[2]);
		int D = 2 * (nCase[0] + nCase[1] + nCase[2]); // All cases
		int n = 2 * nControl[2] + nControl[1] + 2 * nCase[2] + nCase[1]; // A/A + A/a

		double pdown = FisherExactTest.get().fisherExactTestDown(k, N, D, n);
		double pup = FisherExactTest.get().fisherExactTestUp(k, N, D, n);

		return Math.min(pup, pdown);
	}

	@Override
	public void parse(String[] args) {
		if (args.length <= 0) usage(null);

		for (int argc = 0; argc < args.length; argc++) {
			if (args[argc].equals("-tfam")) tfamFile = args[++argc];
			else if (args[argc].equals("-name")) name = args[++argc];
			else if ((groups == null) && (tfamFile == null) && isGroupString(args[argc])) groups = args[argc];
			else if (vcfFileName == null) vcfFileName = args[argc];
			else usage("Unkown parameter '" + args[argc] + "'");
		}

		// Sanity check
		if (vcfFileName == null) usage("Missing paramter 'file.vcf'");
		if ((groups == null) && (tfamFile == null)) usage("You must provide either a 'group' string or a TFAM file");

		if (name == null) name = "";
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
	 * Codominant model
	 * @param nControl
	 * @param nCase
	 * @return
	 */
	protected double pCodominant(int nControl[], int nCase[]) {
		return CochranArmitageTest.get().p(nControl, nCase, CochranArmitageTest.WEIGHT_CODOMINANT);
	}

	/**
	* Dominant model: Either a/A or A/A causes the disease
	* @param nControl
	* @param nCase
	* @return
	*/
	protected double pDominant(int nControl[], int nCase[]) {
		int k = nCase[2] + nCase[1]; // Cases a/a + A/a
		int N = nControl[0] + nControl[1] + nControl[2] + nCase[0] + nCase[1] + nCase[2];
		int D = nCase[0] + nCase[1] + nCase[2]; // All cases
		int n = nControl[2] + nControl[1] + nCase[2] + nCase[1]; // a/a + A/a

		double pdown = FisherExactTest.get().fisherExactTestDown(k, N, D, n);
		double pup = FisherExactTest.get().fisherExactTestUp(k, N, D, n);

		return Math.min(pup, pdown);
	}

	/**
	 * Recessive model: Only A/A causes the disease
	 * @param nControl
	 * @param nCase
	 * @return
	 */
	protected double pRecessive(int nControl[], int nCase[]) {
		int k = nCase[2]; // Cases a/a
		int N = nControl[0] + nControl[1] + nControl[2] + nCase[0] + nCase[1] + nCase[2];
		int D = nCase[0] + nCase[1] + nCase[2]; // All cases
		int n = nControl[2] + nCase[2]; // a/a

		double pdown = FisherExactTest.get().fisherExactTestDown(k, N, D, n);
		double pup = FisherExactTest.get().fisherExactTestUp(k, N, D, n);

		return Math.min(pup, pdown);
	}

	/**
	 * Show p-value as a string and record minimum p-value
	 * @param vcfEntry
	 * @param p
	 * @return
	 */
	String pValueStr(VcfEntry vcfEntry, double p) {
		if (verbose && (p > 0) && (p < 1.0) && (p <= pValueMin)) //
			Timer.showStdErr("Minimum p-value so far: " //
					+ pValueMin //
					+ "\tchr: " + vcfEntry.getChromosomeName() //
					+ "\tpos: " + (vcfEntry.getStart() + 1) //
					+ (!vcfEntry.getId().isEmpty() ? "\tid: " + vcfEntry.getId() : "") //
			);

		if ((p > 0) && (p < pValueMin)) {
			pValueMin = p;
			posMin = vcfEntry.getChromosomeName() + ":" + (vcfEntry.getStart() + 1);
		}
		return String.format("%.3e", p);
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
		if (verbose) Timer.showStdErr("Annotating number of cases and controls : '" + vcfFileName + "'");

		if (tfamFile != null) parseCaseControlTfam();
		else parseCaseControlString();

		// Read all vcfEntries
		VcfFileIterator vcf = new VcfFileIterator(vcfFileName);

		int i = 1;
		for (VcfEntry vcfEntry : vcf) {
			handleVcfHeader(vcf); // Handle header stuff
			annotate(vcfEntry); // Annotate

			// Show
			if (createList) list.add(vcfEntry);
			else System.out.println(vcfEntry);

			if (verbose) Gpr.showMark(i++, SHOW_EVERY);
		}

		if (verbose) {
			Timer.showStdErr("Done.\n\tMinimum pValue: " + pValueMin + "\tVcf entry: " + posMin);
		}
		return list;
	}

	/**
	 * Swap counts if REF is minor allele (instead of ALT)
	 * @param nControl
	 * @param nCase
	 */
	protected void swapMinorAllele(int nControl[], int nCase[]) {
		int refCount = 2 * nControl[0] + nControl[1] + 2 * nCase[0] + nCase[1];
		int altCount = 2 * nControl[2] + nControl[1] + 2 * nCase[2] + nCase[1];

		if (refCount < altCount) {
			if (debug) Gpr.debug("Swapping genotype counts");
			int tmp = nControl[0];
			nControl[0] = nControl[2];
			nControl[2] = tmp;

			tmp = nCase[0];
			nCase[0] = nCase[2];
			nCase[2] = tmp;
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

		System.err.println("Usage: java -jar " + SnpSift.class.getSimpleName() + ".jar caseControl [-v] [-name nameString] { -tfam file.tfam | <CaseControlString> } file.vcf");
		System.err.println("Where:");
		System.err.println("\t<CaseControlString> : A string of {'+', '-', '0'}, one per sample, to identify two groups (case='+', control='-', neutral='0')");
		System.err.println("\t -name nameStr      : A name to be added after to 'Cases' or 'Controls' tags");
		System.err.println("\t -tfam file.tfam    : A TFAM file having case/control informations (phenotype colmun)");
		System.err.println("\tfile.vcf            : A VCF file (variants and genotype data)");
		System.exit(1);
	}
}
