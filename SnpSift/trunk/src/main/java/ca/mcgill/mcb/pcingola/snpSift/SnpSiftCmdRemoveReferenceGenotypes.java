package ca.mcgill.mcb.pcingola.snpSift;

import java.util.List;

import ca.mcgill.mcb.pcingola.fileIterator.VcfFileIterator;
import ca.mcgill.mcb.pcingola.interval.Genome;
import ca.mcgill.mcb.pcingola.util.Timer;
import ca.mcgill.mcb.pcingola.vcf.VcfEntry;
import ca.mcgill.mcb.pcingola.vcf.VcfGenotype;

/**
 * Removes reference genotypes.
 * I.e. replaces the genotype string by the MISSING string ('.') if the genotype is just homozygous reference (e.g. '0/0')
 * 
 * @author pablocingolani
 */
public class SnpSiftCmdRemoveReferenceGenotypes {

	public static final int SHOW_EVERY = 1000;
	public static final int SHOW_EVERY_NL = 100 * SHOW_EVERY;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SnpSiftCmdRemoveReferenceGenotypes vcfSnpCompare = new SnpSiftCmdRemoveReferenceGenotypes();
		vcfSnpCompare.run();
	}

	public SnpSiftCmdRemoveReferenceGenotypes() {
	}

	public SnpSiftCmdRemoveReferenceGenotypes(String[] args) {
		if (args.length != 0) usage(null);
	}

	/**
	 * Analyze the file
	 */
	void run() {
		Timer.showStdErr("Reading STDIN");
		Genome genome = new Genome("genome");
		VcfFileIterator vcfFile = new VcfFileIterator("-", genome);
		vcfFile.setCreateChromos(true); // Create chromosomes when needed

		// Read all vcfEntries
		int entryNum = 1;
		for (VcfEntry vcfEntry : vcfFile) {
			VcfGenotype nogenotype = null;

			// Show header?
			if (entryNum == 1) {
				if (!vcfFile.getHeader().isEmpty()) System.out.println(vcfFile.getHeader());
			}

			// Replace using 'nogenotype' if it is not a variant
			List<VcfGenotype> genotypes = vcfEntry.getVcfGenotypes();
			for (int i = 0; i < genotypes.size(); i++) {
				VcfGenotype genotype = genotypes.get(i);

				// Not a variant? => Replace
				if (!genotype.isVariant()) {
					if (nogenotype == null) nogenotype = new VcfGenotype(vcfEntry, vcfEntry.getFormat(), VcfFileIterator.MISSING);
					genotypes.set(i, nogenotype);
				}
			}

			// Show entry
			System.out.println(vcfEntry);

			// Show progress
			if (entryNum % SHOW_EVERY == 0) {
				if (entryNum % SHOW_EVERY_NL == 0) System.err.println('.');
				else System.err.print('.');
			}

			entryNum++;
		}

		Timer.showStdErr("Done");
	}

	/**
	 * Show usage and exit
	 */
	void usage(String errMsg) {
		if (errMsg != null) System.err.println("Error: " + errMsg);
		System.err.println("Usage: cat file.vcf | java -jar " + SnpSift.class.getSimpleName() + "" + ".jar rmRefGen > file_noref.vcf");
		System.exit(1);
	}
}
