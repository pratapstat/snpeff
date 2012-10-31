package ca.mcgill.mcb.pcingola.snpEffect.testCases;

import java.io.Serializable;
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
import ca.mcgill.mcb.pcingola.snpEffect.ChangeEffect.EffectType;
import ca.mcgill.mcb.pcingola.snpEffect.Config;
import ca.mcgill.mcb.pcingola.snpEffect.SnpEffectPredictor;
import ca.mcgill.mcb.pcingola.snpEffect.factory.SnpEffPredictorFactoryRand;
import ca.mcgill.mcb.pcingola.util.Gpr;
import ca.mcgill.mcb.pcingola.util.GprSeq;

class Save implements Serializable {

	private static final long serialVersionUID = 3888380698995710933L;

	public SnpEffectPredictor snpEffectPredictor;
	public String chromoSequence;
	public String chromoNewSequence;
	public String ref;
	public String mnp;

	public int pos;
	public int i;
}

/**
 * Test random SNP changes 
 * 
 * @author pcingola
 */
public class TestCasesMnp extends TestCase {

	static boolean debug = false;
	static int MAX_MNP_LEN = 10;

	// Create factory
	int maxGeneLen = 1000;
	int maxTranscripts = 1;
	int maxExons = 5;

	Random rand;
	Config config;
	Genome genome;
	CodonTable codonTable;
	Chromosome chromosome;
	Gene gene;
	Transcript transcript, transcriptNew;
	SnpEffectPredictor snpEffectPredictor;
	String chromoSequence = "";
	String chromoNewSequence = "";
	char chromoBases[];

	String codonsOld = "";
	String codonsNew = "";

	public TestCasesMnp() {
		super();
		init();
	}

	void addIfDiff(char codonOld[], char codonNew[]) {
		String cold = new String(codonOld);
		String cnew = new String(codonNew);
		if (!cold.equals(cnew)) {
			codonsOld += transcript.isStrandPlus() ? cold : GprSeq.wc(cold);
			codonsNew += transcript.isStrandPlus() ? cnew : GprSeq.wc(cnew);
		}
	}

	void analyze(int i, int pos, String ref, String mnp) {
		String codons = codons();

		// Create a SeqChange
		int seqChangeStrand = +1;
		SeqChange seqChange = new SeqChange(chromosome, pos, ref + "", mnp + "", seqChangeStrand, "", 1.0, 1);

		//---
		// Calculate effects
		//---
		List<ChangeEffect> effects = snpEffectPredictor.seqChangeEffect(seqChange);

		// Show
		ChangeEffect effect = null;
		if (effects.size() > 1) { // Usually there is only one effect
			for (ChangeEffect ce : effects) {
				if ((ce.getEffectType() != EffectType.SPLICE_SITE_ACCEPTOR) //
						&& (ce.getEffectType() != EffectType.SPLICE_SITE_DONOR)) //
					effect = ce;
			}
		} else effect = effects.get(0);

		String effStr = effect.effect(true, true, true);

		if (codons.length() > 1) {
			String codonsExp[] = codons.split("/");

			boolean error = (!codonsExp[0].toUpperCase().equals(effect.getCodonsOld().toUpperCase()) || !codonsExp[1].toUpperCase().equals(effect.getCodonsNew().toUpperCase()));

			if (error || debug) Gpr.debug("Fatal error:"//
					+ "\n\tPos           : " + pos //
					+ "\n\tSeqChange     : " + seqChange + (seqChange.getStrand() >= 0 ? "+" : "-") //
					+ "\n\tCodon (exp)   : " + codons//
					+ "\n\tCodon (pred)  : " + effect.getCodonsOld().toUpperCase() + "/" + effect.getCodonsNew().toUpperCase() //
					+ "\n\tEffect (pred) : " + effStr //
					+ "\n\tGene          : " + gene//
					+ "\n\tChromo        : " + chromoSequence//
			);

			/**
			 * Error? Dump so we can debug...
			 */
			if (error) {
				System.err.println("Error. Dumping data");
				Save save = new Save();
				save.snpEffectPredictor = snpEffectPredictor;
				save.chromoSequence = chromoSequence;
				save.chromoNewSequence = chromoNewSequence;
				save.ref = ref;
				save.pos = pos;
				save.mnp = mnp;
				String outFile = "/tmp/sep_" + i + "_" + pos + ".bin";
				Gpr.toFileSerialize(outFile, save);
				throw new RuntimeException("Codons do not match!\n\tData dumped: '" + outFile + "'");
			}

			// Check warnings
			if (!effect.getWarning().isEmpty()) Gpr.debug("WARN:" + effect.getWarning() + "\t" + seqChange + "\t" + seqChangeStrand);
			Assert.assertEquals(true, effect.getWarning().isEmpty());
		}

	}

