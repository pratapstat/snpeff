package ca.mcgill.mcb.pcingola.snpSift;

import java.util.HashMap;

import ca.mcgill.mcb.pcingola.fileIterator.VcfFileIterator;
import ca.mcgill.mcb.pcingola.util.Timer;
import ca.mcgill.mcb.pcingola.vcf.VcfEntry;

/**
 * Annotate a VCF file with ID from another VCF file (database)
 * 
 * Loads db file in memory, thus it makes no assumption about order.
 * Requires tons of memory
 * 
 * @author pablocingolani
 */
public class SnpSiftCmdAnnotateMem extends SnpSift {

	public static final int SHOW = 10000;
	public static final int SHOW_LINES = 100 * SHOW;

	String vcfDb;
	String vcfFile;
	HashMap<String, String> db = new HashMap<String, String>();

	/**
	 * Main
	 * @param args
	 */
	public static void main(String[] args) {
		SnpSiftCmdAnnotateMem vcfAnnotate = new SnpSiftCmdAnnotateMem(args);
		vcfAnnotate.readDb();
		vcfAnnotate.annotate();
	}

	public SnpSiftCmdAnnotateMem(String args[]) {
		super(args, "annotate");
	}

	/**
	 * Annotate entries
	 */
	public void annotate() {
		if( verbose ) Timer.showStdErr("Annotating entries from: '" + vcfFile + "'");

		VcfFileIterator vcf = new VcfFileIterator(vcfFile);

		int countAnnotated = 0, count = 0;
		boolean showHeader = true;
		for( VcfEntry vcfEntry : vcf ) {

			// Show header?
			if( showHeader ) {
				addHeader(vcf);
				System.out.print(vcf.getHeader());
				showHeader = false;
			}

			// Anything found? => Annotate
			boolean annotated = false;
			for( int i = 0; i < vcfEntry.getAlts().length; i++ ) {
				String key = key(vcfEntry, i);
				String id = db.get(key);

				if( id != null ) {
					// Add ID
					if( !vcfEntry.getId().isEmpty() ) id = vcfEntry.getId() + ";" + id;
					vcfEntry.setId(id);

					annotated = true;
				}
			}

			// Show entry
			System.out.println(vcfEntry);

			if( annotated ) countAnnotated++;
			count++;
		}

		double perc = (100.0 * countAnnotated) / count;
		if( verbose ) Timer.showStdErr("Done." //
				+ "\n\tTotal annotated entries : " + countAnnotated //
				+ "\n\tTotal entries           : " + count //
				+ "\n\tPercent                 : " + String.format("%.2f%%", perc) //
		);
	}

	/**
	 * Create a key for a hash
	 * @param vcfEntry
	 * @return
	 */
	String key(VcfEntry vcfEntry, int altIndex) {
		return vcfEntry.getChromosomeName() + ":" + vcfEntry.getStart() + "_" + vcfEntry.getRef() + "/" + vcfEntry.getAlts()[altIndex];
	}

	/**
	 * Parse command line arguments
	 */
	@Override
	public void parse(String[] args) {
		int argNum = 0;
		if( args.length == 0 ) usage(null);

		if( args[argNum].equals("-q") ) {
			verbose = false;
			argNum++;
		}

		if( args.length >= argNum ) vcfDb = args[argNum++];
		else usage("Missing 'database.vcf'");

		if( args.length >= argNum ) vcfFile = args[argNum++];
		else usage("Missing 'file.vcf'");
	}

	/**
	 * Read database
	 */
	public void readDb() {
		if( verbose ) Timer.showStdErr("Loading database: '" + vcfDb + "'");
		VcfFileIterator dbFile = new VcfFileIterator(vcfDb);

		int count = 1;
		for( VcfEntry vcfEntry : dbFile ) {

			for( int i = 0; i < vcfEntry.getAlts().length; i++ ) {
				String key = key(vcfEntry, i);

				// Add ID
				if( db.containsKey(key) ) {
					String multipleId = db.get(key) + ";" + vcfEntry.getId();
					db.put(key, multipleId);
				} else db.put(key, vcfEntry.getId());
			}

			count++;
			if( verbose ) {
				if( count % SHOW_LINES == 0 ) System.err.print("\n" + count + "\t.");
				else if( count % SHOW == 0 ) System.err.print('.');
			}
		}

		// Show time
		if( verbose ) {
			System.err.println("");
			Timer.showStdErr("Done. Database size: " + db.size());
		}
	}

	/**
	 * Show usage message
	 * @param msg
	 */
	@Override
	public void usage(String msg) {
		if( msg != null ) {
			System.err.println("Error: " + msg);
			showCmd();
		}

		showVersion();

		System.err.println("Usage: java -jar " + SnpSift.class.getSimpleName() + ".jar annotate [-q] database.vcf file.vcf > newFile.vcf\nNote: It is assumed that both files fit in memory.");
		System.exit(1);
	}
}
