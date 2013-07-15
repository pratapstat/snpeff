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

	void checkCaseControlString(String vcfFile, String geoupStr, String casesStr, String controlStr) {
		String args[] = { geoupStr, vcfFile };
		SnpSiftCmdCaseControl cmd = new SnpSiftCmdCaseControl(args);

		List<VcfEntry> vcfEntries = cmd.run(true);
		for (VcfEntry ve : vcfEntries) {
			System.out.println(ve);
			Assert.assertEquals(casesStr, ve.getInfo(SnpSiftCmdCaseControl.VCF_INFO_CASE));
			Assert.assertEquals(controlStr, ve.getInfo(SnpSiftCmdCaseControl.VCF_INFO_CONTROL));
		}
	}

	void checkCaseControlTfam(String vcfFile, String tfamFile, String casesStr, String controlStr) {
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
		checkCaseControlTfam("test/test.private.01.vcf", "test/test.private.01.tfam", "0,0,0", "0,0,0");
	}

	public void test_01_Str() {
		checkCaseControlString("test/test.private.01.vcf", "--", "0,0,0", "0,0,0");
	}

	public void test_02() {
		checkCaseControlTfam("test/test.private.02.vcf", "test/test.private.01.tfam", "0,0,0", "2,0,4");
	}

	public void test_02_Str() {
		checkCaseControlString("test/test.private.02.vcf", "--", "0,0,0", "2,0,4");
	}

	public void test_02_Str2() {
		checkCaseControlString("test/test.private.02.vcf", "-+", "1,0,2", "1,0,2");
	}

	public void test_03() {
		checkCaseControlTfam("test/test.private.03.vcf", "test/test.private.01.tfam", "0,0,0", "0,1,1");
	}

	public void test_03_Str() {
		checkCaseControlString("test/test.private.03.vcf", "--", "0,0,0", "0,1,1");
	}

	public void test_03_Str2() {
		checkCaseControlString("test/test.private.03.vcf", "+-", "0,1,1", "0,0,0");
	}

	public void test_03_Str3() {
		checkCaseControlString("test/test.private.03.vcf", "0+", "0,0,0", "0,0,0");
	}

	public void test_04() {
		checkCaseControlTfam("test/test.private.04.vcf", "test/test.private.01.tfam", "0,0,0", "1,0,2");
	}

	public void test_04_Str() {
		checkCaseControlString("test/test.private.04.vcf", "--", "0,0,0", "1,0,2");
	}

	public void test_05() {
		checkCaseControlTfam("test/test.private.05.vcf", "test/test.private.05.tfam", "0,0,0", "0,0,0");
	}

	public void test_06() {
		checkCaseControlTfam("test/test.private.06.vcf", "test/test.private.05.tfam", "0,1,1", "0,0,0");
	}

	public void test_07() {
		checkCaseControlTfam("test/test.private.07.vcf", "test/test.private.05.tfam", "0,1,1", "0,1,1");
	}

	public void test_08() {
		checkCaseControlTfam("test/test.private.08.vcf", "test/test.private.05.tfam", "0,2,2", "0,1,1");
	}

}
