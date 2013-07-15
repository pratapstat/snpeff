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
import ca.mcgill.mcb.pcingola.stats.FloatStats;
import ca.mcgill.mcb.pcingola.util.Timer;

/**
 * Simple program to see groups of peaks intersect for different scores.
 */
public class PeaksXlsIntersectStats {

	public static boolean verbose = false;

	String fileName1;
	String fileName2;
	List<PeakXlsInfo> list1 = null;
	List<PeakXlsInfo> list2 = null;
	Genome genome;
	SnpEffectPredictor snpEffectPredictor;

	public static void main(String[] args) {
		if (args.length != 2) {
			System.err.println("Usage: PeaksXlsIntersect file1_peaks.xls file2_peaks.xls");
			System.exit(1);
		}

		String fileName1 = args[0];
		String fileName2 = args[1];

		PeaksXlsIntersectStats peaksIntersect = new PeaksXlsIntersectStats(fileName1, fileName2);
		peaksIntersect.readFiles();
		peaksIntersect.intersect();
	}

	public PeaksXlsIntersectStats(String fileName1, String fileName2) {
		this.fileName1 = fileName1;
		this.fileName2 = fileName2;
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

	void intersect() {
		intersect(list1, list2);
	}

	/**
	 * Count how many peaks intersect for each quality score
	 * @param fileName1
	 * @param fileName2
	 */
	void intersect(List<PeakXlsInfo> list1, List<PeakXlsInfo> list2) {
		int countIntersect = 0;

		FloatStats foldStats = new FloatStats();
		FloatStats nfoldStats = new FloatStats();

		FloatStats qStats = new FloatStats();
		FloatStats nqStats = new FloatStats();

		FloatStats fdrStats = new FloatStats();
		FloatStats nFdrStats = new FloatStats();

		build(list2);

		for (PeakXlsInfo peak : list1) {
			Markers markers = snpEffectPredictor.intersects(peak);
			if (markers.size() > 0) {
				countIntersect++;
				foldStats.sample(peak.getFold());
				qStats.sample(peak.getQ());
				fdrStats.sample(peak.getFdr());

				showBestIntersect(peak, markers);
			} else {
				nfoldStats.sample(peak.getFold());
				nqStats.sample(peak.getQ());
				nFdrStats.sample(peak.getFdr());
			}
		}

		double percent = 100.0 * countIntersect / list1.size();
		System.err.println("Compare:\t" + fileName1 + "\t" + fileName2 //
				+ "\tPeaks: " + list1.size() //
				+ "\tIntersect: " + countIntersect + "\t" + String.format("%.2f%%", percent) //
				+ "\tFold      :\t" + foldStats.toString(false) //
				+ "\tFold[not] :\t" + nfoldStats.toString(false) //
				+ "\tQ         :\t" + qStats.toString(false) //
				+ "\tQ[not]    :\t" + nqStats.toString(false) //
				+ "\tFDR         :\t" + fdrStats.toString(false) //
				+ "\tFDR[not]    :\t" + nFdrStats.toString(false) //
		);
	}

	/** 
	 * Read both files
	 * 
	 */
	void readFiles() {
		if (verbose) Timer.showStdErr("Reading peaks form file '" + fileName1 + "'");
		list1 = readPeaks(fileName1);

		if (verbose) Timer.showStdErr("Reading peaks form file '" + fileName2 + "'");
		list2 = readPeaks(fileName2);

		if (verbose) Timer.showStdErr("Done, " + list1.size() + " and " + list2.size() + " peaks.");
	}

	/**
	 * Read a peaks file from an XLS file (MACS 'negative peaks' format).
	 * @param fileName
	 * @param add
	 * @return
	 */
	List<PeakXlsInfo> readPeaks(String fileName) {
		ArrayList<PeakXlsInfo> list = new ArrayList<PeakXlsInfo>();
		PeakXlsFileIterator npfi = new PeakXlsFileIterator(fileName);
		for (PeakXlsInfo npi : npfi)
			list.add(npi);

		return list;
	}

	/**
	 * Show best intersecting peak
	 * @param peak
	 * @param markers
	 */
	void showBestIntersect(PeakXlsInfo peak, Markers markers) {
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
		int size = Math.max(peak.size(), mMax.size());
		// double perc = 100.0 * (end - start + 1) / size;

		System.out.println(peak.getChromosomeName() + "\t" + start + "\t" + end //
		//				+ "\t" + (end - start) //
		//				+ "\t" + String.format("%.0f%%", perc) //
		//				+ "\t" + peak.getFold() //
		//				+ "\t" + peak.getQ() //
				); // Show as zero based coordinates
	}

}
