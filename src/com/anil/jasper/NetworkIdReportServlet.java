package com.anil.jasper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.query.JRHibernateQueryExecuterFactory;
import net.sf.jasperreports.engine.util.SimpleFileResolver;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anil.hibernate.base.EntityDAH;
import com.anil.hibernate.base.HibernateSessionFactory;
import com.anil.hibernate.entity.BillingDetails;
import com.anil.hibernate.entity.NetworkRequest;
import com.anil.hibernate.jpa.ThreadEntityManager;
import com.anil.hibernate.vo.DomOperatorVO;

public class NetworkIdReportServlet extends HttpServlet {

	private static Logger logger = LoggerFactory
			.getLogger(NetworkIdReportServlet.class);

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		ServletOutputStream outputStream = response.getOutputStream();
		InputStream inputStream = getServletConfig().getServletContext()
				.getResourceAsStream("reports/NetWorkRequestHibernate.jasper");

		/*
		 * Relative paths from .jrxml files to images (or other resources) do
		 * not work as you suppose they do (i.e. relative to the .jrxml file).
		 * Instead, all relative paths are considered relative to the JVM
		 * current directory.You can customize the way files are resolved by
		 * setting a value for the REPORT_FILE_RESOLVER parameter.
		 * 
		 * The file resolver is simply a class that will be called automatically
		 * by jasper when filling the report, to let the file resolver search
		 * for files in other places.
		 * 
		 * A better option is put the resources in the classpath (maybe under
		 * WEB-INF/classes) as Jasper's default file resolver looks at the
		 * classpath anyways for resource location resolution.
		 */
		HashMap<String, Object> propertiesMap = new HashMap<String, Object>();

		String reportsDirPath = getServletConfig().getServletContext()
				.getRealPath("/reports/");
		File reportsDir = new File(reportsDirPath);
		if (!reportsDir.exists()) {
			throw new FileNotFoundException(String.valueOf(reportsDir));
		}
		propertiesMap.put(JRParameter.REPORT_FILE_RESOLVER,
				new SimpleFileResolver(reportsDir));

		//Transaction tx = null;

		try {
			// Add the Hibernate session to the Jasper Reports parameter map
			Session session = (Session)ThreadEntityManager.getEntityManager().getDelegate();
			/*
			 * Since we afre using thread local sessions; we need to start the
			 * transactions earlier to enable hibernate to load data within the
			 * same thread local session context.
			 */

			//tx = session.beginTransaction();

			propertiesMap
					.put(
							JRHibernateQueryExecuterFactory.PARAMETER_HIBERNATE_SESSION,
							session);

			response.setContentType("application/pdf");
			JasperRunManager.runReportToPdfStream(inputStream, outputStream,
					propertiesMap);

			outputStream.flush();
			outputStream.close();

			// Call the local method to test Hibernate entities
			testHibernateEntities();
			//tx.commit();

			/*
			 * NO need to close session when the transaction ends, either
			 * through commit or roll back, the "current" Session is closed
			 * automatically.
			 */

		} catch (Exception e) {
			/*
			if (tx == null || tx.isActive()) {
				tx.rollback();
			}
			*/
			// display stack trace in the browser
			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(stringWriter);
			e.printStackTrace(printWriter);
			response.setContentType("text/plain");
			response.getOutputStream().print(stringWriter.toString());
		}
	}

	private void testHibernateEntities() {

		EntityDAH dah = new EntityDAH();

		NetworkRequest nwRequest = dah.InsertDomesticOperatorDetails(new DomOperatorVO()
				.returnDefaultDomOpVO(), "Delete");

		dah.updateAS400DataSentFlag(nwRequest);
		
		ArrayList<NetworkRequest> list = dah
				.getNetworkRequestOperatorDataByAccountId("XAT495858", "en_US");

		ListIterator<NetworkRequest> iterator = list.listIterator();

		while (iterator.hasNext()) {
			logger.debug("The network request id obtained with call is:"
					+ iterator.next().getNetworkRequestId());
		}

		dah.insertBillingData("CC");
		//dah.insertBillingData("BA");

		logger.debug("Billing Details: \n");
		List<BillingDetails> listBill = dah.getBillingDetails("BD");

		ListIterator<BillingDetails> listBillIterator = listBill.listIterator();

		BillingDetails billDetails = null;

		while (listBillIterator.hasNext()) {
			billDetails = listBillIterator.next();
			logger.debug("The type of object is: "
					+ billDetails.getClass().getName()
					+ " and details are: Id="
					+ billDetails.getBillingDetailsId() + " number="
					+ billDetails.getNumber());
		}

		logger.debug("Credit Card Details: \n");
		listBill = dah.getBillingDetails("CC");

		listBillIterator = listBill.listIterator();

		billDetails = null;

		while (listBillIterator.hasNext()) {
			billDetails = listBillIterator.next();
			logger.debug("The type of object is: "
					+ billDetails.getClass().getName()
					+ " and details are: Id="
					+ billDetails.getBillingDetailsId() + " number="
					+ billDetails.getNumber());
		}

		dah = null;
	}
}
