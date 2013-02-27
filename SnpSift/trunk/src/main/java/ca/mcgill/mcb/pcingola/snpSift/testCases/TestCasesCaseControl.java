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
		String args[] = { "-tfam", tfamFile, vcfFile };
		SnpSiftCmdCaseControl cmd = new SnpSiftCmdCaseControl(args);

		List<VcfEntry> vcfEntries = cmd.run(true);
		for (VcfEntry ve : vcfEntries) {
			System.out.println(ve);
			Assert.assertEquals(casesStr, ve.getInfo(SnpSiftCmdCaseControl.VCF_INFO_CASE));
			Assert.assertEquals(controlStr, ve.getInfo(SnpSiftCmdCaseControl.VCF_INFO_CONTROL));
		}
	}

	public void test_01() {
		checkCaseControl("test/test.private.01.vcf", "test/test.private.01.tfam", "0,0,0", "0,0,0");
	}

	public void test_02() {
		checkCaseControl("test/test.private.02.vcf", "test/test.private.01.tfam", "0,0,0", "2,0,4");
	}

	public void test_03() {
		checkCaseControl("test/test.private.03.vcf", "test/test.private.01.tfam", "0,0,0", "0,1,1");
	}

	public void test_04() {
		checkCaseControl("test/test.private.04.vcf", "test/test.private.01.tfam", "0,0,0", "1,0,2");
	}

	public void test_05() {
		checkCaseControl("test/test.private.05.vcf", "test/test.private.05.tfam", "0,0,0", "0,0,0");
	}

	public void test_06() {
		checkCaseControl("test/test.private.06.vcf", "test/test.private.05.tfam", "0,1,1", "0,0,0");
	}

	public void test_07() {
		checkCaseControl("test/test.private.07.vcf", "test/test.private.05.tfam", "0,1,1", "0,1,1");
	}

	public void test_08() {
		checkCaseControl("test/test.private.08.vcf", "test/test.private.05.tfam", "0,2,2", "0,1,1");
	}

}
