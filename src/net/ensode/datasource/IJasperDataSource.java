package net.ensode.datasource;

import javax.servlet.ServletContext;

import net.sf.jasperreports.engine.JRDataSource;

public interface IJasperDataSource {
	public JRDataSource createReportDataSource(ServletContext context);
}
