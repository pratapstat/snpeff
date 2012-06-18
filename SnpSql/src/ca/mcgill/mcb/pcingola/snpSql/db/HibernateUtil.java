package ca.mcgill.mcb.pcingola.snpSql.db;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

/**
 * Hibernate session factory class and some other useful stuff
 *
 * @author Pablo Cingolani
 */
public class HibernateUtil {

	private static SessionFactory sessionFactory;
	private static ServiceRegistry serviceRegistry;

	/**
	 * Static initialization of session
	 */
	static {
		try {
			Configuration configuration = new Configuration();
			configuration.configure();
			serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
			sessionFactory = configuration.buildSessionFactory(serviceRegistry);
		} catch (Throwable ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}

	/**
	 * Begin a database transaction
	 * @return
	 */
	public static Session beginTransaction() {
		Session s = getCurrentSession();
		s.beginTransaction();
		return s;
	}

	/**
	 * Close session factory.
	 * Opening a session factory is very expensive, so this is only done at the end of the program. 
	 */
	public static void close() {
		getSessionFactory().close();
	}

	/**
	 * COmmit database transaction
	 */
	public static void commit() {
		Session s = getCurrentSession();
		if (s != null) {
			Transaction tx = s.getTransaction();
			if (tx != null) tx.commit();
		}
	}

	/**
	 * Get current session. Here is a note from Hibernate's web 
	 * Note: 	Hibernate bind the "current session" to the current Java thread. It is opened when 
	 * 			getCurrentSession() is called for the first time, but in a "proxied" state that doesn't 
	 * 			allow you to do anything except start a transaction. When the transaction ends, either 
	 * 			through commit or roll back, the "current" Session is closed automatically.
	 */
	public static Session getCurrentSession() {
		return getSessionFactory().getCurrentSession();
	}

	public static SessionFactory getSessionFactory() {
		// Alternatively, you could look up in JNDI here
		return sessionFactory;
	}

	/**
	 * Perform a query that returns a list of objects
	 * @param queryName
	 * @return 
	 */
	public static List query(String queryName) {
		Session s = HibernateUtil.getCurrentSession();
		Query q = s.getNamedQuery(queryName);
		List results = q.list();
		return results;
	}

	/**
	 * Perform a named query that returns one integer
	 * @param name : Query name
	 * @return
	 */
	public static int queryInt(String queryName) {
		Session s = getCurrentSession();
		Query q = s.getNamedQuery(queryName);
		List<Integer> results = q.list();
		// Return first element in result list (if any)
		if ((results == null) || (results.size() <= 0) || (results.get(0) == null)) return 0;
		return results.get(0);
	}

	/**
	 * Perform a named query that returns one integer
	 * @param name : Query name
	 * @return
	 */
	public static long queryLong(String queryName) {
		Session s = getCurrentSession();
		Query q = s.getNamedQuery(queryName);
		List<Long> results = q.list();
		// Return first element in result list (if any)
		if ((results == null) || (results.size() <= 0) || (results.get(0) == null)) return 0;
		return results.get(0);
	}

	/**
	 * Perform a query that returns several rows and columns
	 * @param queryName
	 * @return 
	 */
	public static List<Object[]> queryMultiColumn(String queryName) {
		Session s = HibernateUtil.getCurrentSession();
		Query q = s.getNamedQuery(queryName);
		List<Object[]> results = q.list();
		return results;
	}

	/**
	 * Roll back (abort) database transaction)
	 */
	public static void rollback() {
		Session s = getCurrentSession();
		if (s != null) {
			Transaction tx = s.getTransaction();
			if (tx != null) tx.rollback();
		}
	}

	/**
	 * Close caches and connection pools
	 */
	public static void shutdown() {
		getSessionFactory().close();
	}
}
