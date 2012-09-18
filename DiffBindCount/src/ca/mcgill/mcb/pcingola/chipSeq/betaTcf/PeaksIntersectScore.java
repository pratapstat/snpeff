package ca.mcgill.mcb.pcingola.chipSeq.betaTcf;

import java.util.ArrayList;
import java.util.List;

import ca.mcgill.mcb.pcingola.fileIterator.SeqChangeBedFileIterator;
import ca.mcgill.mcb.pcingola.interval.Chromosome;
import ca.mcgill.mcb.pcingola.interval.Genome;
import ca.mcgill.mcb.pcingola.interval.SeqChange;
import ca.mcgill.mcb.pcingola.util.Gpr;

/**
 * Simple program to see grous of peaks intersect for different scores.
 * 
 */
public class PeaksIntersectScore {

	public static boolean verbose = false;
	public static boolean intersectList = true;

	public static final String HOME = System.getProperty("user.home");
	public static String dir = HOME + "/chip-seq/usedInDownstreamAnalysis/";

	public static String apcs17 = dir + "/APCS17_Q20_3_1000_rmdup_control_peaks.bed"; // Beta-Catenin
	public static String apcs18 = dir + "/APCS18_MF3_rmdup_Q20_peaks.bed"; // Beta-Catenin
	public static String apcs19 = dir + "/APCS19_Q20_5_1000_rmdup_control_peaks.bed"; // TCF7L2
	public static String apcs20 = dir + "/APCS20_MF4_rmdup_Q20_peaks.bed"; // TCF7L2

	Genome genome;

	public static void main(String[] args) {
		Gpr.debug("Begin");
		PeaksIntersectScore peaksOverlap = new PeaksIntersectScore();

		// Iterate
		if (intersectList) {
			String files[] = { apcs17, apcs18, apcs19, apcs20 };
			String names[] = { "apcs17", "apcs18", "apcs19", "apcs20" };

			System.out.println("File1\tFile2\tAdd\tCount 1\tCount 2\tLength 1\tLength 2\tIntersect\tAvg min dist");
			for (int i = 0; i < files.length; i++)
				for (int j = i + 1; j < files.length; j++) {
					String file1 = files[i];
					String file2 = files[j];

					verbose = true;
					System.out.print(file1 + "\t" + file2 + "\t");
					String listTxt = peaksOverlap.countIntersectScore(file1, file2, 0);

					String outFile = "/tmp/intersect_" + names[i] + "_" + names[j] + ".txt";
					Gpr.toFile(outFile, listTxt);
					System.out.println("Writting intersect list to file: '" + outFile + "'");
				}

		} else {
			String fileNameBeta = apcs18;
			String fileNameTcf = apcs20;
			for (int add = 0; add < 1000; add += 100)
				peaksOverlap.countIntersectScore(fileNameBeta, fileNameTcf, add);
		}

		Gpr.debug("End");
	}

	public PeaksIntersectScore() {
		genome = new Genome("hg");
		for (int i = 1; i < 23; i++)
			genome.add(new Chromosome(genome, 0, 0, 1, "chr" + i));
		genome.add(new Chromosome(genome, 0, 0, 1, "X"));
		genome.add(new Chromosome(genome, 0, 0, 1, "Y"));
		genome.add(new Chromosome(genome, 0, 0, 1, "MT"));
	}

	/**
	 * Count how many peaks intersect for each quality score
	 * @param fileNameBeta
	 * @param fileNameTcf
	 * @param add
	 */
	String countIntersectScore(String fileNameBeta, String fileNameTcf, int add) {
		List<SeqChange> listBeta = readPeaks(fileNameBeta, add);
		List<SeqChange> listTcf = readPeaks(fileNameTcf, add);
		StringBuffer txt = new StringBuffer();

		int countIntersect = 0;
		long sumDist = 0;

		boolean intesrects;
		for (SeqChange beta : listBeta) {
			intesrects = false;
			SeqChange intersectsWith = null;
			int minDistance = Integer.MAX_VALUE;

			for (SeqChange tcf : listTcf) {

				// Same chromosome?
				if (beta.getChromosomeName().equals(tcf.getChromosomeName())) {
					int distance = 0;
					if (beta.intersects(tcf)) {
						distance = 0;
						intesrects = true;
						intersectsWith = tcf;
					} else {
						if (beta.getEnd() < tcf.getStart()) distance = tcf.getStart() - beta.getEnd();
						else if (tcf.getEnd() < beta.getStart()) distance = beta.getStart() - tcf.getEnd();
						else throw new RuntimeException("This should never happen!");
					}

					minDistance = Math.min(minDistance, distance);
				}
			}

			if (intesrects) {
				countIntersect++;
				int start = Math.min(beta.getStart(), intersectsWith.getStart()) + 1;
				int end = Math.max(beta.getEnd(), intersectsWith.getEnd()) + 1;

				double score = Math.min(beta.getScore(), intersectsWith.getScore());

				String out = beta.getChromosomeName() //
						+ "\t" + start //
						+ "\t" + end //
						+ "\t" + beta.getId() //
						+ "\t" + score //
						+ "\n";
				System.out.print(out);
				txt.append(out);
			}
			if (minDistance < 0) throw new RuntimeException("This should never happen!");

			sumDist += minDistance;
		}
		int avgDist = (int) (sumDist / listBeta.size());

		// Length of beta intervals
		int lenBeta = 0;
		for (SeqChange beta : listBeta)
			lenBeta += beta.size();

		// Length of tcf intervals
		int lenTcf = 0;
		for (SeqChange tcf : listTcf)
			lenTcf += tcf.size();

		System.out.println(add //
				+ "\t" + listBeta.size() //
				+ "\t" + listTcf.size() //
				+ "\t" + lenBeta //
				+ "\t" + lenTcf //
				+ "\t" + countIntersect //
				+ "\t" + avgDist //
		);

		return txt.toString();
	}

	/**
	 * Read a peaks file. Add 'add' bases to each side of the interval
	 * @param fileName
	 * @param add
	 * @return
	 */
	List<SeqChange> readPeaks(String fileName, int add) {
		int positionBase = 1;
		ArrayList<SeqChange> list = new ArrayList<SeqChange>();
		SeqChangeBedFileIterator bedFile = new SeqChangeBedFileIterator(fileName, genome, positionBase);

		for (SeqChange peak : bedFile) {
			SeqChange newPeak = new SeqChange((Chromosome) peak.getParent() //
					, peak.getStart() - add //
					, peak.getEnd() + add //
					, peak.getId() //
			);

			newPeak.setScore(peak.getScore());

			list.add(newPeak);
		}

		return list;
	}

}
