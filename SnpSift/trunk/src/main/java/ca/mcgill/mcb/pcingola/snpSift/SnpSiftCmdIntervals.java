package ca.mcgill.mcb.pcingola.snpSift;

import java.util.LinkedList;
import java.util.List;

import ca.mcgill.mcb.pcingola.fileIterator.SeqChangeBedFileIterator;
import ca.mcgill.mcb.pcingola.fileIterator.VcfFileIterator;
import ca.mcgill.mcb.pcingola.interval.Genome;
import ca.mcgill.mcb.pcingola.interval.SeqChange;
import ca.mcgill.mcb.pcingola.interval.tree.IntervalForest;
import ca.mcgill.mcb.pcingola.util.Timer;
import ca.mcgill.mcb.pcingola.vcf.VcfEntry;

/**
 * Filter variants that hit intervals
 * 
 * @author pablocingolani
 */
public class SnpSiftCmdIntervals extends SnpSift {

	LinkedList<String> bedFiles;
	IntervalForest intervalForest;
	Genome genome;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SnpSiftCmdIntervals vcfIntervals = new SnpSiftCmdIntervals(args);
		vcfIntervals.run();
	}

	public SnpSiftCmdIntervals(String[] args) {
		super(args, "int");
		genome = new Genome("genome");
	}

	/**
	 * Initialize default values
	 */
	@Override
	public void init() {
		verbose = false;
	}

	/**
	 * Load all BED files
	 * @param bedFileNames
	 */
	void loadIntervals() {
		LinkedList<SeqChange> seqChangesAll = new LinkedList<SeqChange>();

		// Read filter interval files
		for (String bedFileName : bedFiles) {
			if (verbose) Timer.showStdErr("Reading filter interval file '" + bedFileName + "'");

			SeqChangeBedFileIterator bedFile = new SeqChangeBedFileIterator(bedFileName, genome, 0);
			bedFile.setCreateChromos(true);

			List<SeqChange> seqChanges = bedFile.load();
			seqChangesAll.addAll(seqChanges);
		}

		if (verbose) Timer.showStdErr("Total " + seqChangesAll.size() + " intervals added.");

		if (verbose) Timer.showStdErr("Building interval forest.");
		intervalForest = new IntervalForest(); // Filter only seqChanges that match these intervals
		intervalForest.add(seqChangesAll);
		intervalForest.build();
		if (verbose) Timer.showStdErr("Done.");
	}

	@Override
	public void parse(String[] args) {
		if (args.length <= 0) usage(null);

		bedFiles = new LinkedList<String>();
		for (int i = 0; i < args.length; i++) {
			// Argument starts with '-'?
			if (args[i].startsWith("-")) {
				if (args[i].equals("-h") || args[i].equalsIgnoreCase("-help")) usage(null);
				else if (args[i].equals("-v")) verbose = true;
				else if (args[i].equals("-q")) verbose = false;
			} else bedFiles.add(args[i]);
		}
	}

	/**
	 * Load a file compare calls
	 * 
	 * @param fileName
	 */
	@Override
	public void run() {
		loadIntervals();

		// Read all vcfEntries
		VcfFileIterator vcfFile = new VcfFileIterator("-");

		boolean showHeader = true;
		for (VcfEntry vcfEntry : vcfFile) {

			// Show header
			if (showHeader) {
				addHeader(vcfFile);
				if (!vcfFile.getHeader().isEmpty()) System.out.println(vcfFile.getHeader());
				showHeader = false;
			}

			// Does it intercept any interval?
			if (!intervalForest.query(vcfEntry).isEmpty()) System.out.println(vcfEntry);
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

		System.err.println("Usage: java -jar " + SnpSift.class.getSimpleName() + ".jar intervals file_1.bed file_2.bed ... file_N.bed");
		System.exit(1);
	}
}
