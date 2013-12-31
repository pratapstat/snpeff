package ca.mcgill.mcb.pcingola.snpEffect.testCases;

import junit.framework.Assert;
import junit.framework.TestCase;
import ca.mcgill.mcb.pcingola.interval.Exon;
import ca.mcgill.mcb.pcingola.interval.Gene;
import ca.mcgill.mcb.pcingola.interval.Transcript;
import ca.mcgill.mcb.pcingola.snpEffect.Config;
import ca.mcgill.mcb.pcingola.snpEffect.SnpEffectPredictor;
import ca.mcgill.mcb.pcingola.snpEffect.factory.SnpEffPredictorFactoryEmbl;

/**
 * Test case for EMBL file parsing (database creation)
 * 
 * @author pcingola
 */
public class TestCasesEmbl extends TestCase {

	public static boolean debug = false;

	public TestCasesEmbl() {
		super();
		Exon.ToStringVersion = 1; // Set "toString()" version
	}

	/**
	 * Build a genome from a embl file and compare results to 'expected' results
	 * @param genome
	 * @param emblFile
	 * @param resultFile
	 */
	public SnpEffectPredictor build(String genome, String emblFile) {
		// Build
		Config config = new Config(genome, Config.DEFAULT_CONFIG_FILE);
		SnpEffPredictorFactoryEmbl sepEmbl = new SnpEffPredictorFactoryEmbl(config, emblFile);
		SnpEffectPredictor sep = sepEmbl.create();

		return sep;
	}

	public void testCase_Exon_Simple() {
		// Create SnpEff predictor
		String genome = "testEmblPberghei";
		String resultFile = "tests/testEmblPberghei.genes.embl";
		SnpEffectPredictor sep = build(genome, resultFile);

		int pos = 4056 - 1;
		for (Gene g : sep.getGenome().getGenes()) {
			if (debug) System.out.println("Gene: '" + g.getGeneName() + "', '" + g.getId() + "'");
			for (Transcript tr : g) {
				if (debug) System.out.println("\tTranscript: '" + tr.getId() + "'");
				for (Exon e : tr) {
					if (debug) System.out.println("\t\tExon (" + e.getStrand() + "): '" + e.getId() + "'\t" + e.toStr());
					if (e.intersects(pos)) {
						String seq = e.getSequence();
						String base = e.basesAtPos(pos, 1);
						System.out.println("Seq : " + seq //
								+ "\nBase: " + base //
						);
						Assert.assertEquals("g", base);
					}
				}
			}
		}
	}
}
