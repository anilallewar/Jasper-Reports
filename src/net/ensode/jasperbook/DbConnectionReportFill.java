package net.ensode.jasperbook;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.fill.JRFileVirtualizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbConnectionReportFill {

	private static Logger logger = LoggerFactory
			.getLogger(DbConnectionReportFill.class);
	Connection connection;

	public void generateReport(String reportName) {
		String reportDirectory = "reports";
		JRFileVirtualizer fileVirtualizer = new JRFileVirtualizer(3, "cacheDir");
		HashMap parameterMap = new HashMap();
		parameterMap.put(JRParameter.REPORT_VIRTUALIZER, fileVirtualizer);

		try {
			Class.forName("com.mysql.jdbc.Driver");

			connection = DriverManager
					.getConnection("jdbc:mysql://localhost:3306/flightstats?user=root&password=password");

			logger.debug("Filling report...");
			JasperFillManager.fillReportToFile(reportDirectory + "/"
					+ reportName + ".jasper", parameterMap, connection);
			logger.debug("Exporting to PDF...");
			JasperExportManager.exportReportToPdfFile(reportDirectory + "/"
					+ reportName + ".jrprint");
			logger.debug("Done!");
			logger.debug("Exporting to HTML...");
			JasperExportManager.exportReportToHtmlFile(reportDirectory + "/"
					+ reportName + ".jrprint");
			logger.debug("Done!");

			connection.close();
		} catch (JRException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new DbConnectionReportFill().generateReport(args[0]);
	}
}
