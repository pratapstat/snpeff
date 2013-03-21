package ca.mcgill.mcb.pcingola.snpSift;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import ca.mcgill.mcb.pcingola.fileIterator.SeekableBufferedReader;
import ca.mcgill.mcb.pcingola.fileIterator.VcfFileIterator;
import ca.mcgill.mcb.pcingola.stats.CountByType;
import ca.mcgill.mcb.pcingola.util.Gpr;
import ca.mcgill.mcb.pcingola.util.Timer;
import ca.mcgill.mcb.pcingola.vcf.FileIndexChrPos;
import ca.mcgill.mcb.pcingola.vcf.VcfEntry;
import ca.mcgill.mcb.pcingola.vcf.VcfGenotype;

/**
 * Calculate genotyping concordance between two VCF files.
 * 
 * Typical usage: Calculate concordance between sequencing experiment and genotypting experiment.
 *  
 * @author pcingola
 */
public class SnpSiftCmdConcordance extends SnpSift {

	public static final String GENOTYPE_MISSING = "missing_genotype";
	public static final String GENOTYPE_CHANGE = "change_";
	public static final String SEP = "_";
	public static final int SHOW_EVERY = 10000;

	int countEntries;
	String vcfFileName1, vcfFileName2;
	String name1, name2;
	String chrPrev = "";
	int[] idx2toidx1; // How sample 2 index maps to sample 1 index
	CountByType errors = new CountByType();
	CountByType concordance = new CountByType();
	ArrayList<String> labels;
	FileIndexChrPos indexVcf;

	protected VcfEntry latestVcfEntry = null;

	public SnpSiftCmdConcordance(String args[]) {
		super(args, "concordance");
	}

	/**
	 * Check that VCF entries match
	 * @param ve1
	 * @param ve2
	 * @return
	 */
	boolean check(VcfEntry ve1, VcfEntry ve2) {
		//---
		// Sanity checks
		//---
		if (ve1.getAlts().length > 1) {
			errors(ve1, ve2, "Multiple ALT files in " + vcfFileName1);
			return false;
		}

		if (ve2.getAlts().length > 1) {
			errors(ve1, ve2, "Multiple ALT files in " + vcfFileName2);
			return false;
		}

		if (!ve1.getAltsStr().equals(ve2.getAltsStr())) {
			errors(ve1, ve2, "ALT field does not match");
			return false;
		}

		if (!ve1.getRef().equals(ve2.getRef())) {
			errors(ve1, ve2, "REF field does not match");
			return false;
		}

		if (!ve1.getChromosomeName().equals(ve2.getChromosomeName())) {
			errors(ve1, ve2, "CHROMO field does not match");
			return false;
		}

		if (ve1.getStart() != ve2.getStart()) {
			errors(ve1, ve2, "POS field does not match");
			return false;
		}

		return true;
	}

	/**
	 * Calculate concordance 
	 * @param ve1
	 * @param ve2
	 */
	void concordance(VcfEntry ve1, VcfEntry ve2) {
		// Check that VCF entries match
		if (!check(ve1, ve2)) return;

		int idx2 = 0;
		CountByType count = new CountByType();

		// Compare all genotypes from ve2 to the corresponding genotype in ve1
		for (VcfGenotype gen2 : ve2) {

			if (gen2.isMissing()) {
				String label = GENOTYPE_MISSING + SEP + name2;
				concordance.inc(label);
				count.inc(label);
			} else {
				int idx1 = idx2toidx1[idx2];
				if (idx1 >= 0) {
					VcfGenotype gen1 = ve1.getVcfGenotype(idx1);
					if (gen1.isMissing()) {
						String label = GENOTYPE_MISSING + SEP + name1;
						concordance.inc(label);
						count.inc(label);
					} else {
						int score1 = gen1.getGenotypeCode();
						int score2 = gen2.getGenotypeCode();

						String label = GENOTYPE_CHANGE + score1 + SEP + score2;
						concordance.inc(label);
						count.inc(label);
					}
				}
			}

			idx2++;
		}

		showCounts(ve1, count);
	}

