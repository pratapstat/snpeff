package ca.mcgill.mcb.pcingola.snpSift.caseControl;

import java.util.List;

import ca.mcgill.mcb.pcingola.vcf.VcfEntry;
import ca.mcgill.mcb.pcingola.vcf.VcfGenotype;

/**
 * A counnter for genotypes
 * 
 * @author pcingola
 */
public class VariantCounter {

	int genotypes[];

	public VariantCounter(VcfEntry ve) {
		parseGenotypes(ve);
	}

	public int countNonZeroGenotypes() {
		int count = 0;
		for (int i = 0; i < genotypes.length; i++)
			if (genotypes[i] > 0) count++;
		return count;
	}

	public int getGenotype(int idx) {
		return genotypes[idx];
	}

	public void incGenotype(int idx) {
		genotypes[idx]++;
	}

	public void incGenotype(int idx, int value) {
		genotypes[idx] += value;
	}

	void parseGenotypes(VcfEntry ve) {
		List<VcfGenotype> vcfGenos = ve.getVcfGenotypes();
		genotypes = new int[vcfGenos.size()];

		// Populate array
		int i = 0;
		for (VcfGenotype vcfGeno : vcfGenos)
			genotypes[i++] = (byte) vcfGeno.getGenotypeCode();
	}

	public void setGenotype(int idx, int value) {
		genotypes[idx] = value;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(countNonZeroGenotypes());

		// Show genotypes
		for (int i = 0; i < genotypes.length; i++)
			sb.append("\t" + genotypes[i]);

		return sb.toString();
	}

}
