package ca.mcgill.mcb.pcingola.snpSift;

import java.util.HashMap;
import java.util.List;

import ca.mcgill.mcb.pcingola.fileIterator.VcfFileIterator;
import ca.mcgill.mcb.pcingola.util.Timer;
import ca.mcgill.mcb.pcingola.vcf.VcfEntry;

/**
 * Annotate Loss of function (LOF) in a VCF file
 * 
 * @author pablocingolani
 */
public class SnpSiftCmdLof extends SnpSift {

	public static final int SHOW = 10000;
	public static final int SHOW_LINES = 100 * SHOW;

	String vcfFile;
	HashMap<String, String> db = new HashMap<String, String>();

	/**
	 * Main
	 * @param args
	 */
	public static void main(String[] args) {
		SnpSiftCmdLof varType = new SnpSiftCmdLof(args);
		varType.annotate();
	}

	public SnpSiftCmdLof(String args[]) {
		super(args, "lof");
	}

	@Override
	protected List<String> addHeader() {
		List<String> newHeaders = super.addHeader();
		newHeaders.add("##INFO=<ID=LOF,Number=0,Type=Flag,Description=\"Variant is a putative loss of function (LOF)\">");
		return newHeaders;
	}

	/**
	 * Annotate entries
	 */
	public void annotate() {
		if (verbose) Timer.showStdErr("Annotating loss of function (LOF) on entries from: '" + vcfFile + "'");

		VcfFileIterator vcf = new VcfFileIterator(vcfFile);

		boolean showHeader = true;
		for (VcfEntry vcfEntry : vcf) {
			// Show header?
			if (showHeader) {
				addHeader(vcf);
				String headerStr = vcf.getVcfHeader().toString();
				if (!headerStr.isEmpty()) System.out.println(headerStr);
				showHeader = false;
			}

			annotate(vcfEntry);
			System.out.println(vcfEntry);
		}
	}

	/**
	 * Annotate one entry
	 * @param vcfEntry
	 */
	public void annotate(VcfEntry vcfEntry) {
		throw new RuntimeException("UNIMPLEMENTED!!!");
	}

	/**
	 * Parse command line arguments
	 */
	@Override
	public void parse(String[] args) {
		int argNum = 0;
		if (args.length == 0) usage(null);

		if (args[argNum].equals("-v")) {
			verbose = true;
			argNum++;
		}

		if (args.length >= argNum) vcfFile = args[argNum++];
		else usage("Missing 'file.vcf'");
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

		System.err.println("Usage: java -jar " + SnpSift.class.getSimpleName() + ".jar lof [-v] file.eff.vcf > file.lof.vcf.");
		System.err.println("WARNING: file.eff.vcf has been previously annotated using SnpEff.");
		System.exit(1);
	}
}
