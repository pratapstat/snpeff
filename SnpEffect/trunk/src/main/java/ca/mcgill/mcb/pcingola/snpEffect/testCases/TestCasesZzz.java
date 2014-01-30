package ca.mcgill.mcb.pcingola.snpEffect.testCases;

import java.util.Random;

import junit.framework.TestCase;
import ca.mcgill.mcb.pcingola.interval.Genome;
import ca.mcgill.mcb.pcingola.snpEffect.Config;

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

}
