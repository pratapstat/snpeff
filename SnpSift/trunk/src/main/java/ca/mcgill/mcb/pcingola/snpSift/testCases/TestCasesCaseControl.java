package ca.mcgill.mcb.pcingola.snpSift.testCases;

import java.util.List;

import junit.framework.TestCase;

import org.junit.Assert;

import ca.mcgill.mcb.pcingola.snpSift.caseControl.SnpSiftCmdCaseControl;
import ca.mcgill.mcb.pcingola.vcf.VcfEntry;

/**
 * Case control test cases
 * 
 * @author pcingola
 */
public class TestCasesCaseControl extends TestCase {

	public static boolean verbose = false;

	public static boolean debug = false;

	void checkCaseControl(String vcfFile, String tfamFile, String casesStr, String controlStr) {
		String args[] = { tfamFile, vcfFile };
		SnpSiftCmdCaseControl cmd = new SnpSiftCmdCaseControl(args);

		List<VcfEntry> vcfEntries = cmd.run(true);
		for (VcfEntry ve : vcfEntries) {
			System.out.println(ve);

			Assert.assertEquals(casesStr, ve.getInfo(SnpSiftCmdCaseControl.VCF_INFO_CASE));
			Assert.assertEquals(controlStr, ve.getInfo(SnpSiftCmdCaseControl.VCF_INFO_CONTROL));
		}
	}

	/**
	 * Non private
	 */
	public void test_01() {
		String vcfFile = "test/test.private.01.vcf";
		String tfamFile = "test/test.private.01.tfam";
		checkCaseControl(vcfFile, tfamFile, "", "");
	}

	//	/**
	//	 * Non private
	//	 */
	//	public void test_02() {
	//		String vcfFile = "test/test.private.02.vcf";
	//		String tfamFile = "test/test.private.01.tfam";
	//		checkCaseControl(vcfFile, tfamFile, false);
	//	}
	//
	//	/**
	//	 * Private variant
	//	 */
	//	public void test_03() {
	//		String vcfFile = "test/test.private.03.vcf";
	//		String tfamFile = "test/test.private.01.tfam";
	//		checkCaseControl(vcfFile, tfamFile, true);
	//	}
	//
	//	/**
	//	 * Private variant
	//	 */
	//	public void test_04() {
	//		String vcfFile = "test/test.private.04.vcf";
	//		String tfamFile = "test/test.private.01.tfam";
	//		checkCaseControl(vcfFile, tfamFile, true);
	//	}
	//
	//	public void test_05() {
	//		checkCaseControl("test/test.private.05.vcf", "test/test.private.05.tfam", false);
	//	}
	//
	//	public void test_06() {
	//		checkCaseControl("test/test.private.06.vcf", "test/test.private.05.tfam", true);
	//	}
	//
	//	public void test_07() {
	//		checkCaseControl("test/test.private.07.vcf", "test/test.private.05.tfam", true);
	//	}
	//
	//	public void test_08() {
	//		checkCaseControl("test/test.private.08.vcf", "test/test.private.05.tfam", false);
	//	}

}
