package ca.mcgill.mcb.pcingola.coverage;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.samtools.SAMFileReader;
import net.sf.samtools.SAMFileReader.ValidationStringency;
import net.sf.samtools.SAMRecord;
import ca.mcgill.mcb.pcingola.interval.Chromosome;
import ca.mcgill.mcb.pcingola.interval.Genome;
import ca.mcgill.mcb.pcingola.interval.Marker;
import ca.mcgill.mcb.pcingola.outputFormatter.OutputFormatter;
import ca.mcgill.mcb.pcingola.snpEffect.SnpEffectPredictor;
import ca.mcgill.mcb.pcingola.stats.CountByKey;
import ca.mcgill.mcb.pcingola.stats.CountByType;
import ca.mcgill.mcb.pcingola.util.Gpr;
import ca.mcgill.mcb.pcingola.util.Timer;

/**
 * Count how many reads map (from many SAM/BAM files) onto markers
 * @author pcingola
 */
public class CountReadsOnMarkers {

	public static int SHOW_EVERY = 10000;
	public static boolean debug = true;

	boolean verbose = false; // Be verbose
	List<String> samFileNames;
	ArrayList<CountByKey<Marker>> countReadsByFile;
	ArrayList<CountByKey<Marker>> countBasesByFile;
	ArrayList<CountByKey<String>> countTypesByFile;
	SnpEffectPredictor snpEffectPredictor;

	public CountReadsOnMarkers() {
		init(null);
	}

	public CountReadsOnMarkers(SnpEffectPredictor snpEffectPredictor) {
		init(snpEffectPredictor);
	}

	/**
	 * Add a SAM/BAM file to be processed
	 * @param samFileName
	 */
	public void addFile(String samFileName) {
		samFileNames.add(samFileName);
	}

	/**
	 * Count reads onto intervals
	 */
	public void count() {
		Genome genome = snpEffectPredictor.getGenome();

		// Iterate over all BAM/SAM files
		for (String samFileName : samFileNames) {
			try {
				if (verbose) Timer.showStdErr("Reading reads file '" + samFileName + "'");
				CountByKey<Marker> countReads = new CountByKey<Marker>();
				CountByKey<String> countTypes = new CountByKey<String>();
				CountByKey<Marker> countBases = new CountByKey<Marker>();

				// Open file
				int readNum = 1;
				SAMFileReader sam = new SAMFileReader(new File(samFileName));
				sam.setValidationStringency(ValidationStringency.SILENT);

				for (SAMRecord samRecord : sam) {
					try {
						if (!samRecord.getReadUnmappedFlag()) { // Mapped?
							Chromosome chr = genome.getOrCreateChromosome(samRecord.getReferenceName());
							if (chr != null) {
								// Create a marker from read
								Marker read = new Marker(chr, samRecord.getAlignmentStart(), samRecord.getAlignmentEnd(), 1, "");

								// Find all intersects
								Set<Marker> regions = snpEffectPredictor.regionsMarkers(read);
								HashSet<String> doneClass = new HashSet<String>();
								for (Marker m : regions) {
									countReads.inc(m); // Count reads
									countBases.inc(m, m.intersectSize(read)); // Count number bases that intersect

									// Count by marker type (make sure we only count once per read)
									String clazz = m.getClass().getSimpleName();
									if (!doneClass.contains(clazz)) {
										countTypes.inc(clazz); // Count reads
										doneClass.add(clazz); // Do not count twice
									}
								}
							}
						}

						if (verbose) Gpr.showMark(readNum, SHOW_EVERY);
						readNum++;
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				sam.close();

				// Add count to list
				countReadsByFile.add(countReads);
				countBasesByFile.add(countBases);
				countTypesByFile.add(countTypes);
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (verbose) {
				System.err.println("");
				Timer.showStdErr("Finished reding file " + samFileName);
			}
		}
		if (verbose) Timer.showStdErr("Done.");
	}

	/**
	 * Initialize
	 * @param snpEffectPredictor
	 */
	void init(SnpEffectPredictor snpEffectPredictor) {
		samFileNames = new ArrayList<String>();
		countReadsByFile = new ArrayList<CountByKey<Marker>>();
		countBasesByFile = new ArrayList<CountByKey<Marker>>();
		countTypesByFile = new ArrayList<CountByKey<String>>();

		if (snpEffectPredictor != null) this.snpEffectPredictor = snpEffectPredictor;
		else this.snpEffectPredictor = new SnpEffectPredictor(new Genome());
	}

	public void print() {
		CountByType types = new CountByType();

		// Show title
		System.out.print("chr\tstart\tend\ttype\tIDs");
		for (int j = 0; j < countReadsByFile.size(); j++)
			System.out.print("\tReads:" + samFileNames.get(j) + "\tBases:" + samFileNames.get(j));
		System.out.print("\n");

		//---
		// Show counts by marker
		//---

		// Retrieve all possible keys, sort them
		HashSet<Marker> keys = new HashSet<Marker>();
		for (CountByKey<Marker> cbt : countReadsByFile)
			keys.addAll(cbt.keySet());

		ArrayList<Marker> keysSorted = new ArrayList<Marker>(keys.size());
		keysSorted.addAll(keys);
		Collections.sort(keysSorted);

		// Show counts for each marker
		for (Marker key : keysSorted) {
			// Show 'key' information in first columns
			System.out.print(key.getChromosomeName() //
					+ "\t" + (key.getStart() + 1) //
					+ "\t" + (key.getEnd() + 1) //
					+ "\t" + OutputFormatter.idChain(key) //
			);

			// Show counts for each file
			for (int idx = 0; idx < countReadsByFile.size(); idx++)
				System.out.print("\t" + countReadsByFile.get(idx).get(key) + "\t" + countBasesByFile.get(idx).get(key));
			System.out.print("\n");

			// Add type
			types.inc(key.getClass().getSimpleName());
		}

		//---
		// Show counts by type
		//---
		for (String type : types.keysSorted()) {
			System.out.print("total_unique\t0\t0\t" + type); // Show 'type' information in first columns

			// Show counts for each file
			for (int idx = 0; idx < countReadsByFile.size(); idx++)
				System.out.print("\t" + countTypesByFile.get(idx).get(type) + "\t" /* No bases count */);
			System.out.print("\n");
		}

		System.out.print("\n");
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

}
