package ca.mcgill.mcb.pcingola.snpSql.db;

import java.io.PrintWriter;

import org.hsqldb.Server;

/**
 * HSQLDB utility class
 * 
 * @author pablocingolaniS
 */
public class HsqlDbLocalServer {

	Server server;

	/**
	 * Create a new server
	 * @param addr : IP address (set to localhost if null)
	 * @param port : Port. Use default port if negative (9001)
	 * @param dbName : Database name
	 * @param dbPath : Path to database file
	 * @param debug : Use debug mode
	 */
	public HsqlDbLocalServer(String addr, int port, String dbName, String dbPath, boolean debug) {
		init(addr, port, dbName, dbPath, debug);
	}

	/**
	 * Create a new server at localhost:default port
	 * @param dbName : Database name
	 * @param dbPath : Path to database file
	 * @param debug : Use debug mode
	 */
	public HsqlDbLocalServer(String dbName, String dbPath, boolean debug) {
		init(null, -1, dbName, dbPath, debug);
	}

	/**
	 * Initialize and start server
	 * @param addr
	 * @param port
	 * @param dbName
	 * @param dbPath
	 * @param debug
	 */
	void init(String addr, int port, String dbName, String dbPath, boolean debug) {
		server = new Server();
		server.setSilent(!debug);
		server.setTrace(debug);
		if (debug) server.setLogWriter(new PrintWriter(System.err));
		else server.setLogWriter(null);
		server.setDatabaseName(0, dbName);
		server.setDatabasePath(0, dbPath);

		if (addr == null) addr = "localhost";
		server.setAddress(addr);
		if (port > 0) server.setPort(port); // Use non-default port?

		server.start();
	}

	public void stop() {
		server.shutdownCatalogs(org.hsqldb.Database.CLOSEMODE_NORMAL);
		server.stop();
	}

}
