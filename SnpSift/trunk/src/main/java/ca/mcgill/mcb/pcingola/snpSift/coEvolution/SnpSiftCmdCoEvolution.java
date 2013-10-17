package ca.mcgill.mcb.pcingola.snpSift.coEvolution;

import java.util.ArrayList;
import java.util.List;

import akka.actor.Actor;
import akka.actor.Props;
import akka.actor.UntypedActorFactory;
import ca.mcgill.mcb.pcingola.fileIterator.VcfFileIterator;
import ca.mcgill.mcb.pcingola.ped.PedPedigree;
import ca.mcgill.mcb.pcingola.ped.TfamEntry;
import ca.mcgill.mcb.pcingola.probablility.FisherExactTest;
import ca.mcgill.mcb.pcingola.snpSift.SnpSift;
import ca.mcgill.mcb.pcingola.snpSift.caseControl.SnpSiftCmdCaseControl;
import ca.mcgill.mcb.pcingola.snpSift.coEvolution.akka.MasterCoEvolution;
import ca.mcgill.mcb.pcingola.snpSift.coEvolution.akka.WorkQueueCoEvolution;
import ca.mcgill.mcb.pcingola.stats.CountByType;
import ca.mcgill.mcb.pcingola.util.Gpr;
import ca.mcgill.mcb.pcingola.util.Timer;
import ca.mcgill.mcb.pcingola.vcf.VcfEntry;

/**
 * Simple 'co-evolutionary inspired' case-control analysis
 * 
 * @author pcingola
 */
public class SnpSiftCmdCoEvolution extends SnpSiftCmdCaseControl {

	public static final int SHOW_EVERY = 1000;

	boolean isMulti;
	int minAlleleCount;
	double pvalueThreshold = 1e-4;
	List<String> sampleIds;
	ArrayList<byte[]> genotypes;
	ArrayList<String> entryId;

	public static void main(String[] args) {
		String vcfInput = Gpr.HOME + "/t2d1/eff/hm1.gt.vcf";
		// String vcfInput = Gpr.HOME + "/t2d1/eff/z.gt.vcf";
		String tfam = Gpr.HOME + "/t2d1/pheno/pheno.tfam";

		String argsCmd[] = { tfam, vcfInput };
		SnpSiftCmdCoEvolution coEvolution = new SnpSiftCmdCoEvolution(argsCmd);
		coEvolution.run();
	}

	public SnpSiftCmdCoEvolution(String args[]) {
		super(args);
		command = "coevolution";
	}

	/**
	 * Codes conversion table
	 * 
	 * 		code 1:    0/0      0/1      1/1
	 *                   0        1        2
	 *     code 2   +------------------------+
	 *     0/0   0  |    0        1        2 |
	 *     0/1   1  |    0        0        1 |
	 *     1/1   2  |    0        0        0 |
	 *              +------------------------+
	 *     
	 * @param code1
	 * @param code2
	 * @return
	 */
	int coEvolutionCode(int code1, int code2) {
		return Math.max(code1 - code2, 0);
	}

	public ArrayList<String> getEntryId() {
		return entryId;
	}

	public ArrayList<byte[]> getGenotypes() {
		return genotypes;
	}

	@Override
	public void init() {
		minAlleleCount = 10;
	}

	/**
	 * Load TFAM file
	 */
	void loadTfam() {
		// Load TFAM
		if (verbose) Timer.showStdErr("Reading TFAM file '" + tfamFile + "'");
		pedigree = new PedPedigree();
		pedigree.loadTfam(tfamFile);
	}

	/**
	 * Load VCF file
	 */
	void loadVcf() {
		// Initialize
		genotypes = new ArrayList<byte[]>();
		entryId = new ArrayList<String>();
		double minp = 1.0;

		// Read file
		if (verbose) Timer.showStdErr("Reading input file '" + vcfFileName + "'");
		VcfFileIterator vcf = new VcfFileIterator(vcfFileName);
		for (VcfEntry ve : vcf) {
			if (vcf.isHeadeSection()) {
				parseVcfHeader(vcf);
			}

			// Use if at least 'MIN_MAC' minor allele counts
			int mac = ve.mac();
			if (ve.isVariant() && (mac >= minAlleleCount)) {
				byte genoScores[] = ve.getGenotypesScores();
				genotypes.add(genoScores);
				entryId.add(ve.getChromosomeName() + ":" + (ve.getStart() + 1));

				// Show line
				if (debug) {
					// Calculate p-values
					double pvalue = pValue(genoScores);
					double logp = -Math.log10(pvalue);
					minp = Math.min(pvalue, minp);

					System.out.println(String.format("%s:%d\t%d\t%.2f\t%.4e\t%.4e\t", ve.getChromosomeName(), ve.getStart(), ve.mac(), logp, pvalue, minp));
				}
			}
		}

		if (verbose) Timer.showStdErr("Finished loading VCF. Loaded: " + genotypes.size() + " lines." + (minp < 1 ? "\n\tMin p-value: " + minp : ""));
	}

