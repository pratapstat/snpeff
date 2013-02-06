package ca.mcgill.mcb.pcingola.snpSift;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import ca.mcgill.mcb.pcingola.fileIterator.LineFileIterator;
import ca.mcgill.mcb.pcingola.util.Gpr;
import ca.mcgill.mcb.pcingola.util.Timer;

/**
 * Split a large VCF file by chromosome
 * 
 * @author pablocingolani
 */
public class SnpSiftCmdSplitChr extends SnpSift {

	public static final int SHOW = 10000;
	public static final int SHOW_LINES = 100 * SHOW;
	public static String exts[] = { ".vcf", ".vcf.gz" };

	String vcfFile;
	StringBuilder header = new StringBuilder();

	public SnpSiftCmdSplitChr(String args[]) {
		super(args, "splitChr");
	}

	/**
	 * Create a new output file
	 * @param baseName
	 * @param chr
	 * @return
	 */
	BufferedWriter newFile(String baseName, String chr) {
		String outFileName = baseName + "." + chr + ".vcf";
		System.err.println("");
		Timer.showStdErr("Creating new file '" + outFileName + "'");

		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(outFileName));
			out.write(header.toString());
			return out;
		} catch (Exception e) {
			throw new RuntimeException("Error opening file '" + outFileName + "'");
		}
	}

	/**
	 * Parse command line arguments
	 */
	@Override
	public void parse(String[] args) {
		int argNum = 0;
		if (args.length == 0) usage(null);

		if (args.length >= argNum) vcfFile = args[argNum++];
		else usage("Missing 'file.vcf'");
	}

	/**
	 * Run
	 */
	@Override
	public void run() {
		if (verbose) Timer.showStdErr("Splitting file '" + vcfFile + "'");

		// Base file name
		String baseName = Gpr.removeExt(vcfFile, exts);

		// Init
		boolean isHeader = true;
		int lineNum = 1;
		String chrPrev = "";

		// Open file and iterate
		try {
			BufferedWriter out = null;
			LineFileIterator lfi = new LineFileIterator(vcfFile);
			for (String line : lfi) {
				// Are we in header section?
				if (isHeader) {
					if (line.startsWith("#")) {
						// Add to header
						header.append(line);
						header.append("\n");
					} else {
						// End of header
						isHeader = false;
					}
				}

				// Not in header?
				if (!isHeader) {
					String fields[] = line.split("\t", 2);
					String chr = fields[0];

					// New chromosome?
					if (!chr.equals(chrPrev)) {
						if (out != null) out.close();
						out = newFile(baseName, chr);
						chrPrev = chr;
					}

					// Write line
					out.write(line);
					out.write("\n");

					Gpr.showMark(lineNum, SHOW);
					lineNum++;
				}
			}

			out.close();
		} catch (IOException e) {
			throw new RuntimeException("Error writing data!");
		}

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

		System.err.println("Usage: java -jar " + SnpSift.class.getSimpleName() + ".jar splitChr [-v] file.vcf");
		System.exit(1);
	}
}
