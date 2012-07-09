package ca.mcgill.mcb.pcingola.snpSift.testCases;

import junit.framework.TestCase;

/**
 * Case control test cases
 * 
 * @author pcingola
 */
public class TestCasesCaseControl extends TestCase {

	public static boolean verbose = false;

	/**
	 * Calculate LD parameters
	 * Example from "Principles of population genetics", pages 79,80
	 */
	public void test_01() {

		throw new RuntimeException("FIx this!");

		//		// Parameters
		//		String ccString = "++++++++++--------";
		//		String fileName = "./test/john.vcf";
		//
		//		// Expected values
		//		String ccExpected[] = { "7/1,1/0", "7/3,1/7" };
		//		double ccpExpected[] = { 1.0, 2.296723e-02 };
		//
		//		// Create command line
		//		VcfCaseControl vcfCaseControl = new VcfCaseControl(ccString);
		//
		//		// Iterate over VCF entries
		//		int i = 0;
		//		VcfFileIterator vcfFile = new VcfFileIterator(fileName);
		//		for (VcfEntry vcfEntry : vcfFile) {
		//			vcfCaseControl.addInfo(vcfEntry);
		//
		//			Assert.assertEquals(ccExpected[i], vcfEntry.getInfo("CaseControl"));
		//			Assert.assertEquals(ccpExpected[i], Gpr.parseDoubleSafe(vcfEntry.getInfo("CaseControlP")), 0.001 * ccpExpected[i]);
		//			i++;
		//		}
	}
}
