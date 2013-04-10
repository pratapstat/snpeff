package ca.mcgill.mcb.pcingola;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import ca.mcgill.mcb.pcingola.fileIterator.VcfFileIterator;
import ca.mcgill.mcb.pcingola.snpEffect.ChangeEffect;
import ca.mcgill.mcb.pcingola.snpSift.caseControl.VariantCounter;
import ca.mcgill.mcb.pcingola.util.Gpr;
import ca.mcgill.mcb.pcingola.util.Timer;
import ca.mcgill.mcb.pcingola.vcf.VcfEffect;
import ca.mcgill.mcb.pcingola.vcf.VcfEntry;
import ca.mcgill.mcb.pcingola.vcf.VcfGenotype;

public class ZzzAffectedSamples {

	public static final String VCF_INFO_FREQ_TYPE = "FREQ";
	public static final String VCF_INFO_FREQ_SINGLETON = "Singleton";

	HashMap<String, VariantCounter> varCounters;
	List<String> sampleNames;

	/**
	 * Main
	 * @param args
	 */
	public static void main(String[] args) {
		String vcfFileName = Gpr.HOME + "/fly_pvuseq/dnaSeq.eff.TET_EL.vcf";

		ZzzAffectedSamples zzz = new ZzzAffectedSamples();
		zzz.run(vcfFileName);
		zzz.showResults();
	}

	public ZzzAffectedSamples() {
		varCounters = new HashMap<String, VariantCounter>();
	}

	/**
	 * Get a VarCounter for gene 'geneName'
	 * @param geneName
	 * @param ve
	 * @return
	 */
	VariantCounter getVarCounter(String geneName, VcfEntry ve) {
		VariantCounter varCounter = varCounters.get(geneName);
		if (varCounter == null) {
			varCounter = new VariantCounter(ve);
			varCounters.put(geneName, varCounter);
		}
		return varCounter;

	}

	public void run(String vcfFileName) {
		Timer.showStdErr("Reading file " + vcfFileName);

		for (VcfEntry ve : new VcfFileIterator(vcfFileName)) {
			// Set sample names
			if (sampleNames == null) sampleNames = ve.getVcfFileIterator().getVcfHeader().getSampleNames();

			// Only singletons
			if (ve.isSingleton()) {

				// Genes counted in this entry
				HashSet<String> genesCounted = new HashSet<String>();

				for (VcfEffect veff : ve.parseEffects()) {
					if ((veff.getImpact() == ChangeEffect.EffectImpact.HIGH) || (veff.getImpact() == ChangeEffect.EffectImpact.MODERATE)) {
						// if (veff.getImpact() == ChangeEffect.EffectImpact.HIGH) {
						String geneName = ve.getChromosomeName() + "\t" + veff.getGene();

						// Do not count twice
						if (!genesCounted.contains(geneName)) {
							genesCounted.add(geneName);

							// Get counter for this gene
							VariantCounter varCounter = getVarCounter(geneName, ve);

							// Increment all genotypes that have a variant
							int idx = 0;
							for (VcfGenotype gen : ve) {
								if (gen.isVariant()) varCounter.incGenotype(idx);
								idx++;
							}
						}
					}
				}
			}
		}

		Timer.showStdErr("Done");
	}

	/**
	 * Show final results
	 */
	void showResults() {
		ArrayList<String> genes = new ArrayList<String>();
		genes.addAll(varCounters.keySet());
		Collections.sort(genes);

		// Show title
		System.out.print("Chromosome\tGeneName\tAffected samples");
		for (String sample : sampleNames)
			System.out.print("\t" + sample);
		System.out.println("");

		// Show info
		for (String gene : genes) {
			VariantCounter vc = varCounters.get(gene);
			System.out.println(gene + "\t" + vc);
		}
	}
}
