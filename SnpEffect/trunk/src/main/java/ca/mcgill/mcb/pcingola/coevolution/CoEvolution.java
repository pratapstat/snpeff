package ca.mcgill.mcb.pcingola.coevolution;

import java.util.ArrayList;
import java.util.List;

import ca.mcgill.mcb.pcingola.fileIterator.VcfFileIterator;
import ca.mcgill.mcb.pcingola.ped.PedPedigree;
import ca.mcgill.mcb.pcingola.ped.TfamEntry;
import ca.mcgill.mcb.pcingola.probablility.FisherExactTest;
import ca.mcgill.mcb.pcingola.stats.CountByType;
import ca.mcgill.mcb.pcingola.util.Gpr;
import ca.mcgill.mcb.pcingola.util.Timer;
import ca.mcgill.mcb.pcingola.vcf.VcfEntry;

/**
 * Simple 'co-evolutionary inspired' case-control analysis
 * 
 * @author pcingola
 */
public class CoEvolution {

	public static final int MIN_MAC = 100;
	public static final int SHOW_EVERY = 1000;

	boolean debug = false;
	boolean verbose = true;
	String vcfFileName;
	String tfamFileName;
	PedPedigree pedigree;
	List<String> sampleIds;
	Boolean caseControl[];
	ArrayList<byte[]> genotypes;
	ArrayList<String> entryId;

	public static void main(String[] args) {
		String vcfInput = Gpr.HOME + "/t2d1/eff/hm1.gt.vcf";
		// String vcfInput = Gpr.HOME + "/t2d1/eff/z.gt.vcf";
		String tfam = Gpr.HOME + "/t2d1/pheno/pheno.tfam";

		CoEvolution coEvolution = new CoEvolution(vcfInput, tfam);
		coEvolution.run();
	}

	public CoEvolution(String vcf, String tfam) {
		vcfFileName = vcf;
		tfamFileName = tfam;
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
	int code(int code1, int code2) {
		return Math.max(code1 - code2, 0);
	}

	/**
	 * Load TFAM file
	 */
	void loadTfam() {
		// Load TFAM
		if (verbose) Timer.showStdErr("Reading TFAM file '" + tfamFileName + "'");
		pedigree = new PedPedigree();
		pedigree.loadTfam(tfamFileName);
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
			if (ve.isVariant() && (ve.mac() >= MIN_MAC)) {
				byte genoScores[] = ve.getGenotypesScores();
				genotypes.add(genoScores);
				entryId.add(ve.getChromosomeName() + ":" + (ve.getStart() + 1));

				// Show line
				if (debug) {
					// Calculate p-values
					double pvalue = pValue(genoScores);
					double logp = -Math.log10(pvalue);
					minp = Math.min(pvalue, minp);

					System.out.print(String.format("%s:%d\t%d\t%.2f\t%.4e\t%.4e\t", ve.getChromosomeName(), ve.getStart(), ve.mac(), logp, pvalue, minp));
					for (int i = 0; i < genoScores.length; i++)
						System.out.print(genoScores[i]);
					System.out.println("");
				}
			}
		}

		if (verbose) Timer.showStdErr("Done.\n\tLoaded: " + genotypes.size() + " lines." + (minp < 1 ? "\n\tMin p-value: " + minp : ""));
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

		// pValue
		return pDominant(nControl, nCase);
	}

	/**
	 * Compare p-values between two genotypes
	 */
	double pValue(byte scores1[], byte scores2[]) {
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
					int code = code(code1, code2);
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
	public void run() {
		loadTfam();
		loadVcf();
		runCoEvolution();
	}

	/**
	 * Run co-evolutionary algorithm
	 */
	void runCoEvolution() {
		//---
		// Run main analysis
		//---
		double minp = 1;
		int numEntries = genotypes.size();
		long count = 0;
		for (int i = 0; i < numEntries; i++) {
			for (int j = 0; j < numEntries; j++) {
				if (i != j) {
					double pvalue = pValue(genotypes.get(i), genotypes.get(j));

					count++;
					if (minp > pvalue) {
						minp = pvalue;
						System.out.println(String.format("\n%d\t%s\t%s\t%.4e\t%d\t%d", count, entryId.get(i), entryId.get(j), pvalue, mac(genotypes.get(i)), mac(genotypes.get(j))));
					} else Gpr.showMark((int) count, SHOW_EVERY);
				}
			}

			if (verbose) Timer.showStdErr(count + "\t" + entryId.get(i) + "\t" + i + "/" + numEntries);
		}

		if (verbose) Timer.showStdErr("Done. Number of combinations:\t" + count);
	}

}
