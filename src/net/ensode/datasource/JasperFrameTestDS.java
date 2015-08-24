package net.ensode.datasource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.servlet.ServletContext;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

public class JasperFrameTestDS implements IJasperDataSource {

	@Override
	public JRDataSource createReportDataSource(ServletContext context) {
		JRMapCollectionDataSource dataSource;
		Collection<HashMap<String, String>> reportRows = initializeMapCollection();

		dataSource = new JRMapCollectionDataSource(reportRows);

		return dataSource;
	}

	private Collection<HashMap<String, String>> initializeMapCollection() {
		ArrayList<HashMap<String, String>> reportRows = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> datasourceMap = new HashMap<String, String>();

		datasourceMap
				.put(
						"lots_of_data",
						"This element contains so much data, "
								+ "there is no way it will ever fit in the text field without it stretching.");

		reportRows.add(datasourceMap);

		return reportRows;
	}

}
