package ca.mcgill.mcb.pcingola.snpSql;

import ca.mcgill.mcb.pcingola.snpSql.db.DbUtil;
import ca.mcgill.mcb.pcingola.util.Gpr;
import ca.mcgill.mcb.pcingola.util.Timer;

public class SnpSqlCmdSql extends SnpSql {

	String vcfFileName;
	String query;

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
				else if (query == null) query = args[i];
			}
		}

		// Sanity checks
		if (vcfFileName == null) usage("Missing vcf file.");
		if (query == null) usage("Missing query");

		setDatabseFomVcf(vcfFileName);
	}

	boolean query() {

		Gpr.debug(query);
		return true;
	}

	@Override
	public boolean run() {
		boolean ok = false;
		if (verbose) Timer.showStdErr("Opening database '" + database + "', path '" + databasePath + "'");

		// Create and start a new server
		DbUtil.create(database, databasePath, false, true, verbose);

		try {
			// Connect to database
			DbUtil.beginTransaction();

			// Add data
			ok = query();

			// Close connection
			DbUtil.commit();
		} catch (Throwable t) {
			DbUtil.rollback();
		}

		// Stop server
		if (verbose) Timer.showStdErr("Closing database.");
		DbUtil.get().stop();
		if (verbose) Timer.showStdErr("Done.");
		return ok;
	}

	@Override
	public void usage(String message) {
		if (message != null) System.err.println("Error: " + message + "\n");
		System.err.println("SnpSql version " + VERSION);
		System.err.println("Usage: SnpSql [sql] database query");
		System.exit(-1);
	}

}
