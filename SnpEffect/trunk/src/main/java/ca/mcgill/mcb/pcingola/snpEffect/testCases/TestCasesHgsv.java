package ca.mcgill.mcb.pcingola.snpEffect.testCases;

import java.util.List;
import java.util.Random;

import junit.framework.Assert;
import junit.framework.TestCase;
import ca.mcgill.mcb.pcingola.codons.CodonTable;
import ca.mcgill.mcb.pcingola.interval.Chromosome;
import ca.mcgill.mcb.pcingola.interval.Exon;
import ca.mcgill.mcb.pcingola.interval.Gene;
import ca.mcgill.mcb.pcingola.interval.Genome;
import ca.mcgill.mcb.pcingola.interval.SeqChange;
import ca.mcgill.mcb.pcingola.interval.Transcript;
import ca.mcgill.mcb.pcingola.snpEffect.ChangeEffect;
import ca.mcgill.mcb.pcingola.snpEffect.Config;
import ca.mcgill.mcb.pcingola.snpEffect.SnpEffectPredictor;
import ca.mcgill.mcb.pcingola.snpEffect.factory.SnpEffPredictorFactoryRand;
import ca.mcgill.mcb.pcingola.util.GprSeq;

/**
 * Test random SNP changes 
 * 
 * @author pcingola
 */
public class TestCasesHgsv extends TestCase {

	boolean debug = false;
	Random rand;
	Config config;
	Genome genome;
	Chromosome chromosome;
	Gene gene;
	Transcript transcript;
	SnpEffectPredictor snpEffectPredictor;
	String chromoSequence = "";
	char chromoBases[];

	public TestCasesHgsv() {
		super();
		init();
	}

	void init() {
		initRand();
		initSnpEffPredictor();
	}

	void initRand() {
		rand = new Random(20130708);
	}

	void initSnpEffPredictor() {
		// Create a config and force out snpPredictor for hg37 chromosome Y
		config = new Config("testCase", Config.DEFAULT_CONFIG_FILE);

		// Create factory
		int maxGeneLen = 1000;
		int maxTranscripts = 1;
		int maxExons = 5;
		SnpEffPredictorFactoryRand sepf = new SnpEffPredictorFactoryRand(config, 1, rand, maxGeneLen, maxTranscripts, maxExons);

		// Create predictor
		sepf.setForcePositive(true); // WARNING: We only use positive strand here (the purpose is to check HGSV notation, not to check annotations)
		snpEffectPredictor = sepf.create();
		config.setSnpEffectPredictor(snpEffectPredictor);

		// Chromosome sequence
		chromoSequence = sepf.getChromoSequence();
		chromoBases = chromoSequence.toCharArray();

		// No upstream or downstream
		config.getSnpEffectPredictor().setUpDownStreamLength(0);

		// Build forest
		config.getSnpEffectPredictor().buildForest();

		chromosome = sepf.getChromo();
		genome = config.getGenome();
		gene = genome.getGenes().iterator().next();
		transcript = gene.iterator().next();
	}

	public void test_01() {
		int N = 250;
		CodonTable codonTable = genome.codonTable();

		// Test N times
		//	- Create a random gene transcript, exons
		//	- Change each base in the exon
		//	- Calculate effect
		for (int i = 0; i < N; i++) {
			initSnpEffPredictor();
			if (debug) System.out.println("HGSV Test iteration: " + i + "\n" + transcript);
			else System.out.println("HGSV Test iteration: " + i + "\t" + (transcript.getStrand() >= 0 ? "+" : "-") + "\t" + transcript.cds());

			int cdsBaseNum = 0;

			// For each exon...
			for (Exon exon : transcript.sortedStrand()) {
				// For each base in this exon...
				for (int pos = exon.getStart(); (exon.getStart() <= pos) && (pos <= exon.getEnd()); pos++, cdsBaseNum++) {

					// Reference base
					char refBase = chromoBases[pos]; // exon.basesAt(pos - exon.getStart(), 1).charAt(0);
					refBase = Character.toUpperCase(refBase);
					// Codon number
					int cdsCodonNum = cdsBaseNum / 3;
					int cdsCodonPos = cdsBaseNum % 3;

					int minCodonPos = cdsCodonNum * 3;
					int maxCodonPos = minCodonPos + 3;
					if (maxCodonPos < transcript.cds().length()) {
						String codon = transcript.cds().substring(minCodonPos, maxCodonPos);
						codon = codon.toUpperCase();
						String aa = codonTable.aaThreeLetterCode(codonTable.aa(codon));

						// Get a random base different from 'refBase'
						char snp = refBase;
						while (snp == refBase)
							snp = Character.toUpperCase(GprSeq.randBase(rand));

						// Codon change
						String newCodon = codon.substring(0, cdsCodonPos) + snp + codon.substring(cdsCodonPos + 1);
						String newAa = codonTable.aaThreeLetterCode(codonTable.aa(newCodon));
						String effectExpected = "";

						// Effect
						if (newAa.equals(aa)) {
							if ((cdsCodonNum == 0) && (codonTable.isStart(codon))) {
								if (codonTable.isStart(newCodon)) effectExpected = "p." + aa + "1?";
								else effectExpected = "p." + aa + "1?";
							} else effectExpected = "p.(=)/c." + (cdsBaseNum + 1) + refBase + ">" + snp;
						} else {
							if ((cdsCodonNum == 0) && (codonTable.isStart(codon))) {
								if (codonTable.isStart(newCodon)) effectExpected = "p." + aa + "1?";
								else effectExpected = "p." + aa + "1?";
							} else if (codonTable.isStop(codon)) effectExpected = "p." + aa + (cdsCodonNum + 1) + newAa + "ext*?";
							else if (codonTable.isStop(newCodon)) effectExpected = "p." + aa + (cdsCodonNum + 1) + "*";
							else effectExpected = "p." + aa + (cdsCodonNum + 1) + newAa;
						}

						// Create a SeqChange
						SeqChange seqChange = new SeqChange(chromosome, pos, refBase + "", snp + "", 1, "", 1.0, 1);

						if (!seqChange.isChange()) effectExpected = "EXON";

						// Calculate effects
						List<ChangeEffect> effects = snpEffectPredictor.seqChangeEffect(seqChange);

						// There should be only one effect
						Assert.assertEquals(true, effects.size() <= 1);

						// Show
						if (effects.size() == 1) {
							ChangeEffect effect = effects.get(0);
							String effStr = effect.getHgvs();
							if (debug) System.out.println("\tPos: " + pos //
									+ "\tCDS base num: " + cdsBaseNum + " [" + cdsCodonNum + ":" + cdsCodonPos + "]" //
									+ "\t" + seqChange + (seqChange.getStrand() >= 0 ? "+" : "-") //
									+ "\tCodon: " + codon + " -> " + newCodon //
									+ "\tAA: " + aa + " -> " + newAa //
									+ "\tEffect expected: " + effectExpected //
									+ "\tEffect: " + effStr);

							// Check effect
							Assert.assertEquals(effectExpected, effStr);
						}
					}
				}
			}
		}
	}
}
