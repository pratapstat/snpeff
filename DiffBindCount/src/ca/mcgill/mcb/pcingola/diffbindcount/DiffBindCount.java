package ca.mcgill.mcb.pcingola.diffbindcount;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import net.sf.picard.util.Interval;
import net.sf.picard.util.IntervalList;
import net.sf.picard.util.SamLocusIterator;
import net.sf.samtools.AbstractBAMFileIndex;
import net.sf.samtools.BAMIndexMetaData;
import net.sf.samtools.SAMFileReader;
import net.sf.samtools.SAMRecord;
import ca.mcgill.mcb.pcingola.fileIterator.SeqChangeBedFileIterator;
import ca.mcgill.mcb.pcingola.interval.SeqChange;
import ca.mcgill.mcb.pcingola.util.Gpr;
import ca.mcgill.mcb.pcingola.util.Timer;

/**
 * Count reads from a BAM file given a list of intervals
 * 
 * @author pablocingolani
 */
public class DiffBindCount {

	public static int SHOW_EVERY = 10;
	public static boolean debug = true;

	List<String> samFileNames;
	String intervalsFileName;
	List<Interval> intervalList;
	ArrayList<ArrayList<Integer>> countByFile;
	boolean verbose = false;
	boolean countTotals = false;

	/**
	 * Main command line
	 * @param args
	 */
	public static void main(String[] args) {
		DiffBindCount diffBindCount = new DiffBindCount();
		diffBindCount.parse(args);
		diffBindCount.run();
	}

	public DiffBindCount() {
		samFileNames = new ArrayList<String>();
		intervalList = new ArrayList<Interval>();
		countByFile = new ArrayList<ArrayList<Integer>>();
	}

	/**
	 * Count all reads in a BAM file 
	 * Note: It uses the BAM index
	 * 
	 * @param samReader
	 * @return
	 */
	int countTotalReads(SAMFileReader samReader) {
		try {
			if (verbose) Timer.showStdErr("Counting number of reads.");
			AbstractBAMFileIndex index = (AbstractBAMFileIndex) samReader.getIndex();
			int count = 0;
			for (int i = 0; i < index.getNumberOfReferences(); i++) {
				BAMIndexMetaData meta = index.getMetaData(i);
				count += meta.getAlignedRecordCount();
			}

			if (verbose) Timer.showStdErr("Done. Total " + count + " reads.");
			return count;
		} catch (Exception e) {
			// Error? (e.g. no index)
			System.err.println("ERROR! BAM file not indexed?");
			return -1;
		}
	}

	/**
	 * Parse
	 * @param args
	 */
	void parse(String[] args) {
		// Parse command line arguments
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-v")) verbose = true;
			else if (args[i].equals("-c")) countTotals = true;
			else if (intervalsFileName == null) intervalsFileName = args[i];
			else samFileNames.add(args[i]);
		}

