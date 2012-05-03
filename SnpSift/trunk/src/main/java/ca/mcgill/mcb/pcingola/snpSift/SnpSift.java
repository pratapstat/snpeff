package ca.mcgill.mcb.pcingola.snpSift;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ca.mcgill.mcb.pcingola.Pcingola;
import ca.mcgill.mcb.pcingola.fileIterator.VcfFileIterator;
import ca.mcgill.mcb.pcingola.snpSift.caseControl.SnpSiftCmdCaseControl;
import ca.mcgill.mcb.pcingola.snpSift.hwe.SnpSiftCmdHwe;
import ca.mcgill.mcb.pcingola.snpSift.matrix.SnpSiftCmdAlleleMatrix;
import ca.mcgill.mcb.pcingola.snpSift.matrix.SnpSiftCmdCovarianceMatrix;
import ca.mcgill.mcb.pcingola.util.Gpr;
import flanagan.analysis.Stat;

/**
 * Generic SnpSift tool caller
 * 
 * @author pablocingolani
 */
public class SnpSift {

	public static final String BUILD = "2012-04-09";

	public static final String VERSION_MAJOR = "1.7";
	public static final String REVISION = "";
	public static final String VERSION_SHORT = VERSION_MAJOR + REVISION;
	public static final String VERSION = VERSION_SHORT + " (build " + BUILD + "), by " + Pcingola.BY;

	public static final int MAX_ERRORS = 10; // Report an error no more than X times

	protected boolean verbose = false;
	protected String args[];
	protected String command;
	protected int numWorkers = Gpr.NUM_CORES; // Max number of threads (if multi-threaded version is available)

	protected HashMap<String, Integer> errCount;

	/**
	 * Main
	 * @param args
	 */
	public static void main(String[] args) {
		Stat.igSupress(); // Otherwise we can get error messages printed to STDOUT

		SnpSift snpSift = new SnpSift(args, null);
		snpSift.run();
	}

	public SnpSift(String[] args, String command) {
		this.args = args;
		this.command = command;
		errCount = new HashMap<String, Integer>();
		init();
		if (args != null) parse(args);
	}

	/**
	 * Headers to add
	 * @return
	 */
	protected List<String> addHeader() {
		ArrayList<String> newHeaders = new ArrayList<String>();
		newHeaders.add("##SnpSiftVersion=\"" + VERSION + "\"");
		newHeaders.add("##SnpSiftCmd=\"" + commandLineStr() + "\"");
		return newHeaders;
	}

	/**
	 * Add some lines to header before showing it
	 * @param vcfFile
	 */
	protected void addHeader(VcfFileIterator vcfFile) {
		for (String h : addHeader())
			vcfFile.addHeader(h);
	}

	/**
	 * Show command line
	 * @return
	 */
	protected String commandLineStr() {
		StringBuilder argsList = new StringBuilder();
		argsList.append("SnpSift " + command + " ");

		if (args != null) {
			for (String arg : args)
				argsList.append(arg + " ");
		}

		return argsList.toString();
	}

	/**
	 * Show an error message and exit
	 * @param message
	 */
	public void fatalError(String message) {
		System.err.println(message);
		System.exit(-1);
	}

	/**
	 * Initialize default values
	 */
	public void init() {
	}

	/**
	 * Parse command line arguments
	 */
	public void parse(String[] args) {
		if (args.length < 1) usage(null);

		// Get command
		command = args[0].toUpperCase();

		// Create new array shifting everything 1 position
		String newArgs[] = new String[args.length - 1];
		for (int i = 1; i < args.length; i++)
			newArgs[i - 1] = args[i];

		this.args = newArgs;
	}

	/** 
	 * Run: Executes the appropriate class
	 */
	public void run() {
		if (command.startsWith("ALLELEMAT")) SnpSiftCmdAlleleMatrix.main(args);
		else if (command.startsWith("ANNM")) SnpSiftCmdAnnotateMem.main(args);
		else if (command.startsWith("ANN")) SnpSiftCmdAnnotateSorted.main(args);
		else if (command.startsWith("CA")) SnpSiftCmdCaseControl.main(args);
		else if (command.startsWith("COVMAT")) SnpSiftCmdCovarianceMatrix.main(args);
		else if (command.startsWith("EX")) SnpSiftCmdExtractFields.main(args);
		else if (command.startsWith("FI")) SnpSiftCmdFilter.main(args);
		else if (command.startsWith("GWASCAT")) SnpSiftCmdGwasCatalog.main(args);
		else if (command.startsWith("HW")) SnpSiftCmdHwe.main(args);
		else if (command.startsWith("INTIDX")) SnpSiftCmdIntervalsIndex.main(args);
		else if (command.startsWith("IN")) SnpSiftCmdIntervals.main(args);
		else if (command.startsWith("JOIN")) SnpSiftCmdJoin.main(args);
		else if (command.startsWith("RM")) SnpSiftCmdRemoveReferenceGenotypes.main(args);
		else if (command.startsWith("SIF")) SnpSiftCmdAnnotateSortedSift.main(args);
		else if (command.startsWith("TS")) SnpSiftCmdTsTv.main(args);
		else if (command.startsWith("VARTYPE")) SnpSiftCmdVarType.main(args);
		else usage("Unknown command '" + command + "'");
	}

