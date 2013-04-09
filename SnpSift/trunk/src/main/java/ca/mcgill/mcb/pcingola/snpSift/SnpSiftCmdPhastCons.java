package ca.mcgill.mcb.pcingola.snpSift;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ca.mcgill.mcb.pcingola.fileIterator.LineFileIterator;
import ca.mcgill.mcb.pcingola.fileIterator.VcfFileIterator;
import ca.mcgill.mcb.pcingola.interval.Chromosome;
import ca.mcgill.mcb.pcingola.util.Gpr;
import ca.mcgill.mcb.pcingola.util.Timer;
import ca.mcgill.mcb.pcingola.vcf.VcfEntry;

/**
 * Annotate using PhastCons score files
 * 
 * @author pcingola
 */
public class SnpSiftCmdPhastCons extends SnpSift {

	// Wig fields
	public static final String START_FIELD = "start=";
	public static final String FIXED_STEP_FIELD = "fixedStep";

	// VCF INFO FILED
	public static final String VCF_INFO_PHASTCONS_FIELD = "PhastCons";

	public static final int SHOW_EVERY = 10;

	String phastConsDir;
	String vcfFile;
	HashMap<String, Integer> chromoSize;
	short score[];

	public SnpSiftCmdPhastCons(String[] args) {
		super(args, "phastCons");
	}

	/**
	 * Annotate VcfEntry 
	 * @param ve
	 */
	void annotate(VcfEntry ve) {
		float score = score(ve);
		if (score > 0.0f) ve.addInfo(VCF_INFO_PHASTCONS_FIELD, String.format("%.3f", score));
	}

	/**
	 * Get chromosome size
	 * @param chromo
	 * @return
	 */
	int chromoSize(String chromo) {
		Integer len = chromoSize.get(Chromosome.simpleName(chromo));
		return len == null ? 0 : len;
	}

	/**
	 * Find any file matching a regular expression
	 * @param regex
	 * @return
	 */
	String findPhastConsFile(String dirName, String regex) {
		try {
			File dir = new File(dirName);
			for (File f : dir.listFiles()) {
				String fname = f.getCanonicalPath();
				if (fname.matches(regex)) return fname;
			}
		} catch (IOException e) {
			// Do nothing
		}
		return null;
	}

	/**
	 * Load a phastcons file for this chromosome
	 * @param chromo
	 * @return
	 */
	boolean loadChromo(String chromo, VcfEntry ve) {
		chromo = Chromosome.simpleName(chromo);
		score = null;

		// Find a file that matches a phastCons name
		String wigFile = findPhastConsFile(phastConsDir, ".*/chr" + chromo + ".phastCons.\\d+way.wigFix.*");
		if ((wigFile == null) || !Gpr.exists(wigFile)) {
			Timer.showStdErr("Cannot open PhastCons file '" + wigFile + "' for chromosome '" + chromo + "'\n\tVcfEntry:\t" + ve);
			return false;
		}

		if (verbose) Timer.showStdErr("Loading phastCons data for chromosome '" + chromo + "', file '" + wigFile + "'");

		// Initialize
		int chrSize = chromoSize(chromo) + 1;
		score = new short[chrSize];
		for (int i = 0; i < score.length; i++)
			score[i] = 0;

		//---
		// Read file
		//---
		LineFileIterator lfi = new LineFileIterator(wigFile);
		int index = 0, countHeaders = 1;
		for (String line : lfi) {
			if (line.startsWith(FIXED_STEP_FIELD)) {
				String fields[] = line.split("\\s+");
				for (String f : fields) {
					if (f.startsWith(START_FIELD)) {
						String value = f.substring(START_FIELD.length());
						index = Gpr.parseIntSafe(value) - 1; // Wig files coordinates are 1-based. Reference http://genome.ucsc.edu/goldenPath/help/wiggle.html
						if (verbose) Gpr.showMark(countHeaders++, SHOW_EVERY);
					}
				}
			} else if (index >= score.length) {
				// Out of chromosome?
				Timer.showStdErr("PhastCons index out of chromosome boundaries.\n\tIndex: " + index + "\n\tChromosome length: " + score.length);
				break;
			} else {
				score[index] = (short) (Gpr.parseFloatSafe(line) * 1000);
				index++;
			}
		}

		// Show message
		if (verbose) {
			int countNonZero = 0;
			for (int i = 0; i < score.length; i++)
				if (score[i] != 0) countNonZero++;

			double perc = (100.0 * countNonZero) / score.length;
			System.err.println("");
			Timer.showStdErr(String.format("Total non-zero scores: %d / %d [%.2f%%]", countNonZero, score.length, perc));
		}

		return index > 0;
	}

	/**
	 * Load chromosome length file. 
	 * This is a fasta index file created by "samtools faidx" command.
	 * 
	 * @return
	 */
	void loadFaidx() {
		String file = phastConsDir + "/genome.fai";
		if (!Gpr.exists(file)) { throw new RuntimeException("Cannot fins fasta index file '" + file + "'\n\tYOu can create one by running 'samtools faidx' command and copying the resulting index file to " + file + "\n\n"); }

		// Read and parse file
		chromoSize = new HashMap<String, Integer>();
		String txt = Gpr.readFile(file);
		for (String line : txt.split("\n")) {
			String fields[] = line.split("\\s+");
			String chrName = Chromosome.simpleName(fields[0]);
			int len = Gpr.parseIntSafe(fields[1]);

			chromoSize.put(chrName, len);
		}
	}

	@Override
	public void parse(String[] args) {
		if (args.length != 2) usage(null);
		phastConsDir = args[0];
		vcfFile = args[1];
	}

	/**
	 * Run
	 */
	@Override
	public void run() {
		run(false);
	}

	/**
	 * Run annotations
	 * 
	 * @param createList : If true, create a list of VcfEntries (used for test cases)
	 * 
	 * @return
	 */
	public List<VcfEntry> run(boolean createList) {
		// Load chromosome lengths
		loadFaidx();

		// Iterate over file
		ArrayList<VcfEntry> list = new ArrayList<VcfEntry>();
		VcfFileIterator vcf = new VcfFileIterator(vcfFile);
		String chrPrev = "";
		for (VcfEntry ve : vcf) {
			if (vcf.isHeadeSection()) {
				// Add header line
				vcf.getVcfHeader().addLine("##INFO=<ID=" + VCF_INFO_PHASTCONS_FIELD + ",Number=1,Type=Float,Description=\"PhastCons conservation score\">");

				// Show header
				if (!createList) System.out.println(vcf.getVcfHeader());
			}

			// Do we need to load a database?
			if (!chrPrev.equals(ve.getChromosomeName())) {
				chrPrev = ve.getChromosomeName();
				loadChromo(chrPrev, ve);
			}

			// Annotate entry
			annotate(ve);

			// Show or add to list
			if (createList) list.add(ve);
			else System.out.println(ve);
		}

		return list;
	}

	/**
	 * Score for this entry
	 * @param ve
	 * @return
	 */
	float score(VcfEntry ve) {
		int pos = ve.getEnd();
		if ((score == null) || (pos >= score.length)) return 0.0f;

		// Is this a SNP? i.e. only one base
		if (ve.size() == 1) return score[ve.getStart()] / 1000.0f;

		// More then one base length? 
		// Return the average score of all those bases
		int sum = 0;
		for (int p = ve.getStart(); p <= ve.getEnd(); p++)
			sum += score[p];

		return sum / (1000.0f * ve.size());
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
		System.err.println("Usage: java -jar " + SnpSift.class.getSimpleName() + ".jar path/to/phastCons/dir file.vcf");
		System.exit(1);
	}

}
