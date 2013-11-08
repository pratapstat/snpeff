package ca.mcgill.mcb.pcingola.snpSift.coEvolution;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
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
import ca.mcgill.mcb.pcingola.vcf.VcfEffect;
import ca.mcgill.mcb.pcingola.vcf.VcfEntry;

/**
 * Simple 'co-evolutionary inspired' case-control analysis
 * 
 * @author pcingola
 */
public class SnpSiftCmdCoEvolution extends SnpSiftCmdCaseControl {

	public enum ModelCoevolution {
		ABS, MAX
	};

	public enum ModelPvalue {
		Allelic, Dominant, Recessive, Codominant
	};

	public static final int SHOW_EVERY = 1000;

	boolean isMulti;
	boolean genesMatch[];
	int minAlleleCount;
	int showNonSignificant; // Non significant test can be shown every now and then (if this value is positive)
	long countTests;
	String rFileName;
	ModelCoevolution model;
	List<String> sampleIds;
	ArrayList<byte[]> genotypes;
	ArrayList<String> entryId;
	HashSet<String> genes;
	BufferedWriter rFile;

	public SnpSiftCmdCoEvolution(String args[]) {
		super(args);
		command = "coevolution";
	}

	int coEvolutionCode(int code1, int code2) {
		switch (model) {
		case ABS:
			/**
			 * Codes conversion table
			 * 
			 * 		code 1:    0/0      0/1      1/1
			 *                   0        1        2
			 *     code 2   +------------------------+
			 *     0/0   0  |    0        1        2 |
			 *     0/1   1  |    1        0        1 |
			 *     1/1   2  |    2        1        0 |
			 *              +------------------------+
			 */
			return Math.abs(code1 - code2);

		case MAX:
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
			 */
			return Math.max(code1 - code2, 0);

		default:
			throw new RuntimeException("Unimplemented model '" + model + "'");
		}
	}

	/**
	 * Create an ID for this VCF entry
	 * @param ve
	 * @return
	 */
	String entryId(VcfEntry ve) {
		// Try to get gene names
		HashSet<String> done = new HashSet<String>();
		StringBuilder sb = new StringBuilder();
		for (VcfEffect veff : ve.parseEffects()) {
			String gene = veff.getGene();
			if ((gene != null) && !done.contains(gene)) {
				sb.append((sb.length() > 0 ? "," : "") + gene);
				done.add(gene);
			}
		}

		// ID
		return ve.getChromosomeName() //
				+ ":" + (ve.getStart() + 1) //
				+ "_" + ve.getRef() //
				+ "/" + ve.getAltsStr() //
				+ " " + sb.toString() //
				+ " " + ve.getId() //
		;
	}

	/**
	 * Obtain genotype scores
	 * @param ve
	 * @return
	 */
	byte[] genotypesScores(VcfEntry ve) {
		byte gs[] = ve.getGenotypesScores();

		// Do we need to swap alleles (REF <-> ALT)
		// Note: We swap alleles in order to have 'ALT' always being the minor allele
		int count = 0, alleles = 0;
		for (int i = 0; i < gs.length; i++) {
			if (gs[i] >= 0) {
				count += 2;
				alleles += gs[i];
			}
		}

		// ALT is more common than REF? Swap alleles 
		if (alleles > count / 2) {
			if (debug) Gpr.debug("Swapping alleles for entry: " + ve.toStr());
			for (int i = 0; i < gs.length; i++)
				if (gs[i] > 0) gs[i] = (byte) (2 - gs[i]);
		}

		return gs;
	}

	public ArrayList<String> getEntryId() {
		return entryId;
	}

	public ArrayList<byte[]> getGenotypes() {
		return genotypes;
	}

	/**
	 * Count how many tests have been performed
	 * @return
	 */
	synchronized long incCountTests() {
		return ++countTests;
	}

	@Override
	public void init() {
		minAlleleCount = 10;
		pvalueThreshold = 1e-4;
		model = ModelCoevolution.ABS;
		rFileName = null; // Don't write details unless specified in the command line
		showNonSignificant = 0; // By default do not show any non-significant results
		countTests = 0;
	}

	/**
	 * Initialize genesMatch
	 */
	void initMatchGenes() {
		genesMatch = new boolean[genotypes.size()];
		for (int i = 0; i < genesMatch.length; i++)
			genesMatch[i] = matchGenes(i);
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
				byte genoScores[] = genotypesScores(ve);

				genotypes.add(genoScores);
				entryId.add(entryId(ve));

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

	/**
	 * Does this entry matches "genes" command line option?
	 * @param i
	 * @return
	 */
	boolean matchGenes(int i) {
		// Do we have a preferred list of genes to focus on?
		if (genes == null) return true; // No 'genes' set? Everything matches

		// Get genes from entryId
		String eid[] = entryId.get(i).split(" ");
		if (eid.length < 2) return false; // There is no genes information in this variant

		// Any match?
		String genesStr = eid[1];
		for (String g : genesStr.split(","))
			if (genes.contains(g)) return true;

		return false;
	}

	/**
	 * Do either entries match 'genes' from command line?
	 * @param i
	 * @param j
	 * @return
	 */
	boolean matchGenes(int i, int j) {
		return genesMatch[i] || genesMatch[j];
	}

	/**
	 * Find the minimum non-zero pvalue
	 * Note: If a p-value is exactly zero, there is something probably wrong with the statistic.
	 * @param pvalues
	 * @return
	 */
	double minPvalue(double pvalues[]) {
		double pvalue = 1.0;
		for (int i = 0; i < pvalues.length; i++) {
			double p = pvalues[i];
			if (p <= 0.0) p = 1.0;
			pvalue = Math.min(pvalue, p);
		}
		return pvalue;

	}

	@Override
	public void parse(String[] args) {
		if (args.length <= 0) usage(null);

		for (int argc = 0; argc < args.length; argc++) {
			String arg = args[argc];

			if (isOpt(arg)) {
				// Command line options
				if (arg.equalsIgnoreCase("-minAc")) minAlleleCount = Gpr.parseIntSafe(args[++argc]);
				else if (arg.equalsIgnoreCase("-show")) showNonSignificant = Gpr.parseIntSafe(args[++argc]);
				else if (arg.equalsIgnoreCase("-maxP")) pvalueThreshold = Gpr.parseDoubleSafe(args[++argc]);
				else if (arg.equalsIgnoreCase("-model")) model = ModelCoevolution.valueOf(args[++argc].toUpperCase());
				else if (arg.equalsIgnoreCase("-out")) rFileName = args[++argc];
				else if (arg.equalsIgnoreCase("-genes")) {
					String genesStr = args[++argc];
					genes = new HashSet<String>();
					for (String g : genesStr.split(","))
						genes.add(g);
				}
			} else if (tfamFile == null) tfamFile = arg;
			else if (vcfFileName == null) vcfFileName = arg;
			else usage("Unkown parameter '" + arg + "'");
		}

		// Sanity check
		if (vcfFileName == null) usage("Missing paramter 'file.vcf'");
		if (tfamFile == null) usage("Missing parameter 'file.tfam'");
		if (showNonSignificant < 0) usage("Parameter '-show' must have a non-negative number");
		if ((pvalueThreshold <= 0) || (pvalueThreshold > 1)) usage("Parameter '-maxP' must have a number between 0 and 1. Current value: " + pvalueThreshold);
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
	 * Power calculation
	 */
	void powerCalculation() {
		int nCase[] = new int[3];
		int nControl[] = new int[3];
		int cases = 0, controls = 0;

		// Count cases & controls
		for (int idx = 0; idx < caseControl.length; idx++) {
			if ((caseControl[idx] != null)) {
				// Count a/a, a/A and A/A
				if (caseControl[idx]) cases++;
				else controls++;
			}
		}

		Timer.showStdErr("Power calculation:" //
				+ "\n\tCases    : " + cases //
				+ "\n\tControls : " + controls //
		);

		int numEntries = genotypes.size();
		double pvalueSignificant = 0.05 / (numEntries * numEntries);

		// Calculate pvalues (best case scenario)
		String powerStr = "";
		for (int numCases = cases; numCases > 0; numCases--) {
			nCase[0] = 2 * (cases - numCases);
			nCase[1] = numCases;
			nCase[2] = 0;

			nControl[0] = 2 * controls;
			nControl[1] = 0;
			nControl[2] = 0;

			double pvalues[] = pvalues(nCase, nControl);
			double pvalue = minPvalue(pvalues);
			if (debug && pvalue <= pvalueThreshold) System.out.println("NumCases : " + numCases + "\tp-value: " + pvalue);
			if (pvalue <= pvalueSignificant) powerStr = String.format("" //
					+ "\n\tSignificance threshold : %e" //
					+ "\n\tNumber of cases        : %d" //
					+ "\n\tp-value                : %e" //
			, pvalueSignificant, numCases, pvalue);
		}

		Timer.showStdErr("Done:" + powerStr);
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

		return minPvalue(pvalues(nControl, nCase));
	}

	/**
	 * Compare p-values between two genotypes
	 */
	public double[] pValue(int i1, int i2) {
		byte scores1[] = genotypes.get(i1);
		byte scores2[] = genotypes.get(i2);
		byte codes[] = new byte[scores1.length];
		int casesHom = 0, casesHet = 0, cases = 0;
		int ctrlHom = 0, ctrlHet = 0, ctrl = 0;
		int nCase[] = new int[3];
		int nControl[] = new int[3];

		// Count genotypes
		for (int idx = 0; idx < scores1.length; idx++) {
			codes[idx] = -1;

			if ((caseControl[idx] != null)) {
				int code1 = scores1[idx];
				int code2 = scores2[idx];

				if ((code1 >= 0) && (code2 >= 0)) {
					// Summarize two codes into one
					int code = coEvolutionCode(code1, code2);
					codes[idx] = (byte) code;

					// Count a/a, a/A and A/A
					if (caseControl[idx]) nCase[code]++;
					else nControl[code]++;

					if (caseControl[idx]) {
						// Case sample
						if (code < 0) ; // Missing? => Do not count
						else if (code == 2) casesHom++;
						else casesHet++;

						cases += code;
					} else {
						//Control sample
						if (code < 0) ; // Missing? => Do not count
						else if (code == 2) ctrlHom++;
						else ctrlHet++;

						ctrl += code;
					}
				}
			}
		}

		// Calculate pValues
		double pvalues[] = pvalues(nCase, nControl);
		double pvalue = minPvalue(pvalues);

		// Sanity check
		if (pvalue == 0.0) Gpr.debug("p-value is zero!" + "\n\t" + entryId.get(i1) + "\t" + entryId.get(i2) + "\n\tCases: " + casesHom + "," + casesHet + "," + cases + "\n\tControls: " + ctrlHom + "," + ctrlHet + "," + ctrl);
		if (debug) Gpr.debug(entryId.get(i1) + "\t" + entryId.get(i2) + "\n\tCases: " + casesHom + "," + casesHet + "," + cases + "\n\tControls: " + ctrlHom + "," + ctrlHet + "," + ctrl);

		// Return a tuple
		long count = incCountTests();
		if (pvalue > pvalueThreshold) {
			// Over threshold? Show one every SHOW_NON_SIGNIFICANT values
			// This is used to build the full QQ plot
			if (!((showNonSignificant > 0) && (count % showNonSignificant == 0))) return null; // Do not show
		}

		writeR(i1, i2, pvalues, codes);
		return pvalues;
	}

	/**
	 * Calculate the best p-value
	 * 
	 * Note: It looks like we could remove the recessive model 
	 *       test (QQ plots show it's not doing much).
	 *       Furthermore, using only Allellic model might be 
	 *       enough...
	 * 
	 * @param nCase
	 * @param nControl
	 * @return
	 */
	double[] pvalues(int nCase[], int nControl[]) {
		// pValues
		double pTrend = pTrend(nControl, nCase);
		double pAllelic = pAllelic(nControl, nCase);
		double pDominant = pDominant(nControl, nCase);
		swapMinorAllele(nControl, nCase); // Swap if minor allele is reference
		double pRecessive = pRecessive(nControl, nCase);

		// Return an array of pvalues
		double pvalues[] = { pAllelic, pDominant, pRecessive, pTrend };
		return pvalues;
	}

	/**
	 * Run 
	 */
	@Override
	public void run() {
		try {
			// Load data
			loadTfam();
			loadVcf();
			initMatchGenes();

			// Open 'R' file
			if (rFileName != null) {
				if (verbose) Timer.showStdErr("Writing results to file '" + rFileName + "'");
				rFile = new BufferedWriter(new FileWriter(rFileName));
			} else {
				if (verbose) Timer.showStdErr("No results file will be generated.");
				rFile = null;
			}
			writeRTitle();

			// Run
			if (debug) powerCalculation();
			runCoEvolution();

			if (rFile != null) rFile.close();
			if (verbose) Timer.showStdErr("Results written to file '" + rFileName + "'");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	void runCoEvolution() {
		if (numWorkers <= 1) runCoEvolutionSingle();
		else runCoEvolutionMulti();
	}

	/**
	 * Analyze one entry compared to all other entries
	 * @param i
	 * @return
	 */
	public String runCoEvolution(int i) {
		int numEntries = genotypes.size();
		int count = 0;
		StringBuilder sb = new StringBuilder();

		// Iteration start
		// Note: 'Abs' model yields the same result for [i, j] than [j, i] whereas 'max' model doesn't
		int minj = 0;
		if (model == ModelCoevolution.ABS) minj = i + 1;

		// Iterate on all entries
		for (int j = minj; j < numEntries; j++) {
			if (i != j) {
				// Does this entry match 'genes'?
				if (matchGenes(i, j)) {

					double pvalues[] = pValue(i, j);
					count++;

					if (pvalues != null) {
						String out = String.format("Result\t%.4e\t%.4e\t%.4e\t%.4e\t[%d, %d]\t%s\t%s\t%d\t%d", pvalues[0], pvalues[1], pvalues[2], pvalues[3], i, j, entryId.get(i), entryId.get(j), mac(genotypes.get(i)), mac(genotypes.get(j)));
						if (!isMulti) System.out.println(out);
						else sb.append((sb.length() > 0 ? "\n" : "") + out);

					} else if (!isMulti) Gpr.showMark(count, SHOW_EVERY);
				}
			}
		}

		if (verbose && !isMulti) Timer.showStdErr(entryId.get(i) + "\t" + i + "/" + numEntries);

		return sb.toString();
	}

	/**
	 * Run co-evolutionary algorithm
	 */
	void runCoEvolutionMulti() {
		if (verbose) Timer.showStdErr("Starting analysis on " + numWorkers + " cpus.");

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

		if (verbose) Timer.showStdErr("Done. Total tests performed: " + countTests);
	}

	/**
	 * Run co-evolutionary algorithm
	 */
	void runCoEvolutionSingle() {
		if (verbose) Timer.showStdErr("Starting analysis.");

		for (int i = 0; i < genotypes.size(); i++)
			runCoEvolution(i);

		if (verbose) Timer.showStdErr("Done. Total tests performed: " + countTests);
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
		System.err.println("\t-genes <list>   : Comma separated list of genes to analyze. Default: None");
		System.err.println("\t-maxP  <num>    : Maximum p-value to report. Default: " + pvalueThreshold);
		System.err.println("\t-minAc <num>    : Filter using minimum number of alleles. Default: " + minAlleleCount);
		System.err.println("\t-model <model>  : Model to use {ABS, MAX}. Default: " + model);
		System.err.println("\t-out    <file>  : Raw data output. Default: " + rFileName);
		System.err.println("\t-show  <num>    : Show non-significant p-values every 'num' iterations. Default: " + showNonSignificant);
		System.err.println("\tfile.tfam       : A TFAM file having case/control informations (phenotype colmun)");
		System.err.println("\tfile.vcf        : A VCF file (variants and genotype data)");
		System.exit(1);
	}

	/**
	 * Show in a way that R can read (logistic regression)
	 * @param i1
	 * @param i2
	 * @param codes
	 */
	void writeR(int i1, int i2, double pvalues[], byte codes[]) {
		StringBuilder sb = new StringBuilder();

		// Preapre string
		sb.append(entryId.get(i1).replace(' ', '\t'));
		sb.append('\t');
		sb.append(entryId.get(i2).replace(' ', '\t'));

		// Pvalues
		for (int i = 0; i < pvalues.length; i++)
			sb.append("\t" + pvalues[i]);

		for (int i = 0; i < codes.length; i++)
			sb.append("\t" + codes[i]);
		sb.append('\n');

		// Write it to file
		writeR(sb.toString());
	}

	/**
	 * Make sure only one process is writing at the same time
	 * @param str
	 */
	synchronized void writeR(String str) {
		try {
			if (rFile != null) rFile.write(str);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Show title in R file
	 */
	void writeRTitle() {
		StringBuilder sb = new StringBuilder();

		// Write title
		// { pAllelic, pDominant, pRecessive, pCodominant };
		sb.append("pos1\tgene1\tid1\tpos2\tgene2\tid2\tpAllelic\tpDominant\tpRecessive\tpCodominant");
		for (String sid : sampleIds)
			sb.append("\t" + sid);
		sb.append('\n');

		// Write phenotypes
		sb.append("pheno\tpheno\tpheno\tpheno\tpheno\tpheno\t1.0\t1.0\t1.0\t1.0");
		for (int i = 0; i < caseControl.length; i++) {
			int num = -1;
			if (caseControl[i] != null) num = caseControl[i] ? 1 : 0;
			sb.append("\t" + num);
		}
		sb.append('\n');

		writeR(sb.toString());
	}

}