	/**
	 * Show command line
	 */
	public void showCmd() {
		System.err.print(SnpSift.class.getSimpleName() + " " + command + " ");
		for (String a : args)
			System.err.print(a + " ");
		System.err.println("");
	}

	/**
	 * Show version number
	 */
	public void showVersion() {
		System.err.println(SnpSift.class.getSimpleName() + " version " + VERSION);
	}

	/**
	 * Convert a sanitized expression (from Galaxy) back to the original string
	 * 
	 * References: http://www.mail-archive.com/galaxy-dev@lists.bx.psu.edu/msg00530.html
	 * 
	 * @param str
	 * @return
	 */
	public String unSanitize(String str) {
		str = str.replaceAll("__lt__", "<");
		str = str.replaceAll("__gt__", ">");
		str = str.replaceAll("__sq__", "'");
		str = str.replaceAll("__dq__", "\"");
		str = str.replaceAll("__ob__", "[");
		str = str.replaceAll("__cb__", "]");
		str = str.replaceAll("__oc__", "{");
		str = str.replaceAll("__cc__", "}");
		str = str.replaceAll("__oc__", "{");
		str = str.replaceAll("__at__", "@");
		str = str.replaceAll("__cn__", "\n");
		str = str.replaceAll("__cr__", "\r");
		str = str.replaceAll("__tc__", "\t");
		return str;
	}

	/**
	 * Show usage message
	 * @param msg
	 */
	public void usage(String msg) {
		if (msg != null) {
			System.err.println("Error: " + msg);
			showCmd();
		}

		showVersion();

		System.err.println("Usage: java -jar " + SnpSift.class.getSimpleName() + ".jar [annotate|filter|caseControl|tstv|hwe|intervals|rmRefGen] params..." //
				+ "\n\talleleMat     : Create an allele matrix output." //
				+ "\n\tannotate      : Annotate 'ID' from a database (e.g. dbSnp). Assumes entries are sorted." //
				+ "\n\tannMem        : Annotate 'ID' from a database (e.g. dbSnp). Loads db in memory. Does not assume sorted entries." //
				+ "\n\tcaseControl   : Compare how many variants are in 'case' and in 'control' groups. Also calculates p-values (Fisher exact test and Cochran-Armitage test)" //
				+ "\n\tcovMat        : Create an covariance matrix output (allele matrix as input)." //
				+ "\n\textractFields : Extract fields from VCF file into tab separated format." //
				+ "\n\tfilter        : Filter using arbitrary expressions" //
				+ "\n\tgwasCat       : Annotate using GWAS catalog" //
				+ "\n\thwe           : Calculate Hardy-Weimberg parameters and perform a godness of fit test." //
				+ "\n\tintervals     : Keep variants that intersect with intervals." //
				+ "\n\tintIdx        : Keep variants that intersect with intervals. Creates an index (only for large VCF file and only a few intervals to retrieve)" //
				+ "\n\tjoin          : Join files by genomic region." //
				+ "\n\trmRefGen      : Remove reference genotypes." //
				+ "\n\ttstv          : Calculate transiton to transversion ratio." //
				+ "\n\tsift          : Annotate using SIFT scores from a VCF file." //
				+ "\n\tvarType       : Annotate variant type (SNP,MNP,INS,DEL or MIXED)." //
		);
		System.exit(1);
	}

	/**
	 * Show a warning message (up to MAX_ERRORS times)
	 * @param warn
	 */
	protected void warn(String warn) {
		if (!errCount.containsKey(warn)) errCount.put(warn, 0);

		int count = errCount.get(warn);
		errCount.put(warn, count + 1);

		if (count < MAX_ERRORS) System.err.println("WARNING: " + warn);
	}

}
