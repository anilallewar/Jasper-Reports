package net.ensode.datasource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.servlet.ServletContext;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

public class JasperMapCollectionDS implements IJasperDataSource {

	public JRDataSource createReportDataSource(ServletContext context) {
		JRMapCollectionDataSource dataSource;
		Collection reportRows = initializeMapCollection();

		dataSource = new JRMapCollectionDataSource(reportRows);

		return dataSource;
	}

	private Collection initializeMapCollection() {
		ArrayList reportRows = new ArrayList();
		HashMap<String, String> row1Map = new HashMap<String, String>();
		HashMap<String, String> row2Map = new HashMap<String, String>();
		HashMap<String, String> row3Map = new HashMap<String, String>();
		HashMap<String, String> row4Map = new HashMap<String, String>();

		row1Map.put("tail_num", "N263Y");
		row1Map.put("aircraft_serial", "T-11");
		row1Map.put("aircraft_model", "39 ROSCOE TRNR RACER");
		row1Map.put("engine_model", "R1830 SERIES");

		row2Map.put("tail_num", "N4087X");
		row2Map.put("aircraft_serial", "BA100-163");
		row2Map.put("aircraft_model", "BRADLEY AEROBAT");
		row2Map.put("engine_model", "R2800 SERIES");

		row3Map.put("tail_num", "N43JE");
		row3Map.put("aircraft_serial", "HAYABUSA 1");
		row3Map.put("aircraft_model", "NAKAJIMA KI-43 IIIA");
		row3Map.put("engine_model", "R1830 SERIES");

		row4Map.put("tail_num", "N912S");
		row4Map.put("aircraft_serial", "9973CC");
		row4Map.put("aircraft_model", "PA18-150");
		row4Map.put("engine_model", "R-1820 SER");

		reportRows.add(row1Map);
		reportRows.add(row2Map);
		reportRows.add(row3Map);
		reportRows.add(row4Map);

		return reportRows;
	}
}