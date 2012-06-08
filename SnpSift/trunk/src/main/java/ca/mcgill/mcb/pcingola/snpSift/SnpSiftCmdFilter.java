package ca.mcgill.mcb.pcingola.snpSift;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.Tree;

import ca.mcgill.mcb.pcingola.fileIterator.VcfFileIterator;
import ca.mcgill.mcb.pcingola.snpSift.antlr.VcfFilterLexer;
import ca.mcgill.mcb.pcingola.snpSift.antlr.VcfFilterParser;
import ca.mcgill.mcb.pcingola.snpSift.lang.LangFactory;
import ca.mcgill.mcb.pcingola.snpSift.lang.condition.Condition;
import ca.mcgill.mcb.pcingola.snpSift.lang.expression.Field;
import ca.mcgill.mcb.pcingola.snpSift.lang.expression.FieldIterator;
import ca.mcgill.mcb.pcingola.util.Gpr;
import ca.mcgill.mcb.pcingola.vcf.VcfEntry;

/**
 * Generic VFC filter
 * 
 * Filter out data based on VCF attributes:
 * 		- Chromosome, Position, etc.
 * 
 * 		- Intersecting intervals (BED file)
 * 
 * 		- Quality, Coverage, etc.
 * 
 * 		- Any INFO field
 * 			- Parse expression
 * 			- Int, double fields: FiledZZ == N, FiledZZ < X, FiledZZ > X, FiledZZ <= X, FiledZZ >= X
 * 			- String: FiledZZ eq "someString", FiledZZ =~ "some*regex$"
 * 
 * 		- Samples informations
 * 			- s50 (SNPs that appear in 50% of samples or more)
 * 			- Singletons
 * 			- Doubletons
 * 			- Tripletons
 * 			- negate all previous conditions
 * 			- pValue (Fisher exact test)
 * 
 * 		- Database information
 * 			- Known (e.g. in dbSnp)
 * 			- Novel (e.g. NOT in dbSnp)
 * 
 * Advanced features:
 * 		- Map-Reduce mode (easy integration with Hadoop pipes)
 * 		- Generic data partitioner (stats)
 * 		- Statistics:
 * 			- All plots form SnpEff that do not require annotations.
 * 			- Plots: Coverage, Quality, etc.
 * 			- SNPs vs Coverage
 * 			- Ts/Tv vs Coverage
 * 
 * @author pablocingolani
 */
public class SnpSiftCmdFilter extends SnpSift {

	public static final String VERSION = SnpSiftCmdFilter.class.getSimpleName() + " v0.2";

	static boolean debug = false;

	boolean usePassField;
	String inputFile;
	String expression;
	Condition vcfExpression;
	String filterId = this.getClass().getSimpleName();
	ArrayList<HashSet<String>> sets;

	/**
	 * Main
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) {
		SnpSiftCmdFilter vcfFilter = new SnpSiftCmdFilter(args);
		vcfFilter.run();
	}

	public SnpSiftCmdFilter() {
		super(null, "filter");
	}

	public SnpSiftCmdFilter(String args[]) {
		super(args, "filter");
	}

	@Override
	protected List<String> addHeader() {
		List<String> addHeader = super.addHeader();
		String expr = expression.replace('\n', ' ').replace('\r', ' ').replace('\t', ' ').trim();
		addHeader.add("##FILTER=<ID=" + filterId + ",Description=\"" + VERSION + ", Expression used: " + expr + "\">");
		return addHeader;
	}

	/** 
	 * Read a file as a string set
	 * @param fileName
	 */
	public void addSet(String fileName) {
		// Open file and check
		String file = Gpr.readFile(fileName);
		if (file.isEmpty()) throw new RuntimeException("Could not read any entries from file '" + fileName + "'");

		// Create hash
		HashSet<String> set = new HashSet<String>();
		for (String str : file.split("\n"))
			set.add(str.trim());

		// Add set to array
		sets.add(set);
		if (verbose) System.err.println("Adding set '" + fileName + "', " + set.size() + " elements.");
	}

	/**
	 * Create a "Vcf filter condition" from an FCL definition string
	 * @param lexer : lexer to use
	 * @param verbose : be verbose?
	 * @return A new FIS (or null on error)
	 */
	private Condition createFromLexer(VcfFilterLexer lexer, boolean verbose) throws RecognitionException {
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		VcfFilterParser parser = new VcfFilterParser(tokens);

		// FclParser.fcl_return root = parser.fcl();
		VcfFilterParser.main_return root;
		root = parser.main();
		Tree parseTree = (Tree) root.getTree();

		// Error creating?
		if (parseTree == null) {
			System.err.println("Can't create expression");
			return null;
		}

		if (debug) Gpr.debug("Tree: " + parseTree.toStringTree());

		// Create a language factory
		LangFactory langFactory = new LangFactory(sets);
		return langFactory.conditionFactory(parseTree);
	}

	/**
	 * Iterate over all possible 'FieldIterator' values until one 'true' is found, otherwise return false.
	 * @param vcfEntry
	 * @return
	 */
	boolean evaluate(VcfEntry vcfEntry) {
		FieldIterator fieldIterator = FieldIterator.get();
		fieldIterator.reset();

		boolean all = true, any = false;
		do {
			boolean eval = vcfExpression.eval(vcfEntry);

			all &= eval;
			any |= eval;

			if ((fieldIterator.getType() == Field.TYPE_ALL) && !all) return all;
			if ((fieldIterator.getType() == Field.TYPE_ANY) && any) return any;

			if (fieldIterator.hasNext()) fieldIterator.next(); // End of iteration?
			else break;
		} while (true);

		if (fieldIterator.getType() == Field.TYPE_ALL) return all;
		return any;
	}

