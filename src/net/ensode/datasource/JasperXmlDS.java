package net.ensode.datasource;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.servlet.ServletContext;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRXmlDataSource;

public class JasperXmlDS implements IJasperDataSource {

	public JRDataSource createReportDataSource(ServletContext context) {
		try {
			/*
			 * Passing an XPath expression is optional. If we don't pass one,
			 * the datasource will be created from all the sub-elements of the
			 * root element in the XML file. However, if we do pass one, then
			 * the datasource will be created from all the elements inside the
			 * XPath expression.
			 */
			JRXmlDataSource xmlDataSource = new JRXmlDataSource(
					new BufferedInputStream(context.getResourceAsStream("/reports/AircraftData.xml")),
					"/AircraftData/aircraft");
			return xmlDataSource;
		} catch (JRException jre) {
			jre.printStackTrace();
			return new JREmptyDataSource();
		}
	}
}