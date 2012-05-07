package ca.mcgill.mcb.pcingola.snpSift;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import ca.mcgill.mcb.pcingola.fileIterator.DbNSFPEntry;
import ca.mcgill.mcb.pcingola.fileIterator.DbNSFPFileIterator;
import ca.mcgill.mcb.pcingola.fileIterator.VcfFileIterator;
import ca.mcgill.mcb.pcingola.util.Timer;
import ca.mcgill.mcb.pcingola.vcf.VcfEntry;

/**
 * Annotate a VCF file with dbNSFP (Sorting Tolerant From Intolerant) from
 * another VCF file (database) http://sites.google.com/site/jpopgen/dbNSFP
 * 
 * Paper: Liu X, Jian X, and Boerwinkle E. 2011. dbNSFP: a lightweight database
 * of human non-synonymous SNPs and their functional predictions. Human
 * Mutation. 32:894-899.
 * 
 * @author lletourn
 * 
 */
public class SnpSiftCmdAnnotateSortedDbNSFP extends SnpSift {
	private static final Map<String, String> fieldsToAdd;
	
	protected String dbNSFPFileName;
	protected String vcfFileName;
	protected int count = 0;
	protected int countAnnotated = 0;
	protected DbNSFPFileIterator dbNSFPFile;
	protected VcfFileIterator vcfFile;
	protected DbNSFPEntry currentEntry;

	static {
		fieldsToAdd = new HashMap<String, String>();
		fieldsToAdd.put("SIFT_score", "SIFT score, If a score is smaller than 0.05 the corresponding NS is predicted as 'D(amaging)' otherwise it is predicted as 'T(olerated)'");
		fieldsToAdd.put("Polyphen2_HVAR_pred", "Polyphen2 prediction based on HumVar, 'D' ('probably damaging'), 'P' ('possibly damaging') and 'B' ('benign')");
		fieldsToAdd.put("GERP_NR", "GERP++ neutral rate");
		fieldsToAdd.put("GERP_RS", "GERP++ RS score, the larger the score, the more conserved the site.");
		fieldsToAdd.put("29way_logOdds", "SiPhy score based on 29 mammals genomes. The larger the score, the more conserved the site.");
		fieldsToAdd.put("1000Gp1_AF", "Alternative allele frequency in the whole 1000Gp1 data.");
		fieldsToAdd.put("1000Gp1_AFR_AF", "Alternative allele frequency in the 1000Gp1 African descendent samples.");
		fieldsToAdd.put("1000Gp1_EUR_AF", "Alternative allele frequency in the 1000Gp1 European descendent samples.");
		fieldsToAdd.put("1000Gp1_AMR_AF", "Alternative allele frequency in the 1000Gp1 American descendent samples.");
		fieldsToAdd.put("1000Gp1_ASN_AF", "Alternative allele frequency in the 1000Gp1 Asian descendent samples.");
		fieldsToAdd.put("ESP5400_AA_AF", "Alternative allele frequency in the Afrian American samples of the NHLBI GO Exome Sequencing Project (ESP5400 data set).");
		fieldsToAdd.put("ESP5400_EA_AF", "Alternative allele frequency in the European American samples of the NHLBI GO Exome Sequencing Project (ESP5400 data set).");
	}
	
	/**
	 * Main
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) {
		SnpSiftCmdAnnotateSortedDbNSFP sift = new SnpSiftCmdAnnotateSortedDbNSFP(args);
		sift.run();
	}

	public SnpSiftCmdAnnotateSortedDbNSFP(String args[]) {
		super(args, "dbnsfp");
	}

	/**
	 * Parse command line arguments
	 */
	@Override
	public void parse(String[] args) {

		if (args.length == 0)
			usage(null);

		for (String arg : args) {
			if (arg.equals("-q")) verbose = false;
			else if (arg.equals("-v")) verbose = true;
			else if (arg.equals("-h") || arg.equals("--help")) usage(null);
			else if (dbNSFPFileName == null) dbNSFPFileName = arg;
			else if( vcfFileName == null ) vcfFileName = arg;

		}

		// Sanity check
		if (dbNSFPFileName == null)
			usage("Missing dbNSFP file");
	}

	/**
	 * Initialize annotation process
	 * @throws IOException
	 */
	public void initAnnotate() throws IOException {
		vcfFile = new VcfFileIterator(vcfFileName);
		if(dbNSFPFileName.endsWith(".gz")) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(dbNSFPFileName), 512*1024), Charset.forName("ASCII")));
			dbNSFPFile = new DbNSFPFileIterator(reader);
		} else {
			dbNSFPFile = new DbNSFPFileIterator(dbNSFPFileName);
		}
		
		currentEntry = null;
	}


	@Override
	public void run() {
		if( verbose ) Timer.showStdErr("Annotating entries from: '" + dbNSFPFileName + "'");

		try {
			initAnnotate();
		} catch(Exception e) {
			throw new RuntimeException(e);
		}

		boolean showHeader = true;
		for( VcfEntry vcfEntry : vcfFile ) {
			try {
				// Show header?
				if( showHeader ) {
					addHeader(vcfFile);
					System.out.print(vcfFile.getHeader());
					showHeader = false;
				}

				// Annotate
				annotate(vcfEntry);

				// Show
				System.out.println(vcfEntry);
				count++;
			} catch(Exception e) {
				e.printStackTrace();
			}
		}

		endAnnotate();

		// Show some stats
		if( verbose ) {
			double perc = (100.0 * countAnnotated) / count;
			Timer.showStdErr("Done." //
					+ "\n\tTotal annotated entries : " + countAnnotated //
					+ "\n\tTotal entries           : " + count //
					+ "\n\tPercent                 : " + String.format("%.2f%%", perc) //
					//+ "\n\tErrors (bad references) : " + countBadRef //
			);
		}
	}


	/**
	 * Annotate a VCF entry
	 * @param vcf
	 * @throws IOException
	 */
	public void annotate(VcfEntry vcf) throws IOException {
		while(currentEntry == null) {
			currentEntry = dbNSFPFile.next();
			if(currentEntry == null || !vcf.getChromosomeName().equals(currentEntry.getChromosomeName()) || vcf.getStart() <= currentEntry.getStart()) {
				break;
			}
			
		}
		
		if(vcf.getChromosomeName().equals(currentEntry.getChromosomeName()) && vcf.getStart() == currentEntry.getStart()) {
			StringBuilder info = new StringBuilder();
			
			for(String fieldKey : fieldsToAdd.keySet()) {
				info.setLength(0);
				for(String alt : vcf.getAlts()) {
					Map<String, String> values = currentEntry.getAltAlelleValues(alt);
					if(info.length() > 0)
						info.append(',');
					
					if(values == null)
						info.append('.');
					else
						info.append(values.get(fieldKey));
				}
				vcf.addInfo("ljb"+fieldKey, info.toString());
			}			
			
			currentEntry = null;
		}
	}

	/**
	 * Finish up annotation process
	 */
	public void endAnnotate() {
		vcfFile.close();
		dbNSFPFile.close();
	}

	/**
	 * Add some lines to header before showing it
	 * 
	 * @param vcfFile
	 */
	@Override
	protected void addHeader(VcfFileIterator vcfFile) {
		super.addHeader(vcfFile);
		for(String key : fieldsToAdd.keySet()) {
			vcfFile.addHeader("##INFO=<ID=ljb"+key+",Number=A,Type=String,Description=\""+fieldsToAdd.get(key)+"\">");
		}
	}
}
