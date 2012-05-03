package ca.mcgill.mcb.pcingola.snpSift;

import ca.mcgill.mcb.pcingola.fileIterator.VcfFileIterator;
import ca.mcgill.mcb.pcingola.interval.Genome;
import ca.mcgill.mcb.pcingola.stats.TsTvStats;
import ca.mcgill.mcb.pcingola.util.Timer;
import ca.mcgill.mcb.pcingola.vcf.VcfEntry;

/**
 * Calculate Ts/Tv rations per sample (transitions vs transversions)
 * 
 * @author pablocingolani
 */
public class SnpSiftCmdTsTv {

	public static final int SHOW_EVERY = 1000;
	public static final int SHOW_EVERY_NL = 100 * SHOW_EVERY;

	Genome genome;
	TsTvStats tsTvStats;
	String vcfFileName;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SnpSiftCmdTsTv vcfTsTv = new SnpSiftCmdTsTv(args);
		vcfTsTv.run();
	}

	public SnpSiftCmdTsTv(Genome genome, Boolean homozygous) {
		this.genome = genome;
		tsTvStats = new TsTvStats(homozygous);
	}

	public SnpSiftCmdTsTv(String[] args) {
		parse(args);
	}

	/**
	 * Show an error (if not 'quiet' mode)
	 * @param message
	 */
	public void error(Throwable e, String message) {
		e.printStackTrace();
		System.err.println(message);
	}

	/**
	 * Parse command line arguments
	 * @param args
	 */
	void parse(String[] args) {
		if( args.length != 2 ) usage(null);
		int argc = 0;

		// Homozygous, heterozygous or 'any' SNPs?
		Boolean homozygous = null;
		String homStr = args[argc++].toUpperCase();
		// You can write 'ho' or 'homozygous'
		if( homStr.startsWith("HO") ) homozygous = true;
		else if( homStr.startsWith("HE") ) homozygous = false;
		else if( homStr.startsWith("AN") ) homozygous = null;
		else usage("Expecting 'hom', 'het' or 'any', but got '" + args[3] + "'");

		// Create stats object
		tsTvStats = new TsTvStats(homozygous);

		// Create genome
		genome = new Genome("genome");

		// VCF file 
		vcfFileName = args[argc++];
	}

	/**
	 * Analyze the file
	 */
	void run() {
		Timer.showStdErr("Analysing '" + vcfFileName + "'");

		VcfFileIterator vcfFile = new VcfFileIterator(vcfFileName, genome);
		vcfFile.setCreateChromos(true); // Create chromosomes when needed

		// Read all vcfEntries
		int entryNum = 1;
		for( VcfEntry vcfEntry : vcfFile ) {
			try {
				entryNum++;
				tsTvStats.sample(vcfEntry);

				// Show progress
				if( entryNum % SHOW_EVERY == 0 ) {
					if( entryNum % SHOW_EVERY_NL == 0 ) System.err.println('.');
					else System.err.print('.');
				}

			} catch(Throwable t) {
				error(t, "Error while processing VCF entry (line " + vcfFile.getLineNum() + ") :\n\t" + vcfEntry + "\n" + t);
			}

		}

		System.err.println("");
		System.out.println(tsTvStats);
		Timer.showStdErr("Done");
	}

	/**
	 * Show usage and exit
	 */
	void usage(String errMsg) {
		if( errMsg != null ) System.err.println("Error: " + errMsg);
		System.err.println("Usage: java -jar " + SnpSift.class.getSimpleName() + "" + ".jar tstv <homozygous> file1.vcf\n\t<homozygous> : Use only 'hom', 'het', 'any' genotype fields (i.e. samples). \nWARNING: Only SNPs are used for Ts/Tv calculations.");
		System.exit(1);
	}
}
