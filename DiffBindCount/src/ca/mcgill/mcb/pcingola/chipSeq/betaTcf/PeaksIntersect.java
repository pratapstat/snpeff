package ca.mcgill.mcb.pcingola.chipSeq.betaTcf;

import java.util.ArrayList;
import java.util.List;

import ca.mcgill.mcb.pcingola.fileIterator.SeqChangeBedFileIterator;
import ca.mcgill.mcb.pcingola.interval.Chromosome;
import ca.mcgill.mcb.pcingola.interval.Genome;
import ca.mcgill.mcb.pcingola.interval.SeqChange;
import ca.mcgill.mcb.pcingola.util.Gpr;

/**
 * Simple program to see if two grous of peaks intersect
 */
public class PeaksIntersect {

	enum Mode {
		TWO_FILES, LIST, TWO_LISTS
	}

	public static int MARK = 100;;

	public static boolean verbose = false;
	public static boolean intersectList = true;

	public static String HOME_DIR = System.getProperty("user.home");
	public static String BASE_DIR = HOME_DIR + "/chip-seq/";
	public static String DATA_DIR = BASE_DIR + "/usedInDownstreamAnalysis/";

	public static String apcs17 = DATA_DIR + "/APCS17_Q20_3_1000_rmdup_control_peaks.bed"; // Beta-Catenin
	public static String apcs18 = DATA_DIR + "/APCS18_MF3_rmdup_Q20_peaks.bed"; // Beta-Catenin
	public static String apcs19 = DATA_DIR + "/APCS19_Q20_5_1000_rmdup_control_peaks.bed"; // TCF7L2
	public static String apcs20 = DATA_DIR + "/APCS20_MF4_rmdup_Q20_peaks.bed"; // TCF7L2

	Genome genome;

	public static void main(String[] args) {
		Gpr.debug("Begin");
		PeaksIntersect peaksOverlap = new PeaksIntersect();

		Mode mode = Mode.TWO_FILES;

		switch (mode) {

		case TWO_FILES:
			// Intersect two files
			String fileNameBeta = apcs17;
			String fileNameTcf = apcs18;
			for (int add = 0; add < 1000; add += 100)
				peaksOverlap.countIntersect(fileNameBeta, fileNameTcf, add);
			break;

		case LIST:
			// Intersect all files in a list
			String files[] = { apcs17, apcs18, apcs19, apcs20 };
			String names[] = { "apcs17", "apcs18", "apcs19", "apcs20" };

			System.out.println("File1\tFile2\tAdd\tCount 1\tCount 2\tLength 1\tLength 2\tIntersect\tAvg min dist");
			for (int i = 0; i < files.length; i++)
				for (int j = i + 1; j < files.length; j++) {
					String file1 = files[i];
					String file2 = files[j];

					verbose = true;
					System.out.print(file1 + "\t" + file2 + "\t");
					String listTxt = peaksOverlap.countIntersect(file1, file2, 0);

					String outFile = "/tmp/intersect_" + names[i] + "_" + names[j] + ".txt";
					Gpr.toFile(outFile, listTxt);
					// System.out.println("Writting intersect list to file: '" + outFile + "'");
				}
			break;

		case TWO_LISTS:
			int add = 0;
			// Intersect two lists of files
			//				String files1[] = { BASE_DIR + "APCS01_MF2_rmdup_Q20_peaks.bed", BASE_DIR + "APCS01_MF3_rmdup_Q20_peaks.bed", BASE_DIR + "APCS01_MF4_rmdup_Q20_peaks.bed", BASE_DIR + "APCS01_MF5_rmdup_Q20_peaks.bed", BASE_DIR + "APCS01_MF6_rmdup_Q20_peaks.bed", BASE_DIR + "APCS01_MF7_rmdup_Q20_peaks.bed", BASE_DIR + "APCS01_MF8_rmdup_Q20_peaks.bed", BASE_DIR + "APCS01_MF9_rmdup_Q20_peaks.bed", BASE_DIR + "APCS01_MF10_rmdup_Q20_peaks.bed" };
			//				String files2[] = { BASE_DIR + "APCS02_MF2_rmdup_Q20_peaks.bed", BASE_DIR + "APCS02_MF3_rmdup_Q20_peaks.bed", BASE_DIR + "APCS02_MF4_rmdup_Q20_peaks.bed", BASE_DIR + "APCS02_MF5_rmdup_Q20_peaks.bed", BASE_DIR + "APCS02_MF6_rmdup_Q20_peaks.bed", BASE_DIR + "APCS02_MF7_rmdup_Q20_peaks.bed", BASE_DIR + "APCS02_MF8_rmdup_Q20_peaks.bed", BASE_DIR + "APCS02_MF9_rmdup_Q20_peaks.bed", BASE_DIR + "APCS02_MF10_rmdup_Q20_peaks.bed", };
			String files1[] = { apcs17, apcs19 };
			String files2[] = { apcs18, apcs20 };
			System.out.println("File1\tFile2\tAdd\tCount 1\tCount 2\tLength 1\tLength 2\tIntersect\tAvg min dist");
			for (int i = 0; i < files1.length; i++) {
				String file1 = files1[i];
				String file2 = files2[i];

				verbose = true;
				System.out.print(file1 + "\t" + file2 + "\t");
				String listTxt = peaksOverlap.countIntersect(file1, file2, add);

				String outFile = "/tmp/intersect_" + i + ".txt";
				Gpr.toFile(outFile, listTxt);
				// System.out.println("Writting intersect list to file: '" + outFile + "'");
			}
			break;

		}

		Gpr.debug("End");
	}

	public PeaksIntersect() {
		genome = new Genome("hg");
		for (int i = 1; i < 23; i++)
			genome.add(new Chromosome(genome, 0, 0, 1, "chr" + i));
		genome.add(new Chromosome(genome, 0, 0, 1, "chrX"));
		genome.add(new Chromosome(genome, 0, 0, 1, "chrY"));
		genome.add(new Chromosome(genome, 0, 0, 1, "MT"));
	}

	/**
	 * Count how many peaks intersect
	 * @param fileName1
	 * @param fileName2
	 * @param add
	 */
	String countIntersect(String fileName1, String fileName2, int add) {
		List<SeqChange> list1 = readPeaks(fileName1, add);
		List<SeqChange> list2 = readPeaks(fileName2, add);
		System.err.print("\nSizes: " + list1.size() + " / " + list2.size() + ": ");
		StringBuffer txt = new StringBuffer();

		int countIntersect = 0;
		long sumDist = 0;

		boolean intesrects;
		int count1 = 1;
		for (SeqChange seqChange1 : list1) {

			if (count1 % (100 * MARK) == 0) System.err.print('\n');
			else if (count1 % (10 * MARK) == 0) System.err.print('+');
			else if (count1 % MARK == 0) System.err.print('.');
			count1++;

			intesrects = false;
			SeqChange intersectsWith = null;
			int minDistance = Integer.MAX_VALUE;

			for (SeqChange seqChange2 : list2) {

				// Same chromosome?
				if (seqChange1.getChromosomeName().equals(seqChange2.getChromosomeName())) {
					int distance = 0;
					if (seqChange1.intersects(seqChange2)) {
						distance = 0;
						intesrects = true;
						intersectsWith = seqChange2;
					} else {
						if (seqChange1.getEnd() < seqChange2.getStart()) distance = seqChange2.getStart() - seqChange1.getEnd();
						else if (seqChange2.getEnd() < seqChange1.getStart()) distance = seqChange1.getStart() - seqChange2.getEnd();
						else throw new RuntimeException("This should never happen!");
					}

					minDistance = Math.min(minDistance, distance);
				}
			}

			if (intesrects) {
				countIntersect++;
				int start = Math.min(seqChange1.getStart(), intersectsWith.getStart()) + 1;
				int end = Math.max(seqChange1.getEnd(), intersectsWith.getEnd()) + 1;
				txt.append(seqChange1.getChromosomeName() //
						+ "\t" + start //
						+ "\t" + end //
						+ "\t" + seqChange1.getId() //
						+ "\t" + Math.min(seqChange1.getScore(), intersectsWith.getScore()) //
						+ "\n" //
				);
			}
			if (minDistance < 0) throw new RuntimeException("This should never happen!");

			sumDist += minDistance;
		}
		int avgDist = (int) (sumDist / list1.size());

		// Length of list2 intervals
		int len1 = 0;
		for (SeqChange seqChange1 : list1)
			len1 += seqChange1.size();

		// Length of tcf intervals
		int len2 = 0;
		for (SeqChange seqChange2 : list2)
			len2 += seqChange2.size();

		System.out.println(add //
				+ "\t" + list1.size() //
				+ "\t" + list2.size() //
				+ "\t" + len1 //
				+ "\t" + len2 //
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
