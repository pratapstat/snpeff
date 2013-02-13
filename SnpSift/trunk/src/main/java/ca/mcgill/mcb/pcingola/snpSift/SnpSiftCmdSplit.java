package ca.mcgill.mcb.pcingola.snpSift;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import ca.mcgill.mcb.pcingola.fileIterator.LineFileIterator;
import ca.mcgill.mcb.pcingola.util.Gpr;
import ca.mcgill.mcb.pcingola.util.Timer;

/**
 * Split a large VCF file by chromosome or bby number of lines
 * 
 * @author pablocingolani
 */
public class SnpSiftCmdSplit extends SnpSift {

	public static final int SHOW = 1000;
	public static final int SHOW_LINES = 100 * SHOW;
	public static String exts[] = { ".vcf", ".vcf.gz" };

	String vcfFile;
	StringBuilder header = new StringBuilder();
	int numLines;
	ArrayList<String> fileNames;

	public SnpSiftCmdSplit(String args[]) {
		super(args, "split");
		fileNames = new ArrayList<String>();
	}

	/**
	 * Names of files created when splitting
	 * @return
	 */
	public ArrayList<String> getFileNames() {
		return fileNames;
	}

	/**
	 * Create a new output file
	 * @param baseName
	 * @param chr
	 * @return
	 */
	BufferedWriter newFile(String baseName, String chr, int fileNumber) {
		// File name
		String outFileName = "";
		if (numLines <= 0) outFileName = baseName + "." + chr + ".vcf"; // Splitting by chromosome
		else outFileName = String.format("%s.%03d.vcf", baseName, fileNumber); // Splitting by number of lines

		// Update list of file names
		fileNames.add(outFileName);

		if (verbose) {
			System.err.println("");
			Timer.showStdErr("Creating new file '" + outFileName + "'");
		}

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
		numLines = -1;
		if (args.length == 0) usage(null);

		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-l")) {
				// Parse number of lines
				if ((i + 1) < args.length) {
					String numStr = args[++i];
					numLines = Gpr.parseIntSafe(numStr);
					if (numLines <= 0) usage("Number of lines must be a positive number. num = '" + numStr + "'");
				} else usage("Missing 'num' argument");
			} else if (vcfFile == null) vcfFile = args[i];
		}

		if (vcfFile == null) usage("Missing 'file.vcf'");
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
		int lineNum = 0, fileNum = 0;
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

					// New file? Split file?
					if ((out == null) // No file created?
							|| ((numLines > 0) && (numLines <= lineNum)) // Splitting by lines :  Number of lines reached?
							|| ((numLines <= 0) && (!chr.equals(chrPrev))) // Splitting by chromosome split: Is this a new chromosome? 
					) {
						// Split file
						if (out != null) out.close();
						out = newFile(baseName, chr, fileNum);
						fileNum++;
						chrPrev = chr;
						lineNum = 0;
					}

					// Write line
					out.write(line);
					out.write("\n");

					Gpr.showMark(lineNum, SHOW);
					lineNum++;
				}
			}

			if (out != null) out.close();
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
		System.err.println("Usage: java -jar " + SnpSift.class.getSimpleName() + ".jar splitChr [-v] [-l <num>]file.vcf");
		System.err.println("Options:");
		System.err.println("\t-l <num>   : Split by 'num' lines (instead of chromosomes).");
		System.exit(1);
	}
}
