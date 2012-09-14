package ca.mcgill.mcb.pcingola.diffbindcount;

import java.io.File;
import java.util.HashSet;
import java.util.List;

import net.sf.picard.util.Interval;
import net.sf.picard.util.IntervalList;
import net.sf.picard.util.SamLocusIterator;
import net.sf.samtools.SAMFileReader;
import net.sf.samtools.SAMRecord;
import ca.mcgill.mcb.pcingola.fileIterator.SeqChangeBedFileIterator;
import ca.mcgill.mcb.pcingola.interval.SeqChange;
import ca.mcgill.mcb.pcingola.util.Timer;

/**
 * Count reads from a BAM file given intervals
 * 
 * @author pablocingolani
 */
public class DiffBindCount {

	String samFileName;
	String intervalsFileName;
	IntervalList intList;
	SAMFileReader samReader;

	/**
	 * Main command line
	 * @param args
	 */
	public static void main(String[] args) {
		DiffBindCount diffBindCount = new DiffBindCount();
		diffBindCount.parse(args);
		diffBindCount.run();
	}

	/**
	 * Parse
	 * @param args
	 */
	void parse(String[] args) {
		if (args.length != 2) {
			System.err.println("Usage: " + this.getClass().getSimpleName() + " readsFile intervalsFile");
			System.err.println("\treadsFile : A file contianing the reads. Either BAM or SAM format.");
			System.err.println("\tintervals : Intervals in a BED or XLS (MACs).");
			System.exit(-1);
		}

		samFileName = args[0];
		intervalsFileName = args[1];
	}

	/**
	 * Read intervals from BED or XLS
	 */
	protected void readIntervals() {
		// Create interval list
		Timer.showStdErr("Reading intervals from: " + intervalsFileName);
		int count = 0;
		intList = new IntervalList(samReader.getFileHeader());
		SeqChangeBedFileIterator bed = new SeqChangeBedFileIterator(intervalsFileName);
		for (SeqChange sc : bed) {
			Interval interval = new Interval(sc.getChromosomeNameOri(), sc.getStart(), sc.getEnd());
			intList.add(interval);
			count++;
		}
		Timer.showStdErr("Total " + count + " intervals added.");
		if (count <= 0) throw new RuntimeException("No intervals, nothing to do!");
	}

	/**
	 * Count reads
	 */
	public void run() {
		// Open file
		Timer.showStdErr("Reading sam file: " + samFileName);
		samReader = new SAMFileReader(new File(samFileName));

		// Read intervals from XLS or BED file
		readIntervals();

		// Create interval iterator
		SamLocusIterator samLocusIter = new SamLocusIterator(samReader, intList, true);
		int intervalNumber = -1;
		Interval interval = null;
		HashSet<String> reads = new HashSet<String>();
		for (SamLocusIterator.LocusInfo locusInfo : samLocusIter) {
			// Next interval in the list?
			if ((interval == null) //
					|| !interval.getSequence().equals(locusInfo.getSequenceName()) //
					|| (locusInfo.getPosition() < interval.getStart()) //
					|| (interval.getEnd() < locusInfo.getPosition()) //
			) {
				if (interval != null) //
					System.out.println(interval.getSequence() //
							+ "\t" + interval.getStart() // 
							+ "\t" + interval.getEnd() //
							+ "\t" + reads.size() //
					);
				intervalNumber++;
				interval = intList.getIntervals().get(intervalNumber);
				reads = new HashSet<String>();
			}

			// Get all reads form this position
			List<SamLocusIterator.RecordAndOffset> recAndPosList = locusInfo.getRecordAndPositions();
			for (SamLocusIterator.RecordAndOffset recAndPos : recAndPosList) {
				SAMRecord samRec = recAndPos.getRecord();
				reads.add(samRec.getReadName());
			}
		}

		if (samLocusIter != null) samLocusIter.close();
		samReader.close();
		Timer.showStdErr("Done.");
	}
}
