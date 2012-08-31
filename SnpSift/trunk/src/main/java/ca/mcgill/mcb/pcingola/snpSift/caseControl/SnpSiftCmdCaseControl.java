package ca.mcgill.mcb.pcingola.snpSift.caseControl;

import java.util.List;

import akka.actor.Actor;
import akka.actor.Props;
import akka.actor.UntypedActorFactory;
import ca.mcgill.mcb.pcingola.akka.Master;
import ca.mcgill.mcb.pcingola.akka.vcf.VcfWorkQueue;
import ca.mcgill.mcb.pcingola.fileIterator.VcfFileIterator;
import ca.mcgill.mcb.pcingola.snpSift.SnpSift;
import ca.mcgill.mcb.pcingola.stats.FisherExactTest;
import ca.mcgill.mcb.pcingola.util.Gpr;
import ca.mcgill.mcb.pcingola.util.Timer;
import ca.mcgill.mcb.pcingola.vcf.VcfEntry;

/**
 * Calculate sample pValue form a VCF file.
 * I.e.: The probability of a SNP being in N or more cases).
 * 
 * @author pablocingolani
 */
public class SnpSiftCmdCaseControl extends SnpSift {

	public static final int SHOW_EVERY = 1000;

	boolean addFisherInfo = false; // Used only for debugging purposes

	double minPvalue = 1.0;
	Boolean homozygousCase;
	Boolean homozygousControl;
	String groups;
	String fileName;
	FisherExactTest fisherExactTest = FisherExactTest.get();
	VcfCaseControl vcfCaseControl;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SnpSiftCmdCaseControl cmd = new SnpSiftCmdCaseControl(args);
		cmd.run();
	}

	public SnpSiftCmdCaseControl(String args[]) {
		super(args, "casecontrol");
		vcfCaseControl = new VcfCaseControl(groups, homozygousCase, homozygousControl);
	}

	/**
	 * Lines to be added to VCF header
	 * @return
	 */
	@Override
	protected List<String> addHeader() {
		List<String> addh = super.addHeader();
		addh.addAll(vcfCaseControl.addHeader());
		return addh;
	}

	/**
	 * Is this a valid 'groups' string?
	 * @param groupsStr
	 * @return
	 */
	boolean isGroupString(String groupsStr) {
		return groupsStr.replace('+', ' ').replace('-', ' ').replace('0', ' ').trim().isEmpty();
	}

	@Override
	public void parse(String[] args) {
		if (args.length <= 0) usage(null);

		int nonOpts = -1;

		for (int argc = 0; argc < args.length; argc++) {
			if ((nonOpts < 0) && args[argc].startsWith("-")) { // Argument starts with '-'?

				if (args[argc].equals("-h") || args[argc].equalsIgnoreCase("-help")) usage(null);
				else if (args[argc].equals("-v")) verbose = true;
				else if (args[argc].equals("-q")) verbose = false;
				else if (args[argc].equals("-t")) {
					numWorkers = Gpr.parseIntSafe(args[++argc]);
					if (numWorkers <= 0) usage("Number of threads should be a positive number.");
				} else usage("Unknown option '" + args[argc] + "'");

			} else { // Other arguments
				if (nonOpts < 0) nonOpts = 0;

				switch (nonOpts) {
				case 0:
					homozygousCase = parseHomHet(args[argc]);
					break;
				case 1:
					homozygousControl = parseHomHet(args[argc]);
					break;
				case 2:
					groups = args[argc];
					break;
				case 3:
					fileName = args[argc];
					break;
				default:
					usage("Unkown parameter '" + args[argc] + "'");
					break;
				}
				nonOpts++;
			}
		}

		// Sanity check
		if (groups == null) usage("Missing paramter 'groups'");
		if (fileName == null) usage("Missing paramter 'fileName'");
		if (!isGroupString(groups)) usage("Expecting a sequence of {'+' , '-', '0'} , but got '" + groups + "'");
	}

	/**
	 *  Parse a string having indicatinf 'hom' or 'het'
	 * @param homStr
	 * @return
	 */
	Boolean parseHomHet(String homStr) {
		homStr = homStr.toUpperCase();
		// You can write 'ho*' instead of homozygous
		if (homStr.startsWith("HO")) return true;
		if (homStr.toUpperCase().startsWith("HE")) return false;
		if (homStr.toUpperCase().startsWith("AN")) return null;

		usage("Expecting 'hom', 'het' or 'any', but got '" + homStr + "'");
		return null;
	}

	/**
	 * Load a file compare calls
	 * 
	 * @param vcfFile
	 */
	@Override
	public void run() {
		String addHeader[] = addHeader().toArray(new String[0]);

		// Run multi or single threaded versions
		if (numWorkers == 1) runSingle(addHeader);
		else runMulti(addHeader);
	}

	/**
	 * Analyze the file (run multi-thread mode)
	 */
	void runMulti(final String addHeader[]) {
		if (verbose) Timer.showStdErr("Case-Control: '" + fileName + "'. Running multi-threaded mode (numThreads=" + numWorkers + ").");

		int batchSize = Master.DEFAULT_BATCH_SIZE;

		// Master factory 
		Props props = new Props(new UntypedActorFactory() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Actor create() {
				MasterVcfCaseControl master = new MasterVcfCaseControl(numWorkers, groups, homozygousCase, homozygousControl);
				master.setAddHeader(addHeader);
				return master;
			}
		});

		// Create and run queue
		VcfWorkQueue vcfWorkQueue = new VcfWorkQueue(fileName, batchSize, SHOW_EVERY, props);
		vcfWorkQueue.run(true);

		if (verbose) Timer.showStdErr("Done.");
	}

	/**
	 * Run in single threaded mode (easier to debug);
	 */
	public void runSingle(String addHeader[]) {
		if (verbose) Timer.showStdErr("Case-Control: '" + fileName + "'. Running single threaded mode.");

		// Read all vcfEntries
		VcfFileIterator vcfFile = new VcfFileIterator(fileName);

		boolean showHeader = true;
		int countEntries = 0;
		for (VcfEntry vcfEntry : vcfFile) {
			// Show header
			if (showHeader) {
				// Add header lines
				for (String add : addHeader)
					vcfFile.getVcfHeader().addLine(add);

				// Show header
				String headerStr = vcfFile.getVcfHeader().toString();
				if (!headerStr.isEmpty()) System.out.println(headerStr);
				showHeader = false;
			}

			// Is quality OK?
			if (vcfEntry.isVariant()) {
				vcfCaseControl.addInfo(vcfEntry);
				System.out.println(vcfEntry);
			}

			countEntries++;
		}

		if (verbose) {
			Timer.showStdErr("Done.");

			System.err.println("Minimum pValue                    : " + minPvalue);
			System.err.println("Number of comparissons            : " + countEntries);
			double padj = Math.min((countEntries * minPvalue), 1.0);
			System.err.println("Min P-value adjusted (Bonferroni) : " + padj);
		}
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

		System.err.println("Usage: java -jar " + SnpSift.class.getSimpleName() + ".jar caseControl [-v] [-q] [-t numThreads] <CaseControlString> file.vcf");
		System.err.println("\t-q       : Be quite");
		System.err.println("\t-v       : Be verbose");
		System.err.println("\t-t <num> : Number of threads. Default: " + numWorkers);
		System.err.println("\t<CaseControlString> : A string of {'+', '-', '0'}, one per sample, to identify two groups (case='+', control='-', neutral='0')");
		System.exit(1);
	}
}
