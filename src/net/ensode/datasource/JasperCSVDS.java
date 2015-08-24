package net.ensode.datasource;

import java.io.InputStreamReader;

import javax.servlet.ServletContext;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRCsvDataSource;

public class JasperCSVDS implements IJasperDataSource {

	public JRDataSource createReportDataSource(ServletContext context) {

		try {
			JRCsvDataSource jRCsvDataSource = new JRCsvDataSource(
					new InputStreamReader(context
							.getResourceAsStream("/reports/AircraftData.csv")));
			jRCsvDataSource.setUseFirstRowAsHeader(true);
			return jRCsvDataSource;
		} catch (Exception fnfe) {
			fnfe.printStackTrace();
			return new JREmptyDataSource();
		}

	}
}
