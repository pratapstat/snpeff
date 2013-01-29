package ca.mcgill.mcb.pcingola.snpSift.matrix;

import ca.mcgill.mcb.pcingola.fileIterator.VcfFileIterator;
import ca.mcgill.mcb.pcingola.snpSift.SnpSift;
import ca.mcgill.mcb.pcingola.util.Gpr;
import ca.mcgill.mcb.pcingola.util.Timer;
import ca.mcgill.mcb.pcingola.vcf.VcfEntry;
import ca.mcgill.mcb.pcingola.vcf.VcfGenotype;

/**
 * Convert VCf file to allele matrix
 * 
 * Note: Only use SNPs
 * 
 * Note: Only variants with two possible alleles. I.e. the matrix has three possible values in each cell:
 * 		- 0, for allele 0/0
 * 		- 1, for allele 0/1 or 1/0
 * 		- 2, for allele 1/1
 * 
 * @author pcingola
 */
public class SnpSiftCmdAlleleMatrix extends SnpSift {

	public static String SEPARATOR = "";
	public static int SHOW_EVERY = 1000;
	String vcfFile;

	public SnpSiftCmdAlleleMatrix() {
		super(null, null);
	}

	public SnpSiftCmdAlleleMatrix(String[] args) {
		super(args, "alleleMatrix");
	}

	/**
	 * Parse command line arguments
	 */
	@Override
	public void parse(String[] args) {
		if (args.length != 1) usage(null);
		vcfFile = args[0];
	}

	/**
	 * Process a VCF entry
	 * @param vcfEntry
	 * @return null if the entry is not suitable
	 */
	public int[] processInt(VcfEntry vcfEntry) {
		if (!vcfEntry.isSnp()) return null;

		// Init
		int i = 0;
		int scores[] = new int[vcfEntry.getVcfGenotypes().size()];

		// For every sample 
		for (VcfGenotype vcfGen : vcfEntry.getVcfGenotypes()) {
			int gens[] = vcfGen.getGenotype();

			if (gens == null) return null; // Missing all genotypes? Do not show
			else if (gens.length != 2) return null; // More (or less) than two genotypes?
			else {
				for (int g : gens)
					if ((g < 0) || (g > 1)) return null; // Missing one genotype? Do not show
			}

			// Calculate score
			int genScore = gens[0] + gens[1];

			// Sanity check
			if ((genScore < 0) || (genScore > 2)) throw new RuntimeException("Fatal error: Out of range. genCode=" + genScore + "\t" + vcfGen + "\n\t" + vcfEntry);

			scores[i++] = genScore;
		}

		return scores;
	}

	/**
	 * Process a VCF entry and return a string (tab separated values)
	 * @param vcfEntry
	 * @return
	 */
	public String processStr(VcfEntry vcfEntry) {
		int scores[] = processInt(vcfEntry);

		if (scores == null) return null;

		StringBuilder sb = new StringBuilder();
		String sep = "";
		for (int sc : scores) {
			sb.append(sep + sc);
			sep = SEPARATOR;
		}
		return sb.toString();
	}

	/**
	 * Process the whole VCF file
	 */
	@Override
	public void run() {
		Timer.showStdErr("Processing file '" + vcfFile + "'");

		int i = 1;
		VcfFileIterator vcf = new VcfFileIterator(vcfFile);
		for (VcfEntry ve : vcf) {
			String out = processStr(ve);
			if (out != null) {
				System.out.println(ve.getChromosomeName() //
						+ "\t" + (ve.getStart() + 1) //
						+ "\t" + ve.getId() //
						+ "\t" + ve.getRef() //
						+ "\t" + ve.getAltsStr() //
						+ "\t" + out //
				);
			}
			Gpr.showMark(i++, SHOW_EVERY);
		}

		Timer.showStdErr("Done");
	}

	/**
	 * Show usage message
	 * @param msg
	 */
	@Override
	public void usage(String msg) {
		if (msg != null) {
			System.err.println("Error: " + msg);
			showCmd();
		}

		showVersion();

		System.err.println("Usage: java -jar " + SnpSift.class.getSimpleName() + ".jar alleleMat file.vcf > allele.matrix.txt");
		System.exit(1);
	}

}
