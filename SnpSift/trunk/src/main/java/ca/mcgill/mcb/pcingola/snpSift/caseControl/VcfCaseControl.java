package ca.mcgill.mcb.pcingola.snpSift.caseControl;

import java.util.ArrayList;
import java.util.List;

import ca.mcgill.mcb.pcingola.probablility.CochranArmitageTest;
import ca.mcgill.mcb.pcingola.stats.FisherExactTest;
import ca.mcgill.mcb.pcingola.vcf.VcfEntry;
import ca.mcgill.mcb.pcingola.vcf.VcfGenotype;

/**
 * Calculate sample pValue form a VCF file.
 * I.e.: The probability of a SNP being in N or more cases).
 * 
 * @author pablocingolani
 */
public class VcfCaseControl {

	boolean addFisherInfo = false; // Used only for debugging purposes
	Boolean groupCase[]; // Is this group case or control?
	String groups;
	FisherExactTest fisherExactTest = FisherExactTest.get();

	public VcfCaseControl(String groups) {
		this.groups = groups;

		// Parse groups
		char groupsChars[] = groups.toCharArray();
		groupCase = new Boolean[groupsChars.length];
		for (int i = 0; i < groupsChars.length; i++) {
			switch (groupsChars[i]) {
			case '+':
				groupCase[i] = Boolean.TRUE;
				break;
			case '-':
				groupCase[i] = Boolean.FALSE;
				break;
			case '0':
				groupCase[i] = null;
				break;
			default:
				throw new RuntimeException("Error parsing case/control string. Unknow group character '" + groupCase[i] + "'");
			}
		}
	}

	/**
	 * Lines to be added to VCF header
	 * @return
	 */
	public List<String> addHeader() {
		ArrayList<String> addh = new ArrayList<String>();
		addh.add("##INFO=<ID=CC,Number=2,Type=Integer,Description=\"Number of cases and controls.\">");
		addh.add("##INFO=<ID=CC,Number=2,Type=Float,Description=\"P-Value of cases vs. controls using Fisher exact test.\">");
		addh.add("##INFO=<ID=CC_DOM,Number=1,Type=Float,Description=\"Case control: Cochran-Armitage test using dominant model\">");
		addh.add("##INFO=<ID=CC_REC,Number=1,Type=Float,Description=\"Case control: Cochran-Armitage test using recessive model\">");
		addh.add("##INFO=<ID=CC_COD,Number=1,Type=Float,Description=\"Case control: Cochran-Armitage test using co-dominance model\">");
		return addh;
	}

	/**
	 * Add p-value info
	 * @param bitSet
	 */
	public void addInfo(VcfEntry vcfEntry) {
		// Sanity check
		if (vcfEntry.getVcfGenotypes().size() != groupCase.length) throw new RuntimeException("Number of genotypes (samples) from Vcf file and CaseControlString provided by command line do not match:\n\tGenotypes: " + vcfEntry.getVcfGenotypes().size() + "\n\tCaseControl string length: " + groupCase.length + "\n\tVcfEntry: " + vcfEntry);

		// Only performs these tests when there is 1 REF and 1 ALT
		if (vcfEntry.getAlts().length != 1) return;

		// Iterate over all samples (there is one vcfGenotype per sample)
		int sampleNum = 0;
		int nCase[] = new int[3];
		int nControl[] = new int[3];

		for (VcfGenotype vcfGenotype : vcfEntry) {
			// Cochran-Armitage test
			int code = vcfGenotype.getGenotypeCode();

			// Any missing genotype? => Skip
			if (code >= 0) {
				if (groupCase[sampleNum] != null) {
					// Count a/a, a/A and A/A
					if (groupCase[sampleNum]) nCase[code]++;
					else nControl[code]++;
				}
			}

			sampleNum++;
		}

		//---
		// Calculate OR
		//---
		// TODO calculate OR
		//		!!!!!!!!!!!!!!!!!!

		//---
		// Calculate pValues
		//---
		//		vcfEntry.addInfo("CC_ALL", "" + pAllelic(nControl, nCase));
		vcfEntry.addInfo("CC_DOM", "" + pDominant(nControl, nCase));
		//		vcfEntry.addInfo("CC_REC", "" + pRecessive(nControl, nCase));
		//		vcfEntry.addInfo("CC_COD", "" + pCodominant(nControl, nCase)); // Also called 'TREND'
	}

	protected double pAllelic(int nControl[], int nCase[]) {
		return 0;
	}

	protected double pCodominant(int nControl[], int nCase[]) {
		return CochranArmitageTest.get().p(nControl, nCase, CochranArmitageTest.WEIGHT_CODOMINANT);
	}

	protected double pDominant(int nControl[], int nCase[]) {
		int k = nControl[0] + nControl[1];
		int N = nControl[0] + nControl[1] + nControl[2] + nCase[0] + nCase[1] + nCase[2];
		int D = nControl[0] + nControl[1] + nControl[2];
		int n = nControl[0] + nControl[1] + nCase[0] + nCase[1];
		return FisherExactTest.get().fisherExactTestUp(k, N, D, n);
	}

	protected double pRecessive(int nControl[], int nCase[]) {
		return 0;
	}

}
