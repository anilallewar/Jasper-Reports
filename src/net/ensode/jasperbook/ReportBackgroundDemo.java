package net.ensode.jasperbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReportBackgroundDemo {
	private static Logger logger = LoggerFactory
			.getLogger(ReportBackgroundDemo.class);

	public void generateReport() {
		try {
			// This method creates the Jasper report and stored it the .jasper
			// file folder
			// as a .jrprint file in the same folder with the same name. The
			// .jrprint file
			// is the serialized version of the JasperPrint object created as a
			// result of
			// filling the report
			JasperFillManager.fillReportToFile(
					"reports/BackgroundDemoReport.jasper", null,
					new JREmptyDataSource());

			FileInputStream fis = new FileInputStream(
					"reports/BackgroundDemoReport.jrprint");
			logger.debug("Reading the report object from file streem ...");
			ObjectInputStream objectInputStream = new ObjectInputStream(fis);
			JasperPrint constructedReport = (JasperPrint) objectInputStream
					.readObject();

			// Create the internal jasper reports viewer
			JasperViewer jsViewer = new JasperViewer(constructedReport);
			jsViewer.setVisible(true);

			objectInputStream.close();
			fis.close();
		} catch (JRException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new ReportBackgroundDemo().generateReport();

	}

}
