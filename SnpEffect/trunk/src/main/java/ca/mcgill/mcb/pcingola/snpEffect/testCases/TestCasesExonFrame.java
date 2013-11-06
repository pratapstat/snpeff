package ca.mcgill.mcb.pcingola.snpEffect.testCases;

import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;
import ca.mcgill.mcb.pcingola.snpEffect.commandLine.SnpEff;
import ca.mcgill.mcb.pcingola.snpEffect.commandLine.SnpEffCmdEff;
import ca.mcgill.mcb.pcingola.vcf.VcfEntry;

/**
 * Test case for exon frames
 * 
 * @author pcingola
 */
public class TestCasesExonFrame extends TestCase {

	public TestCasesExonFrame() {
		super();
	}

	/**
	 * Build test database
	 */
	public void test_01() {
		String genomeName = "testLukas";
		String args[] = { "build", "-v", "-noLog", "-gff3", genomeName };

		SnpEff snpEff = new SnpEff();
		snpEff.parseArgs(args);
		boolean ok = snpEff.run();

		Assert.assertTrue(ok);
	}

	/**
	 * Annotate using test database
	 */
	public void test_02() {
		String genomeName = "testLukas";
		String vcfFileName = "tests/testLukas.1.vcf";
		String args[] = { genomeName, vcfFileName };

		// Annotate
		SnpEffCmdEff snpEff = new SnpEffCmdEff();
		snpEff.parseArgs(args);
		List<VcfEntry> vcfEntries = snpEff.run(true);

		// Analyze annotations
		for (VcfEntry ve : vcfEntries) {
			System.out.println(ve);
		}

	}

}
