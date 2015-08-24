package com.anil.exporter;

import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;

import javax.servlet.ServletOutputStream;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

public class JasperXMLExporter {

	public static void exportToXML(InputStream inStream,
			ServletOutputStream outStream, HashMap<String, Object> parameters,
			Connection connection) throws JRException {

		JasperPrint jasperPrint = JasperFillManager.fillReport(inStream,
				parameters, connection);

		JasperExportManager.exportReportToXmlStream(jasperPrint, outStream);

		return;
	}

	public static void exportToXML(InputStream inStream,
			ServletOutputStream outStream, HashMap<String, Object> parameters,
			JRDataSource dataSource) throws JRException {

		JasperPrint jasperPrint = JasperFillManager.fillReport(inStream,
				parameters, dataSource);

		JasperExportManager.exportReportToXmlStream(jasperPrint, outStream);

		return;
	}
}