		// Sanity check
		if (samFileNames.size() < 1) {
			System.err.println("Usage: " + this.getClass().getSimpleName() + " [-v] intervalsFile readsFile_1 readsFile_2 ...  readsFile_N");
			System.err.println("\tintervals : Intervals in a BED or XLS (MACs).");
			System.err.println("\treadsFile : A file contianing the reads. Either BAM or SAM format.");
			System.exit(-1);
		}

	}

	/**
	 * Read intervals from BED or XLS
	 */
	protected List<Interval> readIntervals(String intervalsFileName) {
		// Create interval list
		if (verbose) Timer.showStdErr("Reading intervals from: " + intervalsFileName);
		List<Interval> intList = new ArrayList<Interval>();

		// Read from file
		SeqChangeBedFileIterator bed = new SeqChangeBedFileIterator(intervalsFileName);
		for (SeqChange sc : bed) {
			Interval interval = new Interval(sc.getChromosomeNameOri(), sc.getStart(), sc.getEnd());
			intList.add(interval);
		}

		// Show count (sanity check)
		if (verbose) Timer.showStdErr("Total " + intList.size() + " intervals added.");
		if (intList.size() <= 0) throw new RuntimeException("No intervals, nothing to do!");

		return intList;
	}

	/**
	 * Run
	 */
	public void run() {
		if (countTotals) runCountTotals();
		else {
			runCountIntervals();
			System.out.print(this);
		}
	}

	/**
	 * Count reads onto intervals
	 */
	void runCountIntervals() {
		// Iterate over all BAM/SAM files
		for (String samFileName : samFileNames) {
			try {
				ArrayList<Integer> countReads = new ArrayList<Integer>();

				// Open file
				if (verbose) Timer.showStdErr("Reading sam file: " + samFileName);
				SAMFileReader samReader = new SAMFileReader(new File(samFileName));

				countTotalReads(samReader);

				// Read intervals from XLS or BED file
				if (intervalList.isEmpty()) intervalList = readIntervals(intervalsFileName); // Read list of intervals
				IntervalList intList = new IntervalList(samReader.getFileHeader()); // Create and populate an interval list that is compatible with SamLocusIterator.
				for (Interval i : intervalList)
					intList.add(i);

				// Create interval iterator
				SamLocusIterator samLocusIter = new SamLocusIterator(samReader, intList, true);
				int intervalNumber = -1, show = 1;
				Interval interval = null;
				HashSet<String> reads = new HashSet<String>();

				// Iterate over all locations
				for (SamLocusIterator.LocusInfo locusInfo : samLocusIter) {
					// Next interval in the list?
					if ((interval == null) //
							|| !interval.getSequence().equals(locusInfo.getSequenceName()) //
							|| (locusInfo.getPosition() < interval.getStart()) //
							|| (interval.getEnd() < locusInfo.getPosition()) //
					) {
						if (interval != null) {
							if (!debug) System.out.println(interval.getSequence() + "\t" + interval.getStart() + "\t" + interval.getEnd() + "\t" + reads.size());
							countReads.add(reads.size()); // Count number of reads in this interval
						}

						intervalNumber++; // Next interval
						interval = intList.getIntervals().get(intervalNumber);
						reads = new HashSet<String>(); // Clear read count
						if (verbose) Gpr.showMark(show++, SHOW_EVERY);
					}

					// Get all reads at this position
					List<SamLocusIterator.RecordAndOffset> recAndPosList = locusInfo.getRecordAndPositions();
					for (SamLocusIterator.RecordAndOffset recAndPos : recAndPosList) {
						// Make sure we count all reads (add read name to set)
						SAMRecord samRec = recAndPos.getRecord();
						reads.add(samRec.getReadName());
					}
				}

				// Update counts
				countReads.add(reads.size()); // Count number of reads in last interval
				countByFile.add(countReads); // Add count to list
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (verbose) {
				System.err.println("");
				Timer.showStdErr("Finished file " + samFileName);
			}
		}
		if (verbose) Timer.showStdErr("Done.");
	}

	/**
	 * Count all reasd in each BAM file
	 */
	void runCountTotals() {
		for (String samFileName : samFileNames) {
			try {
				ArrayList<Integer> countReads = new ArrayList<Integer>();

				// Open file
				if (verbose) Timer.showStdErr("Reading sam file: " + samFileName);
				SAMFileReader samReader = new SAMFileReader(new File(samFileName));
				int totalReads = countTotalReads(samReader);
				System.out.println(totalReads);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (verbose) Timer.showStdErr("Done.");
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		// Show title
		sb.append("chr\tstart\tend");
		for (int j = 0; j < countByFile.size(); j++)
			sb.append("\t" + samFileNames.get(j));
		sb.append("\n");

		// Show a row for each interval
		for (int i = 0; i < intervalList.size(); i++) {
			Interval interval = intervalList.get(i);
			sb.append(interval.getSequence() + "\t" + interval.getStart() + "\t" + interval.getEnd());
			for (ArrayList<Integer> countReads : countByFile) {
				int count = (countReads.size() > i ? countReads.get(i) : 0);
				sb.append("\t" + count);
			}
			sb.append("\n");
		}

		return sb.toString();
	}
}
