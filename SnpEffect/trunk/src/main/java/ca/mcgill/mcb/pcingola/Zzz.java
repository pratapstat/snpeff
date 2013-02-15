package ca.mcgill.mcb.pcingola;

import java.util.Random;

import ca.mcgill.mcb.pcingola.interval.Exon;
import ca.mcgill.mcb.pcingola.interval.Gene;
import ca.mcgill.mcb.pcingola.interval.Genome;
import ca.mcgill.mcb.pcingola.interval.SeqChange;
import ca.mcgill.mcb.pcingola.interval.Transcript;
import ca.mcgill.mcb.pcingola.snpEffect.Config;
import ca.mcgill.mcb.pcingola.snpEffect.SnpEffectPredictor;
import ca.mcgill.mcb.pcingola.util.GprSeq;
import ca.mcgill.mcb.pcingola.util.Timer;

/**
 * Simple test program
 * @author pcingola
 */
public class Zzz {

	public static void main(String[] args) {
		Config config = new Config("testHg3765Chr22");
		Timer.show("Loading predictor");
		SnpEffectPredictor snpEffectPredictor = config.loadSnpEffectPredictor();
		Timer.show("Done");

		Random random = new Random(20130214);

		Genome genome = snpEffectPredictor.getGenome();
		for (Gene g : genome.getGenes()) {
			if (g.isProteinCoding()) {
				System.out.println(g.getGeneName());

				for (Transcript t : g) {
					System.out.println("\t" + t.getId());

					for (Exon ex : t) {
						// Positive strand sequence
						String seq = ex.getSequence();
						seq = ex.isStrandPlus() ? seq : GprSeq.reverseWc(seq);

						// Skip exon it too long
						if (ex.size() > 1000) continue;

						// Skip some exons (otherwise test is too long)
						if (random.nextInt(10) > 1) continue;

						System.out.println("\t\t" + ex.getId() + "\tStrand: " + ex.getStrand() + "\tSize: " + ex.size());

						// Change each base
						for (int i = ex.getStart(), idx = 0; i < ex.getEnd(); i++, idx++) {
							// Create a fake SNP. Random REF and ALT bases
							char ref = seq.charAt(idx);
							char alt;
							do {
								alt = GprSeq.randBase(random);
							} while (ref == alt);

							// Resulting sequence
							String altStr = alt + "";
							String newSeq = seq.substring(0, idx) + altStr + seq.substring(idx + 1);
							newSeq = ex.isStrandPlus() ? newSeq : GprSeq.reverseWc(newSeq);
							newSeq = newSeq.toLowerCase();

							SeqChange seqChange = new SeqChange(t.getChromosome(), i, ref + "", alt + "", 1, "", -1, -1);
							// System.out.println("\t\t\t" + seqChange);

							Exon exNew = ex.apply(seqChange);
							//							System.out.println("\t\t\t\tOriginal : " + ex);
							//							System.out.println("\t\t\t\tNew      : " + exNew);

							if (!exNew.getSequence().equals(newSeq)) throw new RuntimeException("Error:" //
									+ "\n\t\tSeqChange : " + seqChange //
									+ "\n\t\tOriginal  : " + ex //
									+ "\n\t\tNew       : " + exNew //
									+ "\n\t\tNew seq   : " + newSeq //
							);
						}
					}
				}
			}
		}
	}
}
