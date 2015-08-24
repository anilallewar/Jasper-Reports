package com.anil.manager;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.ensode.datasource.DataSourceFactory;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.j2ee.servlets.ImageServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JasperDataSourceReportManager extends JasperReportManager {

	Logger logger = LoggerFactory
			.getLogger(JasperDataSourceReportManager.class);

	public void createReport(String whichReport, String whichFormat,
			String dataSourceName, ServletContext context,
			HttpServletRequest request, HttpServletResponse response) {

		try {

			InputStream reportStream = context.getResourceAsStream("/reports/"
					+ whichReport + ".jasper");

			if (isFileToBeSentAsAttachment(whichFormat)) {
				response.setContentType("application/download");
				response.setHeader("Content-Disposition",
						"attachment;filename=\"" + REPORT_NAME + "."
								+ whichFormat + "\"");

				ServletOutputStream servletOutputStream = response
						.getOutputStream();

				logger.debug("The InputStream is: " + reportStream
						+ " and output stream is: " + servletOutputStream);
				JasperExportHelper.exportReport(whichFormat, reportStream,
						servletOutputStream, context, super
								.createReportParameters(context),
						DataSourceFactory
								.getDataSource(dataSourceName, context));
				servletOutputStream.flush();
				servletOutputStream.close();
			} else {
				response.setContentType("text/html");

				JRHtmlExporter htmlExporter = new JRHtmlExporter();
				logger.debug("The InputStream is: " + reportStream);
				JasperPrint jasperPrint = JasperFillManager.fillReport(
						reportStream, super.createReportParameters(context),
						DataSourceFactory
								.getDataSource(dataSourceName, context));

				request.getSession().setAttribute(
						ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE,
						jasperPrint);
				PrintWriter printWriter = response.getWriter();

				htmlExporter.setParameter(JRExporterParameter.JASPER_PRINT,
						jasperPrint);
				htmlExporter.setParameter(JRExporterParameter.OUTPUT_WRITER,
						printWriter);
				htmlExporter.setParameter(JRHtmlExporterParameter.IMAGES_URI,
						"image?image=");

				htmlExporter.exportReport();

				printWriter.flush();
				printWriter.close();
			}

		} catch (Exception e) {
			try {
				// display stack trace in the browser
				StringWriter stringWriter = new StringWriter();
				PrintWriter printWriter = new PrintWriter(stringWriter);
				e.printStackTrace(printWriter);
				response.setContentType("text/plain");
				response.getOutputStream().print(stringWriter.toString());
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}

		}
	}
}