	/**
	 * Filter a file
	 * @param fileName
	 * @param expression
	 * @param createList
	 * @return
	 */
	public List<VcfEntry> filter(String fileName, String expression, boolean createList) {
		inputFile = fileName;
		this.expression = expression;
		return run(createList);
	}

	/**
	 * Initialize default values
	 */
	@Override
	public void init() {
		verbose = false;
		usePassField = false;
		inputFile = "-";
		filterId = this.getClass().getSimpleName();
		sets = new ArrayList<HashSet<String>>();
	}

	/**
	 * Parse command line options
	 * @param args
	 */
	@Override
	public void parse(String[] args) {
		for (int i = 0; i < args.length; i++) {
			// Argument starts with '-'?
			if (args[i].startsWith("-")) {
				if (args[i].equals("-h") || args[i].equalsIgnoreCase("-help")) usage(null);
				else if (args[i].equals("-f") || args[i].equalsIgnoreCase("--file")) inputFile = args[++i];
				else if (args[i].equals("-s") || args[i].equalsIgnoreCase("--set")) addSet(args[++i]);
				else if (args[i].equals("-p") || args[i].equalsIgnoreCase("--pass")) usePassField = true;
				else if (args[i].equals("-v")) verbose = true;
				else if (args[i].equals("-q")) verbose = false;
				else if (args[i].equals("-i") || args[i].equalsIgnoreCase("--filterId")) filterId = args[++i];
				else if (args[i].equals("-e") || args[i].equalsIgnoreCase("--exprfile")) {
					String exprFile = args[++i];
					if (verbose) System.err.println("Reading expression from file '" + exprFile + "'");
					expression = Gpr.readFile(exprFile);
				} else usage("Unknow option '" + args[i] + "'");
			} else if (expression == null) expression = args[i];
			else usage("Unknow parameter '" + args[i] + "'");
		}

		if (expression == null) usage("Missing filter expression!");
	}

	/**
	 * Parse expression
	 * @throws Exception
	 */
	void parseExpression() throws Exception {
		if (debug) Gpr.debug("Parse expression: \"" + expression + "\"");

		// Parse string (lexer first, then parser)
		VcfFilterLexer lexer = new VcfFilterLexer(new ANTLRStringStream(expression));

		// Parse tree and create expression
		vcfExpression = createFromLexer(lexer, true);

		if (vcfExpression == null) {
			System.err.println("Fatal error: Cannot build expression tree.");
			System.exit(-1);
		}

		if (debug) Gpr.debug("VcfExpression: " + vcfExpression);
	}

	@Override
	public void run() {
		run(false);
	}

	/**
	 * Run filter
	 * @param createList : If true, create a list with the results. If false, show results on STDOUT
	 * @return If 'createList' is true, return a list containing all vcfEntries that passed the filter. Otherwise return null.
	 */
	public List<VcfEntry> run(boolean createList) {
		// Parse expression
		try {
			parseExpression();
		} catch (Exception e) {
			e.printStackTrace();
			usage("Error parsing expression: '" + expression + "'");
		}

		// Initialize
		LinkedList<VcfEntry> passEntries = (createList ? new LinkedList<VcfEntry>() : null);

		// Open and read entries
		VcfFileIterator vcfFile = new VcfFileIterator(inputFile);
		int entryNum = 0;
		for (VcfEntry vcfEntry : vcfFile) {
			// Show header before first entry
			if (entryNum == 0) {
				addHeader();
				if (!createList) System.out.println(vcfFile.getHeader());
			}

			// Does this entry pass the filter? => Show it
			boolean show = usePassField;

			// Evaluate expression
			boolean eval = evaluate(vcfEntry);

			// Actions after evaluation
			if (usePassField) { // Add to 'FILTER' field?
				show = true;

				if (eval) vcfEntry.setFilterPass("PASS");
				else {
					// Show in filterPass field
					// Get current value
					String filter = vcfEntry.getFilterPass();
					if (filter.equals(".")) filter = ""; // Empty?
					// Append new value
					filter += (!filter.isEmpty() ? ";" : "") + filterId; // Add this filter to the not-passed list
					vcfEntry.setFilterPass(filter);
				}
			} else show = eval; // Show entry (or not) depending on evaluation 

			if (show) {
				if (passEntries != null) passEntries.add(vcfEntry); // Do not show. just add to the list
				else System.out.println(vcfEntry);
			}

			// Debug mode? => Break after a few lines
			entryNum++;
			if (debug && (entryNum > 1000)) break;
		}

		return passEntries;
	}

	/**
	 * Usage message
	 * @param msg
	 */
	@Override
	public void usage(String msg) {
		if (msg != null) {
			System.out.println("Error: " + msg);
			showCmd();
		}

		showVersion();

		System.err.println("Usage: java -jar " + SnpSift.class.getSimpleName() + "" + ".jar filter [options] 'expression'");
		System.err.println("Options:");
		System.err.println("\t-f|--file <file>      : VCF input file. Default: STDIN");
		System.err.println("\t-s|--set <file>       : Create a SET using 'file'");
		System.err.println("\t-e|--exprFile <file>  : Read expression from a file");
		System.err.println("\t-h|--help             : Show this help message");
		System.err.println("\t-p|--pass             : Use 'PASS' field instead of filtering out VCF entries");
		System.err.println("\t--galaxy              : Used from Galaxy (expressions have been sanitized).");
		System.err.println("\t-i|--filterId         : ID for this filter (##FILTER tag in header). Default: " + this.getClass().getSimpleName());
		System.exit(-1);
	}

}
