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
	boolean uncompress;
	boolean save; // Save output to string buffer instead of printing it to STDOUT (used for test cases)
	StringBuilder output = new StringBuilder();

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
	 * Process a VCF entry and return a string (tab separated values)
	 * @param vcfEntry
	 * @return
	 */
	public boolean compressEntry(VcfEntry vcfEntry) {
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

	public String getOutput() {
		return output.toString();
	}

	/**
	 * Parse command line arguments
	 */
	@Override
	public void parse(String[] args) {
		if (args.length == 0) usage(null);

		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			if (isOpt(arg)) {
				if (arg.equals("-u")) uncompress = true;
				else usage("Unknown option '" + arg + "'");
			} else if (vcfFile == null) vcfFile = args[i];
		}
	}

	/**
	 * Parse genotype string (sparse matrix) and set all entries using 'value'
	 * 
	 * @param str
	 * @param gt
	 * @param value
	 */
	void parseSparseGt(String str, byte gt[], int valueInt) {
		if (str == null) return;

		// Split comma separated indeces
		String idxs[] = str.split(",");
		byte value = (byte) valueInt;

		// Set all entries
		for (String idx : idxs) {
			int i = Gpr.parseIntSafe(idx);
			gt[i] = value;
		}

	}

	void print(String out) {
		if (save) output.append(out + "\n");
		else System.out.println(out);
	}

	/**
	 * Process a VCF entry and return a string (tab separated values)
	 * @param vcfEntry
	 * @return
	 */
	@Override
	public void run() {
		VcfFileIterator vcf = new VcfFileIterator(vcfFile);
		if (save) showHeader = false; // No need to show header

		int i = 1, numSamples = 0;
		for (VcfEntry ve : vcf) {
			if (vcf.isHeadeSection()) {
				handleVcfHeader(vcf);
				numSamples = vcf.getVcfHeader().getSampleNames().size();

				if (save) print(vcf.getVcfHeader().toString()); // Save header to output buffer
			}

			if (uncompress) {
				// Uncompress
				print(uncompressEntry(ve, numSamples));
			} else {
				// Compress
				if (compressEntry(ve)) print(ve.toStringNoGt());
				else print(ve.toString());
			}

			if (verbose) Gpr.showMark(i++, SHOW_EVERY);
		}
	}

	public void setSave(boolean save) {
		this.save = save;
	}

	public String uncompressEntry(VcfEntry vcfEntry, int numSamples) {
		// Get 'sparse' matrix entries
		String hoStr = vcfEntry.getInfo(VCF_INFO_HOMS);
		String heStr = vcfEntry.getInfo(VCF_INFO_HETS);
		String naStr = vcfEntry.getInfo(VCF_INFO_NAS);

		// Has genotype field? No compression
		if (vcfEntry.hasGenotypes()) return vcfEntry.toString();

		// Parse 'sparse' entries
		byte gt[] = new byte[numSamples];
		parseSparseGt(naStr, gt, -1);
		parseSparseGt(heStr, gt, 1);
		parseSparseGt(hoStr, gt, 2);

		// Remove info fields
		if (hoStr != null) vcfEntry.rmInfo(VCF_INFO_HOMS);
		if (heStr != null) vcfEntry.rmInfo(VCF_INFO_HETS);
		if (naStr != null) vcfEntry.rmInfo(VCF_INFO_NAS);
		vcfEntry.setFormat("GT");

		// Create output string 
		StringBuilder out = new StringBuilder();
		out.append(vcfEntry.toStringNoGt());
		out.append("\t" + vcfEntry.getFormat());

		for (int i = 0; i < gt.length; i++)
			switch (gt[i]) {
			case -1:
				out.append("\t./.");
				break;

			case 0:
				out.append("\t0/0");
				break;

			case 1:
				out.append("\t0/1");
				break;

			case 2:
				out.append("\t1/1");
				break;

			default:
				throw new RuntimeException("Unknown code '" + gt[i] + "'");
			}
		return out.toString();
	}

	@Override
	public void usage(String msg) {
		if (msg != null) {
			System.err.println("Error: " + msg);
			showCmd();
		}

		showVersion();

		System.err.println("Usage: java -jar " + SnpSift.class.getSimpleName() + ".jar gt [options] file.vcf > file.gt.vcf");
		System.err.println("Options: ");
		System.err.println("\t-u   : Uncompress (restore genotype fields).");
		System.exit(1);
	}

}
