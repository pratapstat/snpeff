package ca.mcgill.mcb.pcingola.snpSql;

import ca.mcgill.mcb.pcingola.snpSql.db.HibernateUtil;
import ca.mcgill.mcb.pcingola.util.Timer;

/**
 * Create a database using a VCF file
 * 
 * @author pablocingolani
 */
public class SnpSqlCmdCreate extends SnpSql {

	String database;
	String vcfFileName;

	@Override
	public void parseArgs(String[] args) {
		this.args = args;
		for (int i = 0; i < args.length; i++) {

			// Argument starts with '-'?
			if (args[i].startsWith("-")) {
				if (args[i].equals("-v") || args[i].equalsIgnoreCase("-verbose")) {
					verbose = true;
				}
			} else {
				if (vcfFileName == null) vcfFileName = args[i];
			}
		}

		// Sanity checks
		if (vcfFileName == null) usage("Missing vcf file.");
	}

	@Override
	public boolean run() {
		boolean ok = false;
		Timer.showStdErr("Creating database from file: '" + vcfFileName + "'");

		HibernateUtil.beginTransaction();
		HibernateUtil.close();

		Timer.showStdErr("Done.");
		return ok;
	}

	@Override
	public void usage(String message) {
		if (message != null) System.err.println("Error: " + message + "\n");
		System.err.println("SnpSql version " + VERSION);
		System.err.println("Usage: SnpSql [create] file.vcf");
		System.exit(-1);
	}

}
