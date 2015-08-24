package net.ensode.datasource;

import javax.servlet.ServletContext;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

public class JasperBeanArrayDS implements IJasperDataSource {

	public JRDataSource createReportDataSource(ServletContext context) {
		JRBeanArrayDataSource dataSource;
		AircraftData[] reportRows = initializeBeanArray();

		dataSource = new JRBeanArrayDataSource(reportRows);

		return dataSource;
	}

	private AircraftData[] initializeBeanArray() {
		AircraftData[] reportRows = new AircraftData[4];

		reportRows[0] = new AircraftData("N263Y", "T-11",
				"39 ROSCOE TRNR RACER", "R1830 SERIES");
		reportRows[1] = new AircraftData("N4087X", "BA100-163",
				"BRADLEY AEROBAT", "R2800 SERIES");
		reportRows[2] = new AircraftData("N43JE", "HAYABUSA 1",
				"NAKAJIMA KI-43 IIIA", "R1830 SERIES");
		reportRows[3] = new AircraftData("N912S", "9973CC", "PA18-150",
				"R-1820 SER");

		return reportRows;
	}
}