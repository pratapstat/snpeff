package ca.mcgill.mcb.pcingola.snpSift.testCases;

import junit.framework.TestCase;

import org.junit.Assert;

import ca.mcgill.mcb.pcingola.fileIterator.VcfFileIterator;
import ca.mcgill.mcb.pcingola.snpSift.SnpSiftCmdAnnotateSortedDbNsfp;
import ca.mcgill.mcb.pcingola.util.Gpr;
import ca.mcgill.mcb.pcingola.vcf.VcfEntry;

/**
 * Test GWAS catalog classes
 * 
 * @author pcingola
 */
public class TestCasesDbNsfp extends TestCase {

	public static boolean verbose = false;
	public static boolean debug = false;

	public void test_01() {
		String vcfFileName = "test/test_dbNSFP_chr1_69134.vcf";
		String args[] = { "test/dbNSFP2.0b3.chr1_69134.txt", vcfFileName };
		SnpSiftCmdAnnotateSortedDbNsfp cmd = new SnpSiftCmdAnnotateSortedDbNsfp(args);
		cmd.setVerbose(verbose);
		cmd.setDebug(debug);

		try {
			cmd.initAnnotate();

			// Get entry.
			// Note: There is only one entry to annotate (the VCF file has one line)
			VcfFileIterator vcfFile = new VcfFileIterator(vcfFileName);
			VcfEntry vcfEntry = vcfFile.next();

			cmd.annotate(vcfEntry);
			if (debug) Gpr.debug(vcfEntry);

			// Check all values
			Assert.assertEquals("2.31", vcfEntry.getInfo(SnpSiftCmdAnnotateSortedDbNsfp.VCF_INFO_PREFIX + "GERP++_RS"));
			Assert.assertEquals("2.31", vcfEntry.getInfo(SnpSiftCmdAnnotateSortedDbNsfp.VCF_INFO_PREFIX + "GERP++_NR"));
			Assert.assertEquals("0.004785", vcfEntry.getInfo(SnpSiftCmdAnnotateSortedDbNsfp.VCF_INFO_PREFIX + "ESP6500_AA_AF"));
			Assert.assertEquals("8.5094", vcfEntry.getInfo(SnpSiftCmdAnnotateSortedDbNsfp.VCF_INFO_PREFIX + "29way_logOdds"));
			Assert.assertEquals("B", vcfEntry.getInfo(SnpSiftCmdAnnotateSortedDbNsfp.VCF_INFO_PREFIX + "Polyphen2_HVAR_pred"));
			Assert.assertEquals("0.090000", vcfEntry.getInfo(SnpSiftCmdAnnotateSortedDbNsfp.VCF_INFO_PREFIX + "SIFT_score"));
			Assert.assertEquals("Q8NH21", vcfEntry.getInfo(SnpSiftCmdAnnotateSortedDbNsfp.VCF_INFO_PREFIX + "Uniprot_acc"));
			Assert.assertEquals("ENST00000534990,ENST00000335137", vcfEntry.getInfo(SnpSiftCmdAnnotateSortedDbNsfp.VCF_INFO_PREFIX + "Ensembl_transcriptid"));

			cmd.endAnnotate();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Test dbnsfp having multiple lines per variant
	 */
	public void test_02() {
		String vcfFileName = "test/test_dbnsfp_multiple.vcf";
		String fields = "genename,Ensembl_geneid,Ensembl_transcriptid";
		String args[] = { "-f", fields, "test/test_dbnsfp_multiple_lines.txt", vcfFileName };

		SnpSiftCmdAnnotateSortedDbNsfp cmd = new SnpSiftCmdAnnotateSortedDbNsfp(args);
		cmd.setVerbose(verbose);
		cmd.setDebug(debug);

		try {
			cmd.initAnnotate();

			// Get entry.
			// Note: There is only one entry to annotate (the VCF file has one line)
			VcfFileIterator vcfFile = new VcfFileIterator(vcfFileName);
			VcfEntry vcfEntry = vcfFile.next();

			cmd.annotate(vcfEntry);
			if (debug) Gpr.debug(vcfEntry);

			// Check all values
			Assert.assertEquals("ENST00000368485,ENST00000515190", vcfEntry.getInfo(SnpSiftCmdAnnotateSortedDbNsfp.VCF_INFO_PREFIX + "Ensembl_transcriptid"));
			Assert.assertEquals("IL6R", vcfEntry.getInfo(SnpSiftCmdAnnotateSortedDbNsfp.VCF_INFO_PREFIX + "genename"));
			Assert.assertEquals("ENSG00000160712", vcfEntry.getInfo(SnpSiftCmdAnnotateSortedDbNsfp.VCF_INFO_PREFIX + "Ensembl_geneid"));

			cmd.endAnnotate();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
