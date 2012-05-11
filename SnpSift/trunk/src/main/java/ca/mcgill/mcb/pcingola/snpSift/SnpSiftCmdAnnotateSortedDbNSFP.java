package ca.mcgill.mcb.pcingola.snpSift;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import ca.mcgill.mcb.pcingola.fileIterator.DbNSFPEntry;
import ca.mcgill.mcb.pcingola.fileIterator.DbNSFPFileIterator;
import ca.mcgill.mcb.pcingola.fileIterator.SeekableBufferedReader;
import ca.mcgill.mcb.pcingola.fileIterator.VcfFileIterator;
import ca.mcgill.mcb.pcingola.util.Timer;
import ca.mcgill.mcb.pcingola.vcf.VcfEntry;
import ca.mcgill.mcb.pcingola.vcf.VcfFileIndexIntervals;

/**
 * Annotate a VCF file with dbNSFP (Sorting Tolerant From Intolerant) from another VCF file (database)
 * http://sites.google.com/site/jpopgen/dbNSFP
 * 
 * Paper: Liu X, Jian X, and Boerwinkle E. 2011. dbNSFP: a lightweight database of human non-synonymous SNPs and their
 * functional predictions. Human Mutation. 32:894-899.
 * 
 * @author lletourn
 * 
 */
public class SnpSiftCmdAnnotateSortedDbNSFP extends SnpSift {
  private static final String KEY_PREFIX = "dbnsfp";
  private static final Map<String, String> fieldsToAdd;

  protected String dbNSFPFileName;
  protected String vcfFileName;
  protected int count = 0;
  protected int countAnnotated = 0;
  protected DbNSFPFileIterator dbNSFPFile;
  protected VcfFileIndexIntervals indexDb;
  protected VcfFileIterator vcfFile;
  protected DbNSFPEntry currentEntry;
  protected String prevChr = null;

  static {
    fieldsToAdd = new HashMap<String, String>();
    fieldsToAdd.put("Ensembl_transcriptid", "Ensembl transcript ids (separated by ',')");
    fieldsToAdd.put("SIFT_score", "SIFT score, If a score is smaller than 0.05 the corresponding NS is predicted as 'D(amaging)' otherwise it is predicted as 'T(olerated)'");
    fieldsToAdd.put("Polyphen2_HVAR_pred", "Polyphen2 prediction based on HumVar, 'D' ('probably damaging'), 'P' ('possibly damaging') and 'B' ('benign') (separated by ',')");
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
    if (vcfFileName == null)
      usage("Missing 'file.vcf'");

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
    System.err.println("Usage: java -jar " + SnpSift.class.getSimpleName() + ".jar " + command + " [-q|-v] dbNSFP.all.gz file.vcf > newFile.vcf\n" //
        + "Options:\n" //
        + "\t-q\t:\tBe quiet.\n" //
        + "\t-v\t:\tBe verbose.\n" //
    );
    System.exit(1);
  }

  /**
   * Initialize annotation process
   * @throws IOException
   */
  public void initAnnotate() throws IOException {
    vcfFile = new VcfFileIterator(vcfFileName);
    dbNSFPFile = new DbNSFPFileIterator(new SeekableBufferedReader(dbNSFPFileName));

    indexDb = index(dbNSFPFileName);
    currentEntry = null;
  }

  /**
   * Index a VCF file
   * @param fileName
   * @return
   */
  private VcfFileIndexIntervals index(String fileName) {
    if (verbose)
      System.err.println("Index: " + fileName);
    VcfFileIndexIntervals vcfFileIndexIntervals = new VcfFileIndexIntervals(fileName);
    vcfFileIndexIntervals.setVerbose(verbose);
    vcfFileIndexIntervals.open();
    vcfFileIndexIntervals.index();
    vcfFileIndexIntervals.close();
    return vcfFileIndexIntervals;
  }

  @Override
  public void run() {
    if (verbose)
      Timer.showStdErr("Annotating entries from: '" + dbNSFPFileName + "'");

    try {
      initAnnotate();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    boolean showHeader = true;
    for (VcfEntry vcfEntry : vcfFile) {
      try {
        // Show header?
        if (showHeader) {
          addHeader(vcfFile);
          System.out.println(vcfFile.getHeader());
          showHeader = false;
        }

        // Annotate
        annotate(vcfEntry);

        // Show
        System.out.println(vcfEntry);
        count++;
      } catch(IOException e) {
        throw new RuntimeException(e);
      }
    }

    endAnnotate();

    // Show some stats
    if (verbose) {
      double perc = (100.0 * countAnnotated) / count;
      Timer.showStdErr("Done." //
          + "\n\tTotal annotated entries : " + countAnnotated //
          + "\n\tTotal entries           : " + count //
          + "\n\tPercent                 : " + String.format("%.2f%%", perc) //
      // + "\n\tErrors (bad references) : " + countBadRef //
          );
    }
  }

  /**
   * Annotate a VCF entry
   * @param vcf
   * @throws IOException
   */
  public void annotate(VcfEntry vcf) throws IOException {
    if (currentEntry == null) {
      currentEntry = dbNSFPFile.next();
      if (currentEntry == null) {
        return;
      }
      if(prevChr == null)
        prevChr = currentEntry.getChromosomeName();
    }

    // Do we have to seek in db file?
    String chr = vcf.getChromosomeName();
    if (!chr.equals(prevChr)) {
      // Get to the beginning of the new chromosome
      long start = indexDb.getStart(chr);

      // No such chromosome?
      if (start < 0) {
        warn("Chromosome '" + chr + "' not found in database.");
        return;
      }

      // Seek
      dbNSFPFile.seek(start);
      prevChr = chr;
      currentEntry = null;
    }

    while (true) {
      if(currentEntry == null) {
        currentEntry = dbNSFPFile.next();
        // Test for EOF, passed through chromosome without finding position, position not annotated
        if (currentEntry == null || !currentEntry.getChromosomeName().equals(vcf.getChromosomeName()) || vcf.getStart() <= currentEntry.getStart()) {
          return;
        }
      }

      if (vcf.getStart() == currentEntry.getStart())
        break;
      currentEntry = null;
    }

    StringBuilder info = new StringBuilder();
    for (String fieldKey : fieldsToAdd.keySet()) {
      info.setLength(0);
      for (String alt : vcf.getAlts()) {
        Map<String, String> values = currentEntry.getAltAlelleValues(alt);
        if (info.length() > 0)
          info.append(',');

        if (values == null)
          info.append('.');
        else
          info.append(values.get(fieldKey));
      }
      vcf.addInfo(KEY_PREFIX + fieldKey, info.toString().replace(';', ','));
    }

    currentEntry = null;
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
    for (String key : fieldsToAdd.keySet()) {
      vcfFile.addHeader("##INFO=<ID="+ KEY_PREFIX + key + ",Number=A,Type=String,Description=\"" + fieldsToAdd.get(key) + "\">");
    }
  }
}