	/**
	 * Log and show errors
	 * @param ve1
	 * @param ve2
	 * @param message
	 */
	void errors(VcfEntry ve1, VcfEntry ve2, String message) {
		errors.inc(message);
		if (verbose) System.err.println("ERROR: " + message + "\n\tVCF entry " + name1 + "\t" + ve1 + "\n\tVCF entry " + name2 + "\t" + ve2);
	}

	/**
	 * Find an entry in 'vcfFile' that matches chromosome and position from 'vcfEntry'
	 * 
	 * @param vcfFile
	 * @param vcfEntry
	 * @return A matching entry, null if not found
	 * @throws IOException
	 */
	VcfEntry find(VcfFileIterator vcfFile, VcfEntry vcfEntry) throws IOException {
		//---
		// Do we have to seek to chromosome position (in vcfFile)?
		//---
		String chr = vcfEntry.getChromosomeName();
		if (!chr.equals(chrPrev)) {
			if (debug) Gpr.debug("Looking for chromosome '" + chr + "'");
			// Get to the beginning of the new chromosome
			long start = indexVcf.getStart(chr);

			// No such chromosome?
			if (start < 0) {
				warn("Chromosome '" + chr + "' not found in database.");
				return null;
			}

			// Seek 
			vcfFile.seek(start);
			latestVcfEntry = null;
			if (verbose) Timer.showStdErr("Chromosome: '" + chr + "'");
		}
		chrPrev = chr;

		//---
		// Compare 'latestVcfEntry' 
		//---
		if (latestVcfEntry != null) {
			// Sanity check
			if (!latestVcfEntry.getChromosomeName().equals(chr)) return null;
			if (vcfEntry.getStart() < latestVcfEntry.getStart()) return null; // Not there yet
			if (vcfEntry.getStart() == latestVcfEntry.getStart()) return latestVcfEntry; // Match!

		}

		//---
		// Read more entries from vcfFile
		//---
		for (VcfEntry ve : vcfFile) {
			countEntries++;
			latestVcfEntry = ve;

			// Does this entry match?
			if (!ve.getChromosomeName().equals(chr)) return null;
			if (vcfEntry.getStart() < latestVcfEntry.getStart()) return null; // Not there yet
			if (vcfEntry.getStart() == latestVcfEntry.getStart()) return latestVcfEntry; // Match!
		}

		return null;
	}

	/**
	 * Parse command line arguments
	 */
	@Override
	public void parse(String[] args) {
		if (args.length == 0) usage(null);

		for (int i = 0; i < args.length; i++) {
			String arg = args[i];

			if (vcfFileName1 == null) vcfFileName1 = arg;
			else if (vcfFileName2 == null) vcfFileName2 = arg;
		}

		// Sanity check
		if (vcfFileName2 == null) usage("Missing vcf file");
	}

	/**
	 * Read headers and parse sample names. Create map from 
	 * sample index in vcf2 to sample index in vcf1 
	 * 
	 * @param vcf1
	 * @param vcf2
	 */
	void parseVcfSampleNames(VcfFileIterator vcf1, VcfFileIterator vcf2) {
		// Read both headers
		vcf1.readHeader();
		vcf2.readHeader();

		// Map sample names to sample number
		HashMap<String, Integer> vcf1Name2Idx = new HashMap<String, Integer>();
		int idx = 0;
		for (String sampleName : vcf1.getSampleNames())
			vcf1Name2Idx.put(sampleName, idx++);

		// Map sample names to sample number
		HashMap<String, Integer> vcf2Name2Idx = new HashMap<String, Integer>();
		idx2toidx1 = new int[vcf2.getSampleNames().size()];
		idx = 0;
		int shared = 0;
		for (String sampleName : vcf2.getSampleNames()) {
			vcf2Name2Idx.put(sampleName, idx);

			if (vcf1Name2Idx.containsKey(sampleName)) {
				shared++;
				idx2toidx1[idx] = vcf1Name2Idx.get(sampleName); // Assign to index mapping array
				if (debug) System.err.println("\tMap\tSamlple " + sampleName + "\tvcf2[" + idx + "]\t->\tvcf1[" + idx2toidx1[idx] + "]");
			} else idx2toidx1[idx] = -1;

			idx++;
		}
		// Show basic stats
		System.out.println("# Number of samples:");
		System.out.println("#\t" + vcf1.getSampleNames().size() + "\tFile " + vcfFileName1);
		System.out.println("#\t" + vcf2.getSampleNames().size() + "\tFile " + vcfFileName2);
		System.out.println("#\t" + shared + "\tBoth files");
	}

