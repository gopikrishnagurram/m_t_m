package com.wavelabs.utility;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * It's a utility class gives objects of Configurations, Session and
 * SessionFactory
 * 
 * @author gopikrishnag
 * @since 2017-02-02
 */
public class Helper {

	private static Configuration cfg = null;
	private static SessionFactory factory = null;
	private static Session session = null;
	private static int count = 0;

	/**
	 * It gives only one SessionFactory, If SessionFactory is close it never
	 * creates new SessionFactory
	 * 
	 * @return SessionFactory
	 */

	public static SessionFactory getSessionFactory() {
		if (count == 0)
			buildSessionFactory();
		return factory;
	}

	@SuppressWarnings("deprecation")
	private static void buildSessionFactory() {
		cfg = new Configuration();
		factory = cfg.configure().buildSessionFactory();
		session = factory.openSession();
		count++;
	}

	/**
	 * If session is associated with sessionFactory it gives same session, if
	 * session is not open it gives new Session
	 * 
	 * @return Session
	 */
	public static Session getSession() {

		if (count == 0) {
			buildSessionFactory();
			count++;
		}
		if (!session.isOpen()) {
			buildSession();
		}
		return session;
	}

	/**
	 * Returns the Configuration associated with SessionFactory, If
	 * sessionFactory not exist, it creates new SessionFacotry
	 * 
	 * @return Configuration
	 */
	public static Configuration getConfiguration() {

		if (count == 0)
			buildSessionFactory();

		return cfg;
	}

	private static Session buildSession() {
		session = factory.openSession();
		return session;
	}
}
