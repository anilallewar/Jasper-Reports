package com.anil.manager;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;

import com.anil.exporter.JasperCSVExporter;
import com.anil.exporter.JasperPDFExporter;
import com.anil.exporter.JasperPlainTextExporter;
import com.anil.exporter.JasperRTFExporter;
import com.anil.exporter.JasperXLSExporter;
import com.anil.exporter.JasperXMLExporter;

public class JasperExportHelper {
	private final static String PDF_FORMAT = "pdf";
	private final static String XLS_FORMAT = "xls";
	private final static String PLAIN_TEX_FORMAT = "txt";
	private final static String RTF_FORMAT = "rtf";
	private final static String CSV_FORMAT = "csv";
	private final static String XML_FORMAT = "xml";

	public static final String REPORT_OUTPUT_DIRECTORY = "tmp";

	/**
	 * This helper method is used to export the report the various formats to the passed
	 * ServletOutputStream. This method is used to populate the reports that require a 
	 * database connection to be passed to the Jasper report for the report query to be 
	 * executed by the report. The supported formats are <br>
	 * <ol>
	 * 		<li>PDF</li>
	 * 		<li>XLS</li>
	 * 		<li>Plain Text</li>
	 * 		<li>RTF</li>
	 * 		<li>CSV</li>
	 * 		<li>XML</li>
	 * <ol>
	 * 
	 * @param reportFormat - The report format to which you want to export
	 * @param inStream - The InputStream containing the report definition .jasper file
	 * @param outStream - The OutputStream to which you want to export the report
	 * @param context - The ServletCOntext object required to access web app resources
	 * @param parameters - The HashMap containing the report parameters
	 * @param connection - The Connection object utilized to execute queries by Jasper Report Engine
	 * @throws JRException - Thrown when any Jasper methods encounter error 
	 * @throws IOException - Thrown when there is any problem in disk IO((mainly file) operations 
	 */
	public static void exportReport(String reportFormat, InputStream inStream,
			ServletOutputStream outStream, ServletContext context,
			HashMap<String, Object> parameters, Connection connection)
			throws JRException, IOException {
		if (PDF_FORMAT.equalsIgnoreCase(reportFormat)) {
			JasperPDFExporter.exportToPDF(inStream, outStream, parameters,
					connection);
		} else if (XLS_FORMAT.equalsIgnoreCase(reportFormat)) {
			JasperXLSExporter.exportToXLS(inStream, outStream, context,
					parameters, connection);
		} else if (PLAIN_TEX_FORMAT.equalsIgnoreCase(reportFormat)) {
			JasperPlainTextExporter.exportToPlainText(inStream, outStream,
					context, parameters, connection);
		} else if (RTF_FORMAT.equalsIgnoreCase(reportFormat)) {
			JasperRTFExporter.exportToRTF(inStream, outStream, context,
					parameters, connection);
		} else if (CSV_FORMAT.equalsIgnoreCase(reportFormat)) {
			JasperCSVExporter.exportToCSV(inStream, outStream, context,
					parameters, connection);
		} else if (XML_FORMAT.equalsIgnoreCase(reportFormat)) {
			JasperXMLExporter.exportToXML(inStream, outStream, parameters,
					connection);
		}

	}

	/**
	 * This helper method is used to export the report the various formats to the passed
	 * ServletOutputStream. This overloaded method is used to populate the reports that require a 
	 * datasource that has been populated by external data. These reports will NOT use an internal SQL 
	 * query to populate the report. The supported formats are <br>
	 * <ol>
	 * 		<li>PDF</li>
	 * 		<li>XLS</li>
	 * 		<li>Plain Text</li>
	 * 		<li>RTF</li>
	 * 		<li>CSV</li>
	 * 		<li>XML</li>
	 * <ol>
	 * 
	 * @param reportFormat - The report format to which you want to export
	 * @param inStream - The InputStream containing the report definition .jasper file
	 * @param outStream - The OutputStream to which you want to export the report
	 * @param context - The ServletCOntext object required to access web app resources
	 * @param parameters - The HashMap containing the report parameters
	 * @param dataSource - The JRDataSource object utilized by Jasper for filling the report
	 * @throws JRException - Thrown when any Jasper methods encounter error 
	 * @throws IOException - Thrown when there is any problem in disk IO((mainly file) operations 
	 */
	public static void exportReport(String reportFormat, InputStream inStream,
			ServletOutputStream outStream, ServletContext context,
			HashMap<String, Object> parameters, JRDataSource dataSource)
			throws JRException, IOException {
		if (PDF_FORMAT.equalsIgnoreCase(reportFormat)) {
			JasperPDFExporter.exportToPDF(inStream, outStream, parameters,
					dataSource);
		} else if (XLS_FORMAT.equalsIgnoreCase(reportFormat)) {
			JasperXLSExporter.exportToXLS(inStream, outStream, context,
					parameters, dataSource);
		} else if (PLAIN_TEX_FORMAT.equalsIgnoreCase(reportFormat)) {
			JasperPlainTextExporter.exportToPlainText(inStream, outStream,
					context, parameters, dataSource);
		} else if (RTF_FORMAT.equalsIgnoreCase(reportFormat)) {
			JasperRTFExporter.exportToRTF(inStream, outStream, context,
					parameters, dataSource);
		} else if (CSV_FORMAT.equalsIgnoreCase(reportFormat)) {
			JasperCSVExporter.exportToCSV(inStream, outStream, context,
					parameters, dataSource);
		} else if (XML_FORMAT.equalsIgnoreCase(reportFormat)) {
			JasperXMLExporter.exportToXML(inStream, outStream, parameters,
					dataSource);
		}
	}
}
