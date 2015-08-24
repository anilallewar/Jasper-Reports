package com.anil.hibernate.jpa;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used to implement the EntityManager-per-request strategy for
 * Hibernate JPA.<br><br>
 * 
 * Suppose you want the push the data operations to Hibernate but don't want each
 * operation within the request to use its own EntityManager; you use this filter 
 * to do that.<br><br>
 * 
 * @author anila
 * 
 */
public class JPARequestFilter implements Filter {

	private static Logger logger = LoggerFactory
			.getLogger(JPARequestFilter.class);

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		try {
			logger
					.debug("Processing the JPA event. The EntityManager will be created in the first call to the ThreadEntityManager.getEntityManager() method and used for the whole request");
			chain.doFilter(request, response);

			logger.debug("Closing EntityManager after request processing; this will release the EntityManager to the factory");
			ThreadEntityManager.closeEntityManager();

			logger.debug("<<< End of JPA conversation");
		} catch (Throwable ex) {
			// Rollback only
			try {
				if (ThreadEntityManager.getEntityManager() != null
						&& ThreadEntityManager.getEntityManager()
								.getTransaction().isActive()) {
					logger
							.error(
									"Trying to rollback database transaction after exception: ",
									ex);
					ThreadEntityManager.getEntityManager().getTransaction()
							.rollback();
				}
			} catch (Throwable rbEx) {
				logger.error("Could not rollback transaction after exception!",
						rbEx);
			} finally {
				logger.debug("Closing Session after exception");
				ThreadEntityManager.closeEntityManager();
			}

			// Let others handle it... maybe another interceptor for exceptions?
			throw new ServletException(ex);
		}
	}

	@Override
	public void destroy() {
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}
}
