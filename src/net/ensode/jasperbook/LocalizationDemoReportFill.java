package net.ensode.jasperbook;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Locale;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalizationDemoReportFill {

	private static Logger logger = LoggerFactory
			.getLogger(LocalizationDemoReportFill.class);

	public static void main(String[] args) throws FileNotFoundException {
		try {
			/*
			 * A property file with a root name matching the value of the
			 * attribute must exist anywhere in the CLASSPATH when filling the
			 * report.
			 * 
			 * So add the reports folder in the classpath using the project
			 * build path properties for this to work.
			 */
			HashMap propertiesMap = new HashMap();

			if (args.length > 0) {
				propertiesMap.put(JRParameter.REPORT_LOCALE,
						new Locale(args[0]));
			}

			logger.debug("Filling report...");
			JasperPrint report = JasperFillManager.fillReport(
					"reports/LocalizationDemoReport.jasper", propertiesMap,
					new JREmptyDataSource());
			JasperViewer jsViewer = new JasperViewer(report);
			jsViewer.setVisible(true);
			logger.debug("Done!");
		} catch (JRException e) {
			e.printStackTrace();
		}
	}
}