	@Override
	public void run() {
		// Assign labels
		name1 = Gpr.removeExt(Gpr.baseName(vcfFileName1));
		name2 = Gpr.removeExt(Gpr.baseName(vcfFileName2));

		//---
		// Index vcf1:  it is assumed to be the smaller of the two
		//---
		if (verbose) Timer.showStdErr("Indexing file '" + vcfFileName1 + "'");
		indexVcf = new FileIndexChrPos(vcfFileName1);
		indexVcf.setVerbose(verbose);
		indexVcf.setDebug(debug);
		indexVcf.open();
		indexVcf.index();
		indexVcf.close();

		//---
		// Open files
		//---
		VcfFileIterator vcf1, vcf2;
		try {
			if (verbose) Timer.showStdErr("Open VCF file '" + vcfFileName1 + "'");
			vcf1 = new VcfFileIterator(new SeekableBufferedReader(vcfFileName1));
			if (verbose) Timer.showStdErr("Open VCF file '" + vcfFileName2 + "'");
			vcf2 = new VcfFileIterator(vcfFileName2);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		//---
		// Get sample names and mapping
		//---
		parseVcfSampleNames(vcf1, vcf2);

		//---
		// Create labels and show title
		//---
		System.out.print("chr\tpos\tref\talt");
		labels = new ArrayList<String>();
		for (int s1 = 0; s1 <= 2; s1++)
			for (int s2 = 0; s2 <= 2; s2++) {
				String label = GENOTYPE_CHANGE + s1 + SEP + s2;
				labels.add(label);
				System.out.print("\t" + label);
			}
		labels.add(GENOTYPE_MISSING + SEP + name1);
		labels.add(GENOTYPE_MISSING + SEP + name2);
		System.out.println("\t" + GENOTYPE_MISSING + SEP + name1 + "\t" + GENOTYPE_MISSING + SEP + name2);

		//---
		// Iterate on larger file
		//---
		for (VcfEntry ve2 : vcf2) {
			try {
				VcfEntry ve1 = find(vcf1, ve2);
				if (ve1 != null) concordance(ve1, ve2);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}

			// Show progress
			if (verbose && (countEntries >= SHOW_EVERY)) {
				countEntries = 0;
				Timer.showStdErr("\t" + (latestVcfEntry != null ? latestVcfEntry.getChromosomeName() + ":" + (latestVcfEntry.getStart() + 1) : "") + "\t" + ve2.getChromosomeName() + ":" + (ve2.getStart() + 1));
			}
			countEntries++;
		}

		//---
		// Show results
		//---
		showCounts(null, concordance);
		System.out.println("# Errors:");
		for (String l : errors.keySet())
			System.out.println("#\t" + l + "\t" + errors.get(l));
	}

	/**
	 * Show a counter
	 * @param ve
	 * @param count
	 */
	void showCounts(VcfEntry ve, CountByType count) {
		if (ve != null) System.out.print(ve.getChromosomeName() + "\t" + (ve.getStart() + 1) + "\t" + ve.getRef() + "\t" + ve.getAltsStr());
		else System.out.print("# Total\t\t\t");

		for (String label : labels)
			System.out.print("\t" + count.get(label));
		System.out.println("");
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
		System.err.println("Usage: java -jar " + SnpSift.class.getSimpleName() + ".jar " + command + " [options] genotype.vcf sequencing.vcf\n");
		System.exit(1);
	}

}
