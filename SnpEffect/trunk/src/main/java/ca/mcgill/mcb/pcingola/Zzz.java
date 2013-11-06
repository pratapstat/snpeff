package ca.mcgill.mcb.pcingola;

import java.util.List;

import ca.mcgill.mcb.pcingola.fileIterator.VcfFileIterator;
import ca.mcgill.mcb.pcingola.interval.Gene;
import ca.mcgill.mcb.pcingola.interval.SeqChange;
import ca.mcgill.mcb.pcingola.interval.Transcript;
import ca.mcgill.mcb.pcingola.snpEffect.ChangeEffect;
import ca.mcgill.mcb.pcingola.snpEffect.Config;
import ca.mcgill.mcb.pcingola.snpEffect.SnpEffectPredictor;
import ca.mcgill.mcb.pcingola.util.Gpr;
import ca.mcgill.mcb.pcingola.vcf.VcfEntry;

public class Zzz {

	public static void main(String[] args) {
		boolean doCorrect = true;
		String genome = "testLukas";
		String vcfFileName = Gpr.HOME + "/snpEff/testLukas.1.vcf";

		// Load config file
		System.out.println("Loading config file");
		String configFile = Config.DEFAULT_CONFIG_FILE;
		Config config = new Config(genome, configFile);

		// Load database
		System.out.println("Loading database");
		SnpEffectPredictor snpEffectPredictor = config.loadSnpEffectPredictor();

		// Find transcript
		for (Gene gene : snpEffectPredictor.getGenome().getGenes()) {
			System.out.println(gene.getGeneName());
			for (Transcript tr : gene) {

				System.out.println("Before:\n" + tr);
				System.out.println("CDS: " + tr.cds());

				if (doCorrect) {
					tr.frameCorrection();
					tr.resetCdsCache();
					tr.adjust();
					System.out.println("After: \n" + tr);
					System.out.println("CDS: " + tr.cds());
				}

				System.out.println("\n\n");

				System.out.println("Coding : " + tr.isProteinCoding());
				System.out.println("BioType : " + tr.getBioType());
			}
		}

		// Build data structures (interval forest)
		System.out.println("Building forest");
		snpEffectPredictor.setUpDownStreamLength(0);
		snpEffectPredictor.buildForest();

		// Force protein coding
		config.setTreatAllAsProteinCoding(true);

		// Parse VCF?
		if (Math.random() > 0) {
			// Iterate on VCF
			System.out.println("Reading VCF file: " + vcfFileName);
			VcfFileIterator vcf = new VcfFileIterator(vcfFileName);
			for (VcfEntry vcfEntry : vcf) {
				// Show line
				System.out.println(vcfEntry.toStringNoGt());

				// Iterate on each 'sequence change' for each VCF entry
				for (SeqChange seqChange : vcfEntry.seqChanges()) {
					// Calculate effects
					List<ChangeEffect> effects = snpEffectPredictor.seqChangeEffect(seqChange);

					// Show all effects
					for (ChangeEffect changeEffect : effects)
						System.out.println("\t" + changeEffect.toStringSimple(true) //
								+ "\n\t" + changeEffect.toStringSimple(false) //
								+ "\n\t" + changeEffect.toString(false, false) //
						);
				}
			}
		}
	}
}
