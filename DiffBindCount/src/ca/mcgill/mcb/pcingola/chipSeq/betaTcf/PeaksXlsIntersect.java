package ca.mcgill.mcb.pcingola.chipSeq.betaTcf;

import java.util.ArrayList;
import java.util.List;

import ca.mcgill.mcb.pcingola.chipSeq.macs.PeakXlsFileIterator;
import ca.mcgill.mcb.pcingola.chipSeq.macs.PeakXlsInfo;
import ca.mcgill.mcb.pcingola.interval.Chromosome;
import ca.mcgill.mcb.pcingola.interval.Genome;
import ca.mcgill.mcb.pcingola.interval.Marker;
import ca.mcgill.mcb.pcingola.interval.Markers;
import ca.mcgill.mcb.pcingola.snpEffect.SnpEffectPredictor;
import ca.mcgill.mcb.pcingola.stats.IntStats;
import ca.mcgill.mcb.pcingola.util.Gpr;
import ca.mcgill.mcb.pcingola.util.Timer;

/**
 * Simple program to see groups of peaks intersect for different scores.
 */
public class PeaksXlsIntersect {

	public static boolean verbose = true;
	public static int MIN_PEAK_SIZE = 50;

	String fileName[];
	Genome genome;
	SnpEffectPredictor snpEffectPredictor;
	IntStats sizeStats;

	public static void main(String[] args) {
		if (args.length <= 0) {
			System.err.println("Usage: PeaksXlsIntersect file1_peaks.xls ... fileN_peaks.xls");
			System.exit(1);
		}

		PeaksXlsIntersect peaksIntersect = new PeaksXlsIntersect(args);
		peaksIntersect.intersect();
	}

	public PeaksXlsIntersect(String fileName[]) {
		this.fileName = fileName;
	}

	/** 		 
	 * Create SnpEffect predictor and add all peaks
	 * 
	 */
	void build(List<PeakXlsInfo> list) {
		if (verbose) Timer.showStdErr("Creating predictor");
		genome = new Genome("genome");
		snpEffectPredictor = new SnpEffectPredictor(genome);

		for (PeakXlsInfo peak : list) {
			String chr = peak.getChromosomeName();
			if (genome.getChromosome(chr) == null) genome.add(new Chromosome(genome, 0, 0, 1, chr)); // Add chromosome if missing
			snpEffectPredictor.add(peak);
		}

		if (verbose) Timer.showStdErr("Building forest");
		snpEffectPredictor.buildForest();
	}

	/**
	 * Show best intersecting peak
	 * @param peak
	 * @param markers
	 */
	PeakXlsInfo getBestIntersect(PeakXlsInfo peak, Markers markers) {
		int max = 0;
		Marker mMax = null;
		for (Marker m : markers) {
			int overlap = peak.intersectSize(m);
			if (overlap > max) {
				max = overlap;
				mMax = m;
			}
		}

		// Intersection
		int start = Math.max(peak.getStart(), mMax.getStart());
		int end = Math.min(peak.getEnd(), mMax.getEnd());
		sizeStats.sample(end - start + 1);

		return new PeakXlsInfo(peak.getParent(), start, end, "", peak.getTags(), peak.getQ(), peak.getFold(), peak.getFdr());
	}

	List<PeakXlsInfo> intersect() {
		// Intersect all
		List<PeakXlsInfo> list1 = readPeaks(fileName[0]);
		List<PeakXlsInfo> res = list1;
		for (int i = 1; i < fileName.length; i++) {
			List<PeakXlsInfo> list2 = readPeaks(fileName[i]);
			res = intersect(list1, list2);

			Gpr.debug("Sizes:\tlist1: " + list1.size() + "\tlist2: " + list2.size() + "\tIntersect: " + res.size());
			list1 = res;
		}

		// Show
		for (PeakXlsInfo p : res)
			if ((p.getEnd() >= 0) && (p.size() > MIN_PEAK_SIZE)) System.out.println(p.getChromosomeName() + "\t" + p.getStart() + "\t" + p.getEnd());

		return res;
	}

	/**
	 * Count how many peaks intersect for each quality score
	 * @param fileName1
	 * @param fileName2
	 */
	List<PeakXlsInfo> intersect(List<PeakXlsInfo> list1, List<PeakXlsInfo> list2) {
		int countIntersect = 0;
		sizeStats = new IntStats();

		build(list2);

		ArrayList<PeakXlsInfo> listResult = new ArrayList<PeakXlsInfo>();

		for (PeakXlsInfo peak : list1) {
			Markers markers = snpEffectPredictor.intersects(peak);
			if (markers.size() > 0) {
				countIntersect++;
				PeakXlsInfo res = getBestIntersect(peak, markers);
				listResult.add(res);
			}
		}

		double percent = 100.0 * countIntersect / list1.size();
		System.err.println("Compare:" //
				+ "\tPeaks: " + list1.size() //
				+ "\tIntersect: " + countIntersect + "\t" + String.format("%.2f%%", percent) //
				+ "\tMean peak size: " + sizeStats.getMean() //
		);

		return listResult;
	}

	/**
	 * Read a peaks file from an XLS file (MACS 'negative peaks' format).
	 * @param fileName
	 * @param add
	 * @return
	 */
	List<PeakXlsInfo> readPeaks(String fileName) {
		if (verbose) Timer.showStdErr("Reading peaks form file '" + fileName + "'");

		ArrayList<PeakXlsInfo> list = new ArrayList<PeakXlsInfo>();
		PeakXlsFileIterator npfi = new PeakXlsFileIterator(fileName);
		for (PeakXlsInfo npi : npfi)
			list.add(npi);

		if (verbose) Timer.showStdErr("Done, " + list.size() + " intervals");
		return list;
	}

}
