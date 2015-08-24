package com.anil.hibernate.base;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.FlushMode;
import org.hibernate.SessionFactory;
import org.hibernate.StaleObjectStateException;
import org.hibernate.classic.Session;
import org.hibernate.context.ManagedSessionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used to implement the Session-per-conversation strategy for Hibernate.<br><br>
 * 
 * Suppose you want the push the data operations to Hibernate but don't want to flush to database
 * till the last request of a conversation(spanning across 2 or more HTTP request); you use this
 * filter to do that.<br><br>
 * 
 * In order to close a conversation , pass a request parameter or attribute named <code>endOfConversation</code>
 * in the request GET or POST call.
 * @author anila
 *
 */
public class HibernateSessionConversationFilter implements Filter {

	private static Logger logger = LoggerFactory
			.getLogger(HibernateSessionConversationFilter.class);

	public static final String HIBERNATE_SESSION_KEY = "hibernateSession";
	public static final String END_OF_CONVERSATION_FLAG = "endOfConversation";
	private SessionFactory factory;

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		// This variable holds the current hibernate session
		Session currentSession;

		// Try to get Hibernate session from HttpSession
		HttpSession httpSession = ((HttpServletRequest) request).getSession();

		Session disconnectedSession = (Session) httpSession
				.getAttribute(HIBERNATE_SESSION_KEY);

		try {
			if (disconnectedSession == null) {
				// Create a new session that would be flushed at the end of
				// conversation
				logger
						.debug("The session is null. So this is a new conversation");
				currentSession = HibernateSessionFactory
						.getNewHibernateSessionInstance();
				currentSession.setFlushMode(FlushMode.MANUAL);
			} else {
				logger.debug("Continuing the same session: "
						+ disconnectedSession.hashCode());
				currentSession = disconnectedSession;
			}

			logger.debug("Binding the current session to this session");
			ManagedSessionContext.bind(currentSession);

			logger.debug("Starting a database transaction");
			currentSession.beginTransaction();

			logger
					.debug("Processing the event. The session will be used for the entire conversation.");
			chain.doFilter(request, response);

			logger.debug("Unbinding Session after processing");
			currentSession = ManagedSessionContext.unbind(factory);

			// End or continue the long-running conversation?
			if (request.getAttribute(END_OF_CONVERSATION_FLAG) != null
					|| request.getParameter(END_OF_CONVERSATION_FLAG) != null) {

				logger.debug("Flushing Session");
				currentSession.flush();

				logger.debug("Committing the database transaction");
				currentSession.getTransaction().commit();

				logger.debug("Closing the Session");
				currentSession.close();

				logger.debug("Cleaning Session from HttpSession");
				httpSession.setAttribute(HIBERNATE_SESSION_KEY, null);

				logger.debug("<<< End of conversation");

			} else {

				logger.debug("Committing database transaction");
				currentSession.getTransaction().commit();

				logger.debug("Storing Session in the HttpSession");
				httpSession.setAttribute(HIBERNATE_SESSION_KEY, currentSession);

				logger.debug("> Returning to user in conversation");
			}

		} catch (StaleObjectStateException staleEx) {
			logger
					.error(
							"This interceptor does not implement optimistic concurrency control!",
							staleEx);
			logger
					.error("Your application will not work until you add compensation actions!");
			// Rollback, close everything, possibly compensate for any permanent
			// changes
			// during the conversation, and finally restart business
			// conversation. Maybe
			// give the user of the application a chance to merge some of his
			// work with
			// fresh data... what you do here depends on your applications
			// design.
			throw staleEx;
		} catch (Throwable ex) {
			// Rollback only
			try {
				if (factory.getCurrentSession().getTransaction().isActive()) {
					logger
							.error(
									"Trying to rollback database transaction after exception: ",
									ex);
					factory.getCurrentSession().getTransaction().rollback();
				}
			} catch (Throwable rbEx) {
				logger.error("Could not rollback transaction after exception!",
						rbEx);
			} finally {
				logger.error("Cleanup after exception!");

				// Cleanup
				logger.debug("Unbinding Session after exception");
				currentSession = ManagedSessionContext.unbind(factory);

				logger.debug("Closing Session after exception");
				currentSession.close();

				logger.debug("Removing Session from HttpSession");
				httpSession.setAttribute(HIBERNATE_SESSION_KEY, null);

			}

			// Let others handle it... maybe another interceptor for exceptions?
			throw new ServletException(ex);
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		this.factory = HibernateSessionFactory.getFactory();
	}
}
