package ca.mcgill.mcb.pcingola.snpSift.testCases;

import junit.framework.Assert;
import junit.framework.TestCase;
import ca.mcgill.mcb.pcingola.fileIterator.VcfFileIterator;
import ca.mcgill.mcb.pcingola.snpSift.SnpSiftCmdVarType;
import ca.mcgill.mcb.pcingola.vcf.VcfEntry;

/**
 * VarType test cases
 * 
 * @author pcingola
 */
public class TestCasesVarType extends TestCase {

	public static boolean verbose = false;

	/**
	 * Check that all variants in a file belong to a given type
	 * @param file
	 * @param varTypeExpected
	 */
	void checkAllVarType(String file, String varTypeExpected) {
		SnpSiftCmdVarType varType = new SnpSiftCmdVarType(null);

		VcfFileIterator vcf = new VcfFileIterator(file);
		for (VcfEntry ve : vcf) {
			// Annotate
			varType.annotate(ve);

			// Check that all variants are the ones expected
			if (!ve.getInfoFlag(varTypeExpected)) System.err.println("Eror in file '" + file + "':\n" + ve);
			Assert.assertEquals(true, ve.getInfoFlag(varTypeExpected));
		}
	}

	void checkVarTypeField(String file, String varTypeExpected) {
		SnpSiftCmdVarType varType = new SnpSiftCmdVarType(null);

		VcfFileIterator vcf = new VcfFileIterator(file);
		for (VcfEntry ve : vcf) {
			// Annotate
			varType.annotate(ve);

			// Check that all variants are the ones expected
			String varTypeAnnotated = ve.getInfo(SnpSiftCmdVarType.VARTYPE);
			Assert.assertEquals(varTypeExpected, varTypeAnnotated);
		}
	}

	public void test_01_SNP() {
		checkAllVarType("test/varType_snp.vcf", "SNP");
	}

	public void test_02_MNP() {
		checkAllVarType("test/varType_mnp.vcf", "MNP");
	}

	public void test_03_INS() {
		checkAllVarType("test/varType_ins.vcf", "INS");
	}

	public void test_04_DEL() {
		checkAllVarType("test/varType_del.vcf", "DEL");
	}

	public void test_05_multiple() {
		checkVarTypeField("test/varType_multiple.vcf", "SNP,INS");
	}

}
