package ca.mcgill.mcb.pcingola.snpSql;

import java.util.List;

import org.hibernate.Query;

import ca.mcgill.mcb.pcingola.snpSift.SnpSiftCmdFilter;
import ca.mcgill.mcb.pcingola.snpSift.lang.condition.Condition;
import ca.mcgill.mcb.pcingola.snpSql.db.DbUtil;
import ca.mcgill.mcb.pcingola.util.Gpr;
import ca.mcgill.mcb.pcingola.util.Timer;

public class SnpSqlCmdSql extends SnpSql {

	public static final int TEST_LIMIT = 100;

	boolean testQuery;
	String vcfFileName;
	String query;
	Condition condition;
	@SuppressWarnings("rawtypes")
	List results;
	StringBuilder resulsTxt;

	public SnpSqlCmdSql() {
	}

	public SnpSqlCmdSql(String vcfFileName, String query) {
		this.vcfFileName = vcfFileName;
		this.query = query;
		setDatabseFomVcf(vcfFileName);
	}

	@SuppressWarnings("rawtypes")
	public List getResults() {
		return results;
	}

	public String getResultsTxt() {
		return resulsTxt.toString();
	}

	@Override
	public void parseArgs(String[] args) {
		this.args = args;
		for (int i = 0; i < args.length; i++) {

			// Argument starts with '-'?
			if (args[i].startsWith("-")) {
				if (args[i].equals("-v") || args[i].equalsIgnoreCase("-verbose")) verbose = true;
				if (args[i].equals("-t") || args[i].equalsIgnoreCase("-test")) testQuery = true;
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

	/**
	 * Parse query
	 * @return
	 */
	Condition parseQuery(String query) {
		Gpr.debug("QUERY:" + query);
		try {
			SnpSiftCmdFilter ssfilter = new SnpSiftCmdFilter();
			Condition condition = ssfilter.parseExpression(query);
			Gpr.debug("Condition: " + condition);
			return condition;
		} catch (Exception e) {
			System.err.println("Error parsing query: " + query);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Run query
	 * @return
	 */
	boolean query() {
		// Build query string
		query = query.replace('\t', ' ');
		query = query.replace('\n', ' ');

		if (query.toLowerCase().indexOf("where ") < 0) query = "where " + query;
		if (query.toLowerCase().indexOf("from ") < 0) {
			String where = query;
			query = "from Entry entry";

			if (where.indexOf("eff.") > 0) {
				query += ", Effect eff";
				where += " and (eff.id = entry.id) ";
			}

			query = query + " " + where;
		}
		Gpr.debug("Query: " + query);

		// Create query
		Query q = DbUtil.getCurrentSession().createQuery(query);

		// Limit to a few results
		if (testQuery) q.setMaxResults(TEST_LIMIT);

		// Show results
		results = q.list();
		resulsTxt = new StringBuilder();
		System.out.println("Number of results: " + results.size());
		if (results.size() > 0) {
			for (Object o : results) {
				if (o.getClass().isArray()) {
					Object array[] = (Object[]) o;
					for (Object oo : array) {
						System.out.print(oo + "\t");
						resulsTxt.append(oo + "\t");
					}

					System.out.println("");
					resulsTxt.append("\n");
				} else {
					System.out.println(o);
					resulsTxt.append(o + "\n");
				}
			}
		}

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
			t.printStackTrace();
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
