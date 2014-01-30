package ca.mcgill.mcb.pcingola.snpEffect.testCases;

import java.util.List;
import java.util.Random;

import junit.framework.Assert;
import junit.framework.TestCase;
import ca.mcgill.mcb.pcingola.interval.Genome;
import ca.mcgill.mcb.pcingola.snpEffect.Config;
import ca.mcgill.mcb.pcingola.snpEffect.commandLine.SnpEffCmdEff;
import ca.mcgill.mcb.pcingola.vcf.VcfEffect;
import ca.mcgill.mcb.pcingola.vcf.VcfEntry;

/**
 * 
 * Test case
 * 
 * @author pcingola
 */
public class TestCasesZzz extends TestCase {

	boolean verbose = false;
	Random rand;
	Config config;
	Genome genome;

	public TestCasesZzz() {
		super();
	}

	void initSnpEffPredictor(String genomeName) {
		// Create a config and force out snpPredictor for hg37 chromosome Y
		config = new Config(genomeName, Config.DEFAULT_CONFIG_FILE);
		config.loadSnpEffectPredictor();
		config.setTreatAllAsProteinCoding(true); // For historical reasons we set this one to 'true'....
		genome = config.getGenome();
		config.getSnpEffectPredictor().buildForest();
	}

	/**
	 * Insertion on minus strand
	 */
	public void test_34_InsOffByOne() {
		String args[] = { "testENST00000268124", "tests/ins_off_by_one.vcf" };

		SnpEffCmdEff snpeff = new SnpEffCmdEff();
		snpeff.parseArgs(args);

		List<VcfEntry> vcfEnties = snpeff.run(true);
		for (VcfEntry ve : vcfEnties) {

			// Get first effect (there should be only one)
			List<VcfEffect> veffs = ve.parseEffects();
			VcfEffect veff = veffs.get(0);

			Assert.assertEquals("Q53QQ", veff.getAa());
		}
	}
}
