package net.ensode.datasource;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import net.sf.jasperreports.engine.JRDataSource;

public class JasperCustomDataSource implements IJasperDataSource {

	public JRDataSource createReportDataSource(ServletContext context) {

		String[] headers = { "tail_num", "aircraft_serial", "aircraft_model",
				"engine_model" };
		ListOfArraysDataSource dataSource;
		List<String[]> reportRows = initializeListOfArrays();
		dataSource = new ListOfArraysDataSource(reportRows);

		dataSource.setFieldNames(headers);
		return dataSource;
	}

	private List<String[]> initializeListOfArrays() {
		List<String[]> reportRows = new ArrayList<String[]>();

		String[] row1 = { "N263Y", "T-11", "39 ROSCOE TRNR RACER",
				"R1830 SERIES" };
		String[] row2 = { "N4087X", "BA100-163", "BRADLEY AEROBAT",
				"R2800 SERIES" };
		String[] row3 = { "N43JE", "HAYABUSA 1", "NAKAJIMA KI-43 IIIA",
				"R1830 SERIES" };
		String[] row4 = { "N912S", "9973CC", "PA18-150", "R-1820 SER" };

		reportRows.add(row1);
		reportRows.add(row2);
		reportRows.add(row3);
		reportRows.add(row4);

		return reportRows;
	}
}