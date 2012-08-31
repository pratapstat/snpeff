package ca.mcgill.mcb.pcingola.snpSift;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ca.mcgill.mcb.pcingola.fileIterator.SeqChangeBedFileIterator;
import ca.mcgill.mcb.pcingola.fileIterator.VcfFileIterator;
import ca.mcgill.mcb.pcingola.interval.Chromosome;
import ca.mcgill.mcb.pcingola.interval.Genome;
import ca.mcgill.mcb.pcingola.interval.SeqChange;
import ca.mcgill.mcb.pcingola.util.Gpr;
import ca.mcgill.mcb.pcingola.util.Timer;
import ca.mcgill.mcb.pcingola.vcf.VcfFileIndexIntervals;

/**
 * Filter variants that hit intervals
 * 
 * Use an indexed VCF file. 
 * 
 * WARNIGN: File must be uncompressed
 * 
 * @author pablocingolani
 */
public class SnpSiftCmdIntervalsIndex extends SnpSift {

	boolean listCommandLine;
	List<SeqChange> seqChanges;
	Genome genome;
	int inOffset;
	String vcfFile;
	String bedFile;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SnpSiftCmdIntervalsIndex vcfIntervals = new SnpSiftCmdIntervalsIndex(args);
		vcfIntervals.run();
	}

	public SnpSiftCmdIntervalsIndex(String[] args) {
		super(args, "intidx");
	}

	@Override
	public void init() {
		genome = new Genome("genome");
		listCommandLine = false;
		seqChanges = new ArrayList<SeqChange>();
		inOffset = 0;
		vcfFile = null;
		bedFile = null;
	}

	/**
	 * Load all BED files
	 * @param bedFileNames
	 */
	public void loadIntervals() {
		// Read filter interval file
		if (verbose) Timer.showStdErr("Reading BED file '" + bedFile + "'");

		SeqChangeBedFileIterator bf = new SeqChangeBedFileIterator(bedFile, genome, inOffset);
		bf.setCreateChromos(true);

		seqChanges = bf.load();
		Collections.sort(seqChanges); // We want the result VCF file to be sorted
		if (verbose) Timer.showStdErr("Total " + seqChanges.size() + " intervals added.");
	}

	/**
	 * Parse command line arguments
	 * @param args
	 */
	@Override
	public void parse(String[] args) {
		verbose = false;

		for (int i = 0; i < args.length; i++) {

			// Argument starts with '-'?
			if (args[i].startsWith("-")) {

				if (args[i].equals("-if")) {
					if ((i + 1) < args.length) inOffset = Gpr.parseIntSafe(args[++i]);
				} else if (args[i].equals("-v")) verbose = true;
				else if (args[i].equals("-c")) {
					listCommandLine = true;
					inOffset = 1;
				}

			} else {
				if (vcfFile == null) vcfFile = args[i];
				else {
					// Last argument
					if (bedFile == null) {
						if (listCommandLine) seqChanges.add(parsePos(args[i])); // Genomic positions in command line?
						else bedFile = args[i]; // Use BED file
					}
				}
			}
		}

		// Sanity check
		if (vcfFile == null) usage("Missing BED file");
		if (listCommandLine && (seqChanges.size() <= 0)) usage("Missing intervals");
		if (!listCommandLine && (bedFile == null)) usage("Missing VCF file");
	}

	/**
	 * Parse a genomic position
	 * @param pos
	 * @return
	 */
	SeqChange parsePos(String pos) {
		String recs[] = pos.split(":");
		if (recs.length != 2) usage("Invalid interval '" + pos + "'. Format 'chr:start-end'");
		String chr = recs[0];

		String p[] = recs[1].split("-");
		if (p.length != 2) usage("Invalid interval '" + pos + "'. Format 'chr:start-end'");
		int start = Gpr.parseIntSafe(p[0]) - inOffset;
		int end = Gpr.parseIntSafe(p[1]) - inOffset;

		Chromosome chromo = new Chromosome(genome, 0, 0, 1, chr);
		return new SeqChange(chromo, start, end, "");
	}

	/**
	 * Load a file compare calls
	 * 
	 * @param fileName
	 */
	@Override
	public void run() {
		if (!listCommandLine) loadIntervals();

		// Read and show header
		VcfFileIterator vcfFileIt = new VcfFileIterator(vcfFile);
		vcfFileIt.iterator().next(); // Read header (by reading first vcf entry)
		addHeader(vcfFileIt);
		String headerStr = vcfFileIt.getVcfHeader().toString();
		if (!headerStr.isEmpty()) System.out.println(headerStr);
		vcfFileIt.close();

		// Open and index file		
		VcfFileIndexIntervals vf = new VcfFileIndexIntervals(vcfFile);

		if (verbose) Timer.showStdErr("Indexing file '" + vcfFile + "'");
		vf.setVerbose(verbose);
		vf.open();
		vf.index();
		if (verbose) Timer.showStdErr("Done");

		// Find all intervals
		for (SeqChange sc : seqChanges) {
			try {
				if (verbose) Timer.showStdErr("Finding interval: " + sc);
				vf.dump(sc.getChromosomeName(), sc.getStart(), sc.getEnd());
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
		if (verbose) Timer.showStdErr("Done");
	}

	public void setInOffset(int inOffset) {
		this.inOffset = inOffset;
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

		System.err.println("Usage: java -jar " + SnpSift.class.getSimpleName() + ".jar intidx [-if N] [-c] file.vcf ( file.bed | chr:start1-end1 chr:start2-end2 ... chr:startN-endN )");
		System.err.println("Option:");
		System.err.println("\t-if <N>   : Input offset. Default 0 (i.e. zero-based coordinates).");
		System.err.println("\t-c        : Genomic positions in command line instead of BED file (one-based coordinates).");
		System.exit(1);
	}
}
