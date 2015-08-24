package net.ensode.datasource;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.ServletContext;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class JasperBeanCollectionDS implements IJasperDataSource {

	public JRDataSource createReportDataSource(ServletContext context) {
		JRBeanCollectionDataSource dataSource;
		Collection<AircraftData> reportRows = initializeBeanCollection();

		dataSource = new JRBeanCollectionDataSource(reportRows);

		return dataSource;
	}

	public JRDataSource createReportDataSource() {
		JRBeanCollectionDataSource dataSource;
		Collection<AircraftData> reportRows = initializeBeanCollection();

		dataSource = new JRBeanCollectionDataSource(reportRows);

		return dataSource;
	}
	
	private Collection<AircraftData> initializeBeanCollection() {
		ArrayList<AircraftData> reportRows = new ArrayList<AircraftData>();

		AircraftData aircraft = new AircraftData("N263Y", "T-11",
				"39 ROSCOE TRNR RACER", "R1830 SERIES");

		AircraftTail tail = new AircraftTail(10, 12);
		AircraftWing wing = new AircraftWing(11, 13,"5", "Carbon");

		ArrayList<IAircraftComponent> components = new ArrayList<IAircraftComponent>();
		components.add(tail);
		components.add(wing);

		aircraft.setAircraftComponents(components);

		reportRows.add(aircraft);

		aircraft = new AircraftData("N4087X", "BA100-163", "BRADLEY AEROBAT",
				"R2800 SERIES");

		tail = new AircraftTail(20, 24);
		wing = new AircraftWing(21, 25,"10", "Composite");

		components = new ArrayList<IAircraftComponent>();
		components.add(tail);
		components.add(wing);

		aircraft.setAircraftComponents(components);

		reportRows.add(aircraft);

		aircraft = new AircraftData("N43JE", "HAYABUSA 1",
				"NAKAJIMA KI-43 IIIA", "R1830 SERIES");

		tail = new AircraftTail(30, 36);
		wing = new AircraftWing(31, 37,"15", "Titanium");

		components = new ArrayList<IAircraftComponent>();
		components.add(tail);
		components.add(wing);

		aircraft.setAircraftComponents(components);

		reportRows.add(aircraft);

		aircraft = new AircraftData("N912S", "9973CC", "PA18-150", "R-1820 SER");

		tail = new AircraftTail(40, 48);
		wing = new AircraftWing(41, 49,"20", "Steel");

		components = new ArrayList<IAircraftComponent>();
		components.add(tail);
		components.add(wing);

		aircraft.setAircraftComponents(components);

		reportRows.add(aircraft);

		return reportRows;
	}
}