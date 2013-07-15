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
	 * Process a VCF entry and return a string (tab separated values)
	 * @param vcfEntry
	 * @return
	 */
	public int processStr(VcfEntry vcfEntry, StringBuilder sbcodes) {
		// Add all genotype codes
		String sep = "";
		int countNonRef = 0;
		for (VcfGenotype gen : vcfEntry.getVcfGenotypes()) {
			int score = gen.getGenotypeCode();

			String sc = ".";
			if (score >= 0) {
				sc = Integer.toString(score);
				if (score > 0) countNonRef++;
			}

			sbcodes.append(sep + sc);
			sep = SEPARATOR;
		}

		return countNonRef;
	}

	/**
	 * Process the whole VCF file
	 */
	@Override
	public void run() {
		if (verbose) Timer.showStdErr("Processing file '" + vcfFile + "'");

		int i = 1;
		VcfFileIterator vcf = new VcfFileIterator(vcfFile);
		for (VcfEntry ve : vcf) {
			if (vcf.isHeadeSection()) {
				System.out.print("#CHROM\tPOS\tREF\tALT");
				for (String sample : vcf.getVcfHeader().getSampleNames())
					System.out.print("\t" + sample);
				System.out.println("");
			}

			StringBuilder sbcodes = new StringBuilder();
			processStr(ve, sbcodes);

			System.out.println(ve.getChromosomeName() //
					+ "\t" + (ve.getStart() + 1) //
					//	+ "\t" + ve.getId() //
					+ "\t" + ve.getRef() //
					+ "\t" + ve.getAltsStr() //
					//	+ "\t" + ve.getQuality() //
					//	+ "\t" + ve.getInfoStr() //
					+ "\t" + sbcodes.toString() //
			);
			if (verbose) Gpr.showMark(i++, SHOW_EVERY);
		}

		if (verbose) Timer.showStdErr("Done");
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
