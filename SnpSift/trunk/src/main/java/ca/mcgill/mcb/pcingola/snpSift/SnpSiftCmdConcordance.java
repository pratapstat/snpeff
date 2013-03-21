package ca.mcgill.mcb.pcingola.snpSift;

import ca.mcgill.mcb.pcingola.fileIterator.VcfFileIterator;
import ca.mcgill.mcb.pcingola.vcf.FileIndexChrPos;

/**
 * Calculate genotyping concordance between two VCF files.
 * 
 * Typical usage: Calculate concordance between sequencing experiment and genotypting experiment.
 *  
 * @author pcingola
 */
public class SnpSiftCmdConcordance extends SnpSift {

	protected FileIndexChrPos indexDb;
	String vcfFileName1, vcfFileName2;
	protected FileIndexChrPos indexVcf;

	public SnpSiftCmdConcordance(String args[]) {
		super(args, "concordance");
	}

	/**
	 * Parse command line arguments
	 */
	@Override
	public void parse(String[] args) {
		if (args.length == 0) usage(null);

		for (int i = 0; i < args.length; i++) {
			String arg = args[i];

			if (vcfFileName1 == null) vcfFileName1 = arg;
			else if (vcfFileName2 == null) vcfFileName2 = arg;
		}

		// Sanity check
		if (vcfFileName2 == null) usage("Missing vcf file");
	}

	@Override
	public void run() {
		// Index vcf1:  it is assumed to be the smaller of the two
		indexVcf = new FileIndexChrPos(vcfFileName1);
		indexVcf.index();

		// Open files
		VcfFileIterator vcf1 = new VcfFileIterator(vcfFileName1);
		VcfFileIterator vcf2 = new VcfFileIterator(vcfFileName2);

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
		System.err.println("Usage: java -jar " + SnpSift.class.getSimpleName() + ".jar " + command + " [options] genotype.vcf sequencing.vcf\n");
		System.exit(1);
	}

}
