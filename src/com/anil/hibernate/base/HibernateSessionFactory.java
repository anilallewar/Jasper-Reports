/**
 * Module			: Hibernate common SessionFactory Singleton
 * File Name		: HibernateSessionFactory.java
 * Created By		: Anil Allewar
 * Date Created		: 07/09/2009
 * Last Modified by	: 
 * Date Last Modified:
 * Purpose			: This class provides a Singleton instance to load the SessionFactory and then provide
 * 			static method to get instance of Hibernate Session. The SessionFactory is a heavy object that initializes all
 * 			hbm files and their associated object mappings. Hence this class will be loaded at app start up time so as to 
 * 			minimize user response time.    
 * Revision History	:
 * Remarks			: 
 */
package com.anil.hibernate.base;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateSessionFactory {

	private static Logger logger = LoggerFactory
			.getLogger(HibernateSessionFactory.class);
	private static SessionFactory factory = null;

	/**
	 * Private constructor so that no one can initialize an instance of the
	 * object
	 */
	private HibernateSessionFactory() {
	}

	/**
	 * The static block is used to load the initial configuration and set the
	 * factory. We will load the class from CacheInitializer to ensure that the
	 * session factory is created before any calls to the getSession() method
	 * are made.
	 */
	static {
		Configuration config = new AnnotationConfiguration().configure();

		if (factory == null) {
			logger.debug("The HIbernate factory was null");
			/*
			 * try { Context ctx = new InitialContext(); 
			 * factory = (SessionFactory) ctx.lookup("aglinkSessionFactroy"); 
			 * } catch (NamingException e) {
			 * 		logger.debug("The NamingException received is: " + e.getMessage()); 
			 * } catch (Exception e) {
			 * 		logger.debug("The Exception received is: " +  e.getMessage()); 
			 * }
			 */

			// Need to configure the factory again for stand-alone application
			factory = config.buildSessionFactory();
			/*
			 * Validate the schema to find out if all associations are valid.
			 * This needs to be done after the session factory is built so that
			 * the schema update is complete before we try to validate the
			 * schema.
			 */
			new SchemaValidator(config).validate();
		}
	}

	/**
	 * This static method is used to give access to the current session
	 * object(for the thread) to the current thread. The SessionFactory will be
	 * intialized at app startup time and will be used to provide the Session to
	 * each calling method.<br>
	 * <br>
	 * 
	 * Hibernate's Transaction provide the built-in session-per-request
	 * functionality; <br>
	 * <br>
	 * 
	 * In a Jave SE environment, Hibernate can't bind the "current session" to a
	 * transaction, as it does in a JTA environment, it binds it to the current
	 * Java thread. It is opened when getCurrentSession() is called for the
	 * first time, but in a "proxied" state that doesn't allow you to do
	 * anything except start a transaction. When the transaction ends, either
	 * through commit or roll back, the "current" Session is closed
	 * automatically. The next call to getCurrentSession() starts a new proxied
	 * Session, and so on.<br>
	 * <br>
	 * 
	 * So using this method, you don't need to pass the session to every DAH
	 * method; instead have the DAH method call the
	 * getHibernateSessionInstance() method which will give back the session
	 * associated with the current thread. However since the session is in
	 * proxied state, you need to begin transaction in your manager, call DAH
	 * methods and then commit transaction in your manager. Alternately you can
	 * have wrapper methods in your DAH that start the transaction and call the
	 * other DAH methods.<br>
	 * <br>
	 * 
	 * <pre>
	 * Manager method
	 * ************************
	 * public saveData(){
	 * 	try{
	 * 			    Session session = HibernateSessionFactory
	 * 							.getCurrentHibernateSessionInstance();
	 * 				tx = session.beginTransaction();
	 * 				EntityDAH dah = new EntityDAH();
	 * 		
	 * 				dah.InsertDomesticOperatorDetails(new DomOperatorVO()
	 * 						.returnDefaultDomOpVO(), &quot;Delete&quot;);
	 * 		
	 * 				ArrayList&lt;NetworkRequest&gt; list = dah
	 * 						.getNetworkRequestOperatorDataByAccountId(&quot;XAT495858&quot;, &quot;en_US&quot;);
	 * 				dah.insertBillingData(&quot;CC&quot;);
	 * 				dah.insertBillingData(&quot;BA&quot;);
	 * 			    tx.commit();
	 * 		} catch (Exception e) {
	 * 			if (tx == null || tx.isActive()) {
	 * 				tx.rollback();
	 * 			}
	 * 	    }
	 * 	   }
	 * </pre>
	 * 
	 * @return Current Hibernate session object
	 * @author anil allewar
	 * @see https://www.hibernate.org/42.html
	 */
	public static Session getCurrentHibernateSessionInstance() {
		Session session = null;
		try {
			session = factory.getCurrentSession();
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
		return session;
	}
	
	/**
	 * This class provides a org.hibernate.classic.Session instance defined as a
	 * sub-class of org.hibernate.Session that has the Hibernate2 methods also.
	 * 
	 * @see https://www.hibernate.org/43.html
	 * @return
	 */
	public static org.hibernate.classic.Session getNewHibernateSessionInstance() {
		org.hibernate.classic.Session session = null;
		try {
			session = factory.openSession();
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
		return session;
	}
	
	/**
	 * @return the factory
	 */
	public static SessionFactory getFactory() {
		return factory;
	}
}