	/**
	 * Minor allele count
	 * @param scores
	 * @return
	 */
	int mac(byte scores[]) {
		int count = 0;
		for (int i = 0; i < scores.length; i++)
			if (scores[i] > 0) count += scores[i];
		return count;
	}

	double min(double d1, double d2, double d3, double d4) {
		return Math.min(Math.min(d1, d2), Math.min(d3, d4));
	}

	@Override
	public void parse(String[] args) {
		if (args.length <= 0) usage(null);

		for (int argc = 0; argc < args.length; argc++) {
			String arg = args[argc];

			if (isOpt(arg)) {
				// Command line options
				if (arg.equalsIgnoreCase("-minAc")) minAlleleCount = Gpr.parseIntSafe(args[++argc]);
			} else if (tfamFile == null) tfamFile = arg;
			else if (vcfFileName == null) vcfFileName = arg;
			else usage("Unkown parameter '" + arg + "'");
		}

		// Sanity check
		if (vcfFileName == null) usage("Missing paramter 'file.vcf'");
		if (tfamFile == null) usage("Missing parameter 'file.tfam'");
	}

	/**
	 * Parse VCF header to get sample IDs
	 * @param vcf
	 */
	List<String> parseVcfHeader(VcfFileIterator vcf) {
		// Get sample names
		sampleIds = vcf.getSampleNames();

		// Initialize sampleNum2group mapping
		CountByType countByGroup = new CountByType();
		caseControl = new Boolean[sampleIds.size()];
		int sampleNum = 0, missing = 0;
		for (String id : sampleIds) {
			TfamEntry tfam = pedigree.get(id);
			String groupId = "";

			if (tfam == null) {
				missing++;
				System.err.println("WARNING: VCF sample '" + id + "' not found in TFAM file.");
			} else {
				// Assign group
				caseControl[sampleNum] = tfam.isMissing() ? null : tfam.isCase();
				groupId = tfam.getFamilyId();
			}

			countByGroup.inc(groupId);
			sampleNum++;
		}

		if (missing == sampleIds.size()) throw new RuntimeException("All samples are missing in TFAM file!");

		// Show counts by group
		if (verbose) {
			System.err.println("Counts by group:\nGroup\tCount\n" + countByGroup);
			if (missing > 0) System.err.println("Samples from VCF missing in TFAM: " + missing);
			System.err.println("Total samples in VCF: " + sampleIds.size());
		}

		return sampleIds;
	}

	/**
	* Dominant model: Either a/A or A/A causes the disease
	* @param nControl
	* @param nCase
	* @return
	*/
	@Override
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
	 * Calculate pValue
	 * @param genoScores
	 * @return
	 */
	double pValue(byte genoScores[]) {
		int casesHom = 0, casesHet = 0, cases = 0;
		int ctrlHom = 0, ctrlHet = 0, ctrl = 0;
		int nCase[] = new int[3];
		int nControl[] = new int[3];

		// Count genotypes
		for (int idx = 0; idx < genoScores.length; idx++) {
			if ((caseControl[idx] != null)) {
				int code = genoScores[idx];

				if (code >= 0) {
					int codeMissing = Math.max(0, code);

					// Count a/a, a/A and A/A
					if (caseControl[idx]) nCase[code]++;
					else nControl[code]++;

					if (caseControl[idx]) {
						// Case sample
						if (code < 0) ; // Missing? => Do not count
						else if (code == 2) casesHom++;
						else casesHet++;

						cases += codeMissing;
					} else {
						//Control sample
						if (code < 0) ; // Missing? => Do not count
						else if (code == 2) ctrlHom++;
						else ctrlHet++;

						ctrl += codeMissing;
					}
				}
			}
		}

		// Add info fields
		if (debug) Gpr.debug("\n\tCases: " + casesHom + "," + casesHet + "," + cases + "\n\tControls: " + ctrlHom + "," + ctrlHet + "," + ctrl);

		// pValues
		double pCodominant = pCodominant(nControl, nCase);
		swapMinorAllele(nControl, nCase); // Swap if minor allele is reference
		double pAllelic = pAllelic(nControl, nCase);
		double pDominant = pDominant(nControl, nCase);
		double pRecessive = pRecessive(nControl, nCase);

		return min(pCodominant, pAllelic, pDominant, pRecessive);
	}

