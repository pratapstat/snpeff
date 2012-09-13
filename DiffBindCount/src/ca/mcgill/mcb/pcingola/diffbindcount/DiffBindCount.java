package ca.mcgill.mcb.pcingola.diffbindcount;

import java.io.File;
import java.util.List;

import net.sf.picard.util.Interval;
import net.sf.picard.util.IntervalList;
import net.sf.picard.util.SamLocusIterator;
import net.sf.samtools.SAMFileReader;
import net.sf.samtools.SAMRecord;
import ca.mcgill.mcb.pcingola.fileIterator.SeqChangeBedFileIterator;
import ca.mcgill.mcb.pcingola.interval.SeqChange;
import ca.mcgill.mcb.pcingola.snpEffect.commandLine.SnpEff;
import ca.mcgill.mcb.pcingola.util.Gpr;
import ca.mcgill.mcb.pcingola.util.Timer;

/**
 * Count reads from SnpEff
 * 
 * @author pablocingolani
 */
public class DiffBindCount extends SnpEff {

	/**
	 * Main command line
	 * @param args
	 */
	public static void main(String[] args) {
		String samFileName = Gpr.HOME + "/tbVitaminD/chipSeq/bam/Index_12_N_PLUS_H3K4m1_1h.t30l32.sorted.bam";
		String intervalsFileName = Gpr.HOME + "/tbVitaminD/chipSeq/macs/peaks/xls/Index_12_N_PLUS_H3K4m1_1h_peaks.xls";

		// Open file
		Timer.showStdErr("Reading sam file: " + samFileName);
		SAMFileReader samReader = new SAMFileReader(new File(samFileName));

		// Create interval list
		Timer.showStdErr("Reading intervals from: " + intervalsFileName);
		int count = 0;
		IntervalList intList = new IntervalList(samReader.getFileHeader());
		SeqChangeBedFileIterator bed = new SeqChangeBedFileIterator(intervalsFileName);
		for (SeqChange sc : bed) {
			Interval interval = new Interval(sc.getChromosomeNameOri(), sc.getStart(), sc.getEnd());
			intList.add(interval);
			count++;
		}
		Timer.showStdErr("Total " + count + " intervals added.");
		if (count <= 0) throw new RuntimeException("No intervals, nothing to do!");

		// Create interval iterator
		SamLocusIterator samLocusIter = new SamLocusIterator(samReader, intList, true);
		count = 0;
		for (SamLocusIterator.LocusInfo locusInfo : samLocusIter) {
			List<SamLocusIterator.RecordAndOffset> recAndPosList = locusInfo.getRecordAndPositions();
			System.out.println("LOCUS INFO: " + locusInfo + "\tSeqName:" + locusInfo.getSequenceName() + "\tPos:" + locusInfo.getPosition());
			for (SamLocusIterator.RecordAndOffset recAndPos : recAndPosList) {
				SAMRecord samRec = recAndPos.getRecord();
				System.out.println("\t" + samRec + "\t" + samRec.getReferenceName() + "\t" + samRec.getAlignmentStart() + "\t" + samRec.getAlignmentEnd() + "\t" + (samRec.getAlignmentEnd() - samRec.getAlignmentStart()));
			}
			if (count++ > 100) throw new RuntimeException("FORCD END!");
		}

		samLocusIter.close();
		samReader.close();
		Timer.showStdErr("Done.");
	}
}
