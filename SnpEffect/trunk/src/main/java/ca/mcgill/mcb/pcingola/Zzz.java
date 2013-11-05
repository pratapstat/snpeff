package ca.mcgill.mcb.pcingola;

import java.util.List;

import ca.mcgill.mcb.pcingola.fileIterator.VcfFileIterator;
import ca.mcgill.mcb.pcingola.interval.Gene;
import ca.mcgill.mcb.pcingola.interval.SeqChange;
import ca.mcgill.mcb.pcingola.interval.Transcript;
import ca.mcgill.mcb.pcingola.snpEffect.ChangeEffect;
import ca.mcgill.mcb.pcingola.snpEffect.Config;
import ca.mcgill.mcb.pcingola.snpEffect.SnpEffectPredictor;
import ca.mcgill.mcb.pcingola.vcf.VcfEntry;

public class Zzz {

	public static void main(String[] args) {
		String genome = "testLukas";
		//		genome = "testHg3770Chr22";
		String vcfFileName = null; // Gpr.HOME + "/snpEff/testLukas.1.vcf";

		// Load config file
		System.out.println("Loading config file");
		String configFile = Config.DEFAULT_CONFIG_FILE;
		Config config = new Config(genome, configFile);

		// Load database
		System.out.println("Loading database");
		SnpEffectPredictor snpEffectPredictor = config.loadSnpEffectPredictor();

		// Build data structures (interval forest)
		System.out.println("Building forest");
		snpEffectPredictor.setUpDownStreamLength(0);
		snpEffectPredictor.buildForest();

		for (Gene gene : snpEffectPredictor.getGenome().getGenes()) {
			System.out.println(gene.getGeneName());
			for (Transcript tr : gene) {
				System.out.println(tr);
				System.out.println("CDS: " + tr.cds());
				System.out.println("CDS: " + tr.zzz());
			}
		}

		// Parse VCF?
		if (Math.random() > 2) {
			// Iterate on VCF
			System.out.println("Reading VCF file");
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
						System.out.println("\t" + changeEffect);
				}
			}
		}
	}
}
