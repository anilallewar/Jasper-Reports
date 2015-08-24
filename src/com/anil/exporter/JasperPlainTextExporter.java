package com.anil.exporter;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRTextExporter;
import net.sf.jasperreports.engine.export.JRTextExporterParameter;

import com.anil.manager.JasperExportHelper;
import com.anil.manager.JasperReportManager;

public class JasperPlainTextExporter {

	public static void exportToPlainText(InputStream inStream,
			ServletOutputStream outStream, ServletContext context,
			HashMap<String, Object> parameters, Connection connection)
			throws JRException, IOException {

		JasperPrint jasperPrint = JasperFillManager.fillReport(inStream,
				parameters, connection);

		JRTextExporter textExporter = new JRTextExporter();

		textExporter
				.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		textExporter.setParameter(JRTextExporterParameter.CHARACTER_WIDTH,
				new Float(10));
		textExporter.setParameter(JRTextExporterParameter.CHARACTER_HEIGHT,
				new Float(10));
		/*
		 * Note that only HTML, PDF and XML can be exported directly to
		 * ServletOutputStream, for all other formats we need to create the file
		 * on server, push it to output and then delete the temporarily created
		 * file.
		 */
		String reportsDirPath = context.getRealPath("/"
				+ JasperExportHelper.REPORT_OUTPUT_DIRECTORY + "/");

		String randomUUId = UUID.randomUUID().toString();

		String destFile = reportsDirPath + "/"
				+ JasperReportManager.REPORT_NAME + "_" + randomUUId + ".xls";

		textExporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,
				destFile);

		textExporter.exportReport();

		File outputFile = new File(destFile);

		BufferedInputStream buffin = new BufferedInputStream(
				new FileInputStream(outputFile));

		byte[] buffer = new byte[1024];

		while (buffin.read(buffer) != -1) {
			outStream.write(buffer);
		}

		buffin.close();
		buffin = null;

		outputFile.delete();

		return;
	}

	public static void exportToPlainText(InputStream inStream,
			ServletOutputStream outStream, ServletContext context,
			HashMap<String, Object> parameters, JRDataSource dataSource)
			throws JRException, IOException {

		JasperPrint jasperPrint = JasperFillManager.fillReport(inStream,
				parameters, dataSource);

		JRTextExporter textExporter = new JRTextExporter();

		textExporter
				.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		textExporter.setParameter(JRTextExporterParameter.CHARACTER_WIDTH,
				new Float(10));
		textExporter.setParameter(JRTextExporterParameter.CHARACTER_HEIGHT,
				new Float(10));
		/*
		 * Note that only HTML, PDF and XML can be exported directly to
		 * ServletOutputStream, for all other formats we need to create the file
		 * on server, push it to output and then delete the temporarily created
		 * file.
		 */
		String reportsDirPath = context.getRealPath("/"
				+ JasperExportHelper.REPORT_OUTPUT_DIRECTORY + "/");

		String randomUUId = UUID.randomUUID().toString();

		String destFile = reportsDirPath + "/"
				+ JasperReportManager.REPORT_NAME + "_" + randomUUId + ".xls";

		textExporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,
				destFile);

		textExporter.exportReport();

		File outputFile = new File(destFile);

		BufferedInputStream buffin = new BufferedInputStream(
				new FileInputStream(outputFile));

		byte[] buffer = new byte[1024];

		while (buffin.read(buffer) != -1) {
			outStream.write(buffer);
		}

		buffin.close();
		buffin = null;

		outputFile.delete();

		return;
	}
}
