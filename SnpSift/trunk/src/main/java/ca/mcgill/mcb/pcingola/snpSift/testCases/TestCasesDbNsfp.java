package ca.mcgill.mcb.pcingola.snpSift.testCases;

import junit.framework.Assert;
import junit.framework.TestCase;
import ca.mcgill.mcb.pcingola.fileIterator.VcfFileIterator;
import ca.mcgill.mcb.pcingola.snpSift.SnpSiftCmdAnnotateSortedDbNsfp;
import ca.mcgill.mcb.pcingola.vcf.VcfEntry;

/**
 * Test GWAS catalog classes
 * 
 * @author pcingola
 */
public class TestCasesDbNsfp extends TestCase {

	public static boolean verbose = false;

	public void test_01() {
		String vcfFileName = "test/test_dbNSFP_chr1_69134.vcf";
		String args[] = { "-v", "test/dbNSFP2.0b3.chr1_69134.txt", "test/test_dbNSFP_chr1_69134.vcf" };
		SnpSiftCmdAnnotateSortedDbNsfp cmd = new SnpSiftCmdAnnotateSortedDbNsfp(args);

		try {
			cmd.initAnnotate();

			// Get entry.
			// Note: There is only one entry to annotate (the VCF file has one line)
			VcfFileIterator vcfFile = new VcfFileIterator(vcfFileName);
			VcfEntry vcfEntry = vcfFile.next();

			cmd.annotate(vcfEntry);

			// Check all values
			Assert.assertEquals("2.31", vcfEntry.getInfo(SnpSiftCmdAnnotateSortedDbNsfp.KEY_PREFIX + "GERP++_RS"));
			Assert.assertEquals("2.31", vcfEntry.getInfo(SnpSiftCmdAnnotateSortedDbNsfp.KEY_PREFIX + "GERP++_NR"));
			Assert.assertEquals("0.004785", vcfEntry.getInfo(SnpSiftCmdAnnotateSortedDbNsfp.KEY_PREFIX + "ESP5400_AA_AF"));
			Assert.assertEquals("8.5094", vcfEntry.getInfo(SnpSiftCmdAnnotateSortedDbNsfp.KEY_PREFIX + "29way_logOdds"));
			Assert.assertEquals("B", vcfEntry.getInfo(SnpSiftCmdAnnotateSortedDbNsfp.KEY_PREFIX + "Polyphen2_HVAR_pred"));
			Assert.assertEquals("0.090000", vcfEntry.getInfo(SnpSiftCmdAnnotateSortedDbNsfp.KEY_PREFIX + "SIFT_score"));
			Assert.assertEquals("Q8NH21", vcfEntry.getInfo(SnpSiftCmdAnnotateSortedDbNsfp.KEY_PREFIX + "Uniprot_acc"));
			Assert.assertEquals("ENST00000534990,ENST00000335137", vcfEntry.getInfo(SnpSiftCmdAnnotateSortedDbNsfp.KEY_PREFIX + "Ensembl_transcriptid"));

			cmd.endAnnotate();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
