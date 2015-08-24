package com.anil.jasper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
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
import net.sf.jasperreports.engine.util.SimpleFileResolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anil.dao.ConnectionFactory;
import com.anil.hibernate.base.EntityDAH;
import com.anil.hibernate.entity.BillingDetails;
import com.anil.hibernate.entity.NetworkRequest;
import com.anil.hibernate.entity.ReqDomOperator;

public class NetworkReportGroupedServlet extends HttpServlet {

	private static Logger logger = LoggerFactory
			.getLogger(NetworkReportGroupedServlet.class);

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		Connection conn = null;
		ServletOutputStream outputStream = response.getOutputStream();
		InputStream inputStream = getServletConfig().getServletContext()
				.getResourceAsStream("reports/NetWorkRequestGrouped.jasper");

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
		HashMap<String, SimpleFileResolver> propertiesMap = new HashMap<String, SimpleFileResolver>();

		String reportsDirPath = getServletConfig().getServletContext()
				.getRealPath("/reports/");
		File reportsDir = new File(reportsDirPath);
		if (!reportsDir.exists()) {
			throw new FileNotFoundException(String.valueOf(reportsDir));
		}
		propertiesMap.put(JRParameter.REPORT_FILE_RESOLVER,
				new SimpleFileResolver(reportsDir));

		try {
			conn = ConnectionFactory
					.getConnection(ConnectionFactory.DBO_DATABASE);

			response.setContentType("application/pdf");
			JasperRunManager.runReportToPdfStream(inputStream, outputStream,
					propertiesMap, conn);

			ConnectionFactory.closeConnection(conn);
			outputStream.flush();
			outputStream.close();
			testHibernateJPAEntities();

		} catch (Exception e) {
			// display stack trace in the browser
			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(stringWriter);
			e.printStackTrace(printWriter);
			response.setContentType("text/plain");
			response.getOutputStream().print(stringWriter.toString());
		}
	}

	private void testHibernateJPAEntities() {

		EntityDAH dah = new EntityDAH();

		ArrayList<NetworkRequest> list = dah
				.getNetworkRequestDataByCreatedUser("en_US", 578318267, -7, "Y");

		ListIterator<NetworkRequest> iterator = list.listIterator();

		NetworkRequest nwRequest = null;

		while (iterator.hasNext()) {
			nwRequest = iterator.next();
			logger
					.debug("The network request id obtained with call is:"
							+ nwRequest.getNetworkRequestId()
							+ " and the Dom Operator created time is: "
							+ nwRequest.getReqDomOperators().iterator()
									.hasNext() != null ? ((ReqDomOperator) nwRequest
							.getReqDomOperators().iterator().next())
							.getCreatedDate().toString()
							: null);
		}

		logger.debug("Billing Details: \n");
		List<BillingDetails> listBill = dah.getBillingDetails("CC");

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
		dah = null;
	}
}
