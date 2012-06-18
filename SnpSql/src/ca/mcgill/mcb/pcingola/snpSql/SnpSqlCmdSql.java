package ca.mcgill.mcb.pcingola.snpSql;

public class SnpSqlCmdSql extends SnpSql {

	String database;
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
				if (database == null) database = args[i];
				else if (query == null) query = args[i];
			}
		}

		// Sanity checks
		if (database == null) usage("Missing database name");
		if (query == null) usage("Missing query");
	}

	@Override
	public boolean run() {
		boolean ok = false;
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
