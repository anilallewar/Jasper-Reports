package net.ensode.datasource;

import javax.servlet.ServletContext;
import javax.swing.table.DefaultTableModel;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;

public class TableModelReport implements IJasperDataSource {

	DefaultTableModel tableModel;

	private void populateTableModel() {
		String[] columnNames = { "tail_num", "aircraft_serial",
				"aircraft_model", "engine_model" };
		String[][] data = {
				{ "N263Y", "T-11", " 39 ROSCOE TRNR RACER", "R1830 SERIES" },
				{ "N4087X", "BA100-163", "BRADLEY AEROBAT", "R2800 SERIES" },
				{ "N43JE", "HAYABUSA 1", "NAKAJIMA KI-43 IIIA", "R1830 SERIES" },
				{ "N912S", "9973CC", "PA18-150", "R-1820 SER" } };

		this.tableModel = new DefaultTableModel(data, columnNames);
	}

	@Override
	public JRDataSource createReportDataSource(ServletContext context) {
		populateTableModel();
		return new JRTableModelDataSource(this.tableModel);
	}
}
