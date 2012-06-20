package ca.mcgill.mcb.pcingola.snpSql;

import java.util.ArrayList;
import java.util.HashMap;

import ca.mcgill.mcb.pcingola.Pcingola;
import ca.mcgill.mcb.pcingola.logStatsServer.LogStats;
import ca.mcgill.mcb.pcingola.snpEffect.commandLine.CommandLine;
import ca.mcgill.mcb.pcingola.snpSql.db.HsqlDbLocalServer;
import ca.mcgill.mcb.pcingola.util.Gpr;

/**
 * SNP SQL
 * Create and query databases for querying VCF file information
 * 
 * @author pablocingolani
 */
public class SnpSql implements CommandLine {

	public static final String BUILD = "2012-06-18";
	public static final String VERSION_MAJOR = "0.1";
	public static final String REVISION = "";
	public static final String VERSION_SHORT = VERSION_MAJOR + REVISION;
	public static final String VERSION = "SnpSql " + VERSION_SHORT + " (build " + BUILD + "), by " + Pcingola.BY;

	protected String command = "";
	protected String[] args; // Arguments used to invoke this command
	protected String[] shiftArgs;
	protected boolean verbose = false; // Be verbose
	protected boolean log = true; // Log to server (statistics)

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Parse
		SnpSql snpSql = new SnpSql();
		snpSql.parseArgs(args);

		// Run command
		boolean ok = snpSql.run();
		int retCode = ok ? 0 : -1;

		// Done.
		System.exit(retCode);
	}

	@Override
	public void parseArgs(String[] args) {
		this.args = args;
		if (args.length <= 0) usage("Missing command");

		// Commands
		if (args[0].equalsIgnoreCase("create") //
				|| args[0].equalsIgnoreCase("sql") //
		) {
			command = args[0].toLowerCase();

			// Copy all args except initial 'command'
			ArrayList<String> argsList = new ArrayList<String>();
			for (int i = 1; i < args.length; i++) {
				if (args[i].equals("-noLog")) log = false; // This option is always available (to allow privacy in all commands)
				else argsList.add(args[i]);

				if (args[i].equals("-v")) verbose = true; // Make this option availabe here as well
			}
			shiftArgs = argsList.toArray(new String[0]);
		} else {
			command = "sql"; // Default command is 'sql'
			shiftArgs = args;
		}
	}

	/**
	 * Additional values to be reported
	 * @return
	 */
	public HashMap<String, String> reportValues() {
		HashMap<String, String> reportValues = new HashMap<String, String>();
		return reportValues;
	}

	@Override
	public boolean run() {
		boolean ok = false;
		SnpSql snpSql = null;

		//---
		// Select command to run
		//---
		if (command.equals("create")) {
			//---
			// Create database
			//---
			snpSql = new SnpSqlCmdCreate();
			snpSql.parseArgs(shiftArgs);
		} else if (command.equals("sql")) {
			//---
			// Query database
			//---
			snpSql = new SnpSqlCmdSql();
			snpSql.parseArgs(shiftArgs);
		} else throw new RuntimeException("Unknown command '" + command + "'");

		//---
		// Run 
		//---

		// Run server
		HsqlDbLocalServer hsql = new HsqlDbLocalServer("zzz", Gpr.HOME + "/snpSql/zzz", verbose);

		// Run command
		String err = "";
		try {
			ok = snpSql.run();
		} catch (Throwable t) {
			err = t.getMessage();
			t.printStackTrace();
		}

		// Report usage statistics 
		if (log) LogStats.report(VERSION, ok, verbose, args, err, snpSql.reportValues());

		// Stop server
		hsql.stop();

		return ok;
	}

	@Override
	public void usage(String message) {
		if (message != null) System.err.println("Error: " + message + "\n");
		System.err.println("SnpSql version " + VERSION);
		System.err.println("Usage: SnpSql [sql]        [options] database query");
		System.err.println("   or: SnpSql create       [options] database");
		System.exit(-1);
	}
}