	String codons() {
		char seq[] = chromoSequence.toCharArray();
		char seqNew[] = chromoNewSequence.toCharArray();

		codonsOld = "";
		codonsNew = "";
		int codonIdx = 0;
		int i = 0;
		int step = transcript.isStrandPlus() ? 1 : -1;
		char codonOld[] = new char[3];
		char codonNew[] = new char[3];
		for (Exon ex : transcript.sortedStrand()) {
			int start = ex.isStrandPlus() ? ex.getStart() : ex.getEnd();
			for (i = start; ex.intersects(i); i += step, codonIdx = (codonIdx + 1) % 3) {
				codonOld[codonIdx] = seq[i];
				codonNew[codonIdx] = seqNew[i];
				if (codonIdx == 2) addIfDiff(codonOld, codonNew);
			}
		}

		for (; codonIdx != 0; i += step, codonIdx = (codonIdx + 1) % 3) {
			codonOld[codonIdx] = 'N';
			codonNew[codonIdx] = 'N';
			if (codonIdx == 2) addIfDiff(codonOld, codonNew);
		}

		return codonsOld + "/" + codonsNew;
	}

	/**
	 * Create a MNP
	 * @param pos
	 * @param mnpLen
	 */
	String createMnp(int pos, int mnpLen) {
		char chSeq[] = chromoSequence.toCharArray();
		char chSeqNew[] = chromoSequence.toCharArray();

		String mnp = "";
		for (int i = pos; i < (pos + mnpLen); i++) {
			chSeqNew[i] = snp(chSeq[i]);
			mnp += chSeqNew[i];
		}

		chromoNewSequence = new String(chSeqNew);
		return mnp;
	}

	void init() {
		initRand();
		initSnpEffPredictor();
	}

	void initRand() {
		rand = new Random(20111113);
		rand = new Random(20111222);
	}

	void initSnpEffPredictor() {
		// Create a config and force out snpPredictor for hg37 chromosome Y
		config = new Config("testCase", Config.DEFAULT_CONFIG_FILE);

		SnpEffPredictorFactoryRand sepf = new SnpEffPredictorFactoryRand(config, 1, rand, maxGeneLen, maxTranscripts, maxExons);

		// Create predictor
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
		codonTable = genome.codonTable();
		gene = genome.getGenes().iterator().next();
		transcript = gene.iterator().next();
	}

	/**
	 * Create a different base
	 * @param ref
	 * @return
	 */
	char snp(char ref) {
		char snp = ref;
		while (snp == ref) {
			snp = Character.toUpperCase(GprSeq.randBase(rand));
		}
		return snp;
	}

	public void test_02() {
		int N = 1000;

		// Test N times
		//	- Create a random gene transcript, exons
		//	- Change each base in the exon
		//	- Calculate effect
		for (int i = 0; i < N; i++) {
			initSnpEffPredictor();

			if (debug) System.out.println("MNP Test iteration: " + i + "\nChromo:\t" + chromoSequence + "\n" + transcript);
			else System.out.println("MNP Test iteration: " + i + "\t" + (transcript.getStrand() >= 0 ? "+" : "-") + "\t" + transcript.cds());

			if (debug) {
				for (Exon exon : transcript.sortedStrand())
					System.out.println("\tExon: " + exon + "\tSEQ:\t" + (exon.isStrandMinus() ? GprSeq.reverseWc(exon.getSequence()) : exon.getSequence()).toUpperCase());
			}

			// For each base in this exon...
			for (int pos = 0; pos < chromoSequence.length() - 2; pos++) {

				if ((i == 587) && (pos == 712)) //
					Gpr.debug("!!!");

				// MNP length
				int mnpLen = rand.nextInt(MAX_MNP_LEN) + 2;
				int maxMnpLen = chromoSequence.length() - pos;
				mnpLen = Math.min(mnpLen, maxMnpLen);

				String ref = chromoSequence.substring(pos, pos + mnpLen);
				String mnp = createMnp(pos, mnpLen);

				analyze(i, pos, ref, mnp);
			}
		}
	}
}
