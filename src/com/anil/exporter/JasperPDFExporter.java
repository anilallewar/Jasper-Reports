package com.anil.exporter;

import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;

import javax.servlet.ServletOutputStream;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;

public class JasperPDFExporter {
	
	public static void exportToPDF(InputStream inStream,  ServletOutputStream outStream, HashMap<String, Object> parameters, Connection connection) throws JRException{
		JasperRunManager.runReportToPdfStream(inStream,
				outStream, parameters,
				connection);
		return;
	}
	
	public static void exportToPDF(InputStream inStream,  ServletOutputStream outStream, HashMap<String, Object> parameters, JRDataSource dataSource) throws JRException{
		JasperRunManager.runReportToPdfStream(inStream,
				outStream, parameters,
				dataSource);
		return;
	}
}