	/**
	 * Compare p-values between two genotypes
	 */
	public double pValue(byte scores1[], byte scores2[]) {
		int casesHom = 0, casesHet = 0, cases = 0;
		int ctrlHom = 0, ctrlHet = 0, ctrl = 0;
		int nCase[] = new int[3];
		int nControl[] = new int[3];

		// Count genotypes
		for (int idx = 0; idx < scores1.length; idx++) {
			if ((caseControl[idx] != null)) {
				int code1 = scores1[idx];
				int code2 = scores2[idx];

				if ((code1 >= 0) && (code2 >= 0)) {
					// Summarize two codes into one
					int code = coEvolutionCode(code1, code2);
					int codeMissing = Math.max(0, code);

					// Count a/a, a/A and A/A
					if (caseControl[idx]) nCase[code]++;
					else nControl[code]++;

					if (caseControl[idx]) {
						// Case sample
						if (code < 0) ; // Missing? => Do not count
						else if (code == 2) casesHom++;
						else casesHet++;

						cases += codeMissing;
					} else {
						//Control sample
						if (code < 0) ; // Missing? => Do not count
						else if (code == 2) ctrlHom++;
						else ctrlHet++;

						ctrl += codeMissing;
					}
				}
			}
		}

		// Add info fields
		if (debug) Gpr.debug("\n\tCases: " + casesHom + "," + casesHet + "," + cases + "\n\tControls: " + ctrlHom + "," + ctrlHet + "," + ctrl);

		// pValue
		return pDominant(nControl, nCase);
	}

	/**
	 * Run 
	 */
	@Override
	public void run() {
		loadTfam();
		loadVcf();
		runCoEvolution();
	}

	void runCoEvolution() {
		if (numWorkers <= 1) runCoEvolutionSingle();
		else runCoEvolutionMulti();
	}

	/**
	 * Analyze one entry compared to all other entries
	 * @param i
	 * @param pvalueThreshold
	 * @return
	 */
	public String runCoEvolution(int i) {
		int numEntries = genotypes.size();
		int count = 0;
		StringBuilder sb = new StringBuilder();

		for (int j = 0; j < numEntries; j++) {
			if (i != j) {
				double pvalue = pValue(genotypes.get(i), genotypes.get(j));
				count++;

				if (pvalue <= pvalueThreshold) {
					String out = String.format("\n[%d, %d]\t%s\t%s\t%.4e\t%d\t%d", i, j, entryId.get(i), entryId.get(j), pvalue, mac(genotypes.get(i)), mac(genotypes.get(j)));
					if (!isMulti) System.out.println(out);
					else sb.append(out + "\n");

				} else if (!isMulti) Gpr.showMark(count, SHOW_EVERY);
			}
		}

		if (verbose && !isMulti) Timer.showStdErr(entryId.get(i) + "\t" + i + "/" + numEntries);

		return sb.toString();
	}

	/**
	 * Run co-evolutionary algorithm
	 */
	void runCoEvolutionMulti() {
		if (verbose) Timer.showStdErr("Running on " + numWorkers + " cpus.");

		isMulti = true;

		final SnpSiftCmdCoEvolution snpSiftCmdCoEvolution = this;
		Props props = new Props(new UntypedActorFactory() {

			private static final long serialVersionUID = 1L;

			@Override
			public Actor create() {
				MasterCoEvolution master = new MasterCoEvolution(numWorkers, snpSiftCmdCoEvolution);
				return master;
			}
		});

		int batchSize = 1;
		WorkQueueCoEvolution workQueueCoEvolution = new WorkQueueCoEvolution(batchSize, 1, props);
		workQueueCoEvolution.run(true);

		if (verbose) Timer.showStdErr("Done!");
	}

	/**
	 * Run co-evolutionary algorithm
	 */
	void runCoEvolutionSingle() {
		if (verbose) Timer.showStdErr("Starting analysis.");

		for (int i = 0; i < genotypes.size(); i++)
			runCoEvolution(i);

		if (verbose) Timer.showStdErr("Done.");
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

		System.err.println("Usage: java -jar " + SnpSift.class.getSimpleName() + ".jar coevolution file.tfam file.vcf");
		System.err.println("Where:");
		System.err.println("\t-minAc <num>    : Filter using minimum number of alleles. Default: " + minAlleleCount);
		System.err.println("\tfile.tfam       : A TFAM file having case/control informations (phenotype colmun)");
		System.err.println("\tfile.vcf        : A VCF file (variants and genotype data)");
		System.exit(1);
	}

}
