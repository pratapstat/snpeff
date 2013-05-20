package ca.mcgill.mcb.pcingola.snpSift.matrix;

import java.util.List;

import ca.mcgill.mcb.pcingola.fileIterator.VcfFileIterator;
import ca.mcgill.mcb.pcingola.snpSift.SnpSift;
import ca.mcgill.mcb.pcingola.util.Gpr;
import ca.mcgill.mcb.pcingola.vcf.VcfEntry;
import ca.mcgill.mcb.pcingola.vcf.VcfGenotype;

/**
 * Add genotype information to INFO fields
 * 
 * @author pcingola
 */
public class SnpSiftCmdGt extends SnpSift {

	public static final String VCF_INFO_HOMS = "HO";
	public static final String VCF_INFO_HETS = "HE";
	public static final String VCF_INFO_NAS = "NA";

	public static int SHOW_EVERY = 100;
	String vcfFile;

	public SnpSiftCmdGt() {
		super(null, null);
	}

	public SnpSiftCmdGt(String[] args) {
		super(args, "gt");
	}

	@Override
	protected List<String> addHeader() {
		List<String> newHeaders = super.addHeader();
		newHeaders.add("##INFO=<ID=" + VCF_INFO_HOMS + ",Number=.,Type=Integer,Description=\"List of sample indexes having homozygous ALT genotypes\">");
		newHeaders.add("##INFO=<ID=" + VCF_INFO_HETS + ",Number=.,Type=Integer,Description=\"List of sample indexes having heterozygous ALT genotypes\">");
		newHeaders.add("##INFO=<ID=" + VCF_INFO_NAS + ",Number=.,Type=Integer,Description=\"List of sample indexes having missing genotypes\">");
		newHeaders.add("##SnpSiftCmd=\"" + commandLineStr() + "\"");
		return newHeaders;
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
	public boolean processEntry(VcfEntry vcfEntry) {
		if (vcfEntry.getAlts().length > 1) return false;

		StringBuilder homs = new StringBuilder();
		StringBuilder hets = new StringBuilder();
		StringBuilder nas = new StringBuilder();

		// Add all genotype codes
		int idx = 0;
		for (VcfGenotype gen : vcfEntry.getVcfGenotypes()) {
			int score = gen.getGenotypeCode();

			if (score == 0) {
				; //Nothing to do
			} else if (score < 0) nas.append((nas.length() > 0 ? "," : "") + idx);
			else if (score == 1) hets.append((hets.length() > 0 ? "," : "") + idx);
			else if (score == 2) homs.append((homs.length() > 0 ? "," : "") + idx);
			else {
				Gpr.debug("SCORE: " + score + "\t" + gen);
				return false;
			}

			idx++;
		}

		// Update INFO fields
		if (homs.length() > 0) vcfEntry.addInfo(VCF_INFO_HOMS, homs.toString());
		if (hets.length() > 0) vcfEntry.addInfo(VCF_INFO_HETS, hets.toString());
		if (nas.length() > 0) vcfEntry.addInfo(VCF_INFO_NAS, nas.toString());

		return true;
	}

	/**
	 * Process a VCF entry and return a string (tab separated values)
	 * @param vcfEntry
	 * @return
	 */
	@Override
	public void run() {
		VcfFileIterator vcf = new VcfFileIterator(vcfFile);

		int i = 1;
		for (VcfEntry ve : vcf) {
			if (vcf.isHeadeSection()) handleVcfHeader(vcf);

			if (processEntry(ve)) System.out.println(ve.toStringNoGt());
			else System.out.println(ve.toString());

			if (verbose) Gpr.showMark(i++, SHOW_EVERY);
		}
	}

	@Override
	public void usage(String msg) {
		if (msg != null) {
			System.err.println("Error: " + msg);
			showCmd();
		}

		showVersion();

		System.err.println("Usage: java -jar " + SnpSift.class.getSimpleName() + ".jar gt file.vcf > file.gt.vcf");
		System.exit(1);
	}

}
