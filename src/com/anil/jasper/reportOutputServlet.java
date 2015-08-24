package com.anil.jasper;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anil.manager.JasperConnectionReportManager;
import com.anil.manager.JasperDataSourceReportManager;

@SuppressWarnings("serial")
public class reportOutputServlet extends HttpServlet {

	private static Logger logger = LoggerFactory
	.getLogger(reportOutputServlet.class);
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String whichReport = request.getParameter("selectReport");
		String whichFormat = request.getParameter("format");

		StringTokenizer tokenizer = new StringTokenizer(whichReport, "::");

		String dataSourceName = null;

		/*
		 * Get the first token which is the report name; if there is no
		 * delimiter in the source string then the StringTokenizer returns the
		 * entire String
		 */
		if (tokenizer.hasMoreTokens()) {
			whichReport = tokenizer.nextToken();
		}
		// Get the datasource name if present
		if (tokenizer.hasMoreTokens()) {
			dataSourceName = tokenizer.nextToken();
		}

		try {

			if (dataSourceName == null) {

				JasperConnectionReportManager connMgr = new JasperConnectionReportManager();
				connMgr.createReport(whichReport, whichFormat,
						getServletContext(), request, response);
			} else {
				JasperDataSourceReportManager dsMgr = new JasperDataSourceReportManager();
				dsMgr.createReport(whichReport, whichFormat, dataSourceName,
						getServletContext(), request, response);
			}

		} catch (Exception e) {
			// display stack trace in the browser
			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(stringWriter);
			e.printStackTrace(printWriter);
			response.setContentType("text/plain");
			response.getOutputStream().print(stringWriter.toString());
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		doPost(request, response);
	}
}
