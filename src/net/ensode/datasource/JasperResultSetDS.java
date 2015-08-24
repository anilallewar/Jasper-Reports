/**
 * 
 */
package net.ensode.datasource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletContext;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRResultSetDataSource;

import com.anil.dao.ConnectionFactory;

/**
 * @author anila
 * 
 */
public class JasperResultSetDS implements IJasperDataSource {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.ensode.datasource.IJasperDataSource#createReportDataSource(javax.
	 * servlet.ServletContext)
	 */
	@Override
	public JRDataSource createReportDataSource(ServletContext context) {

		JRDataSource resultSetDataSource = null;
		Connection connection = null;
		try {

			connection = ConnectionFactory
					.getConnection(ConnectionFactory.FLIGHTSTATS_DATABASE);

			String query = "select a.tail_num, a.aircraft_serial, "
					+ "am.model as aircraft_model, ae.model as engine_model from aircraft a, "
					+ "aircraft_models am, aircraft_engines ae where a.aircraft_engine_code in ("
					+ "select aircraft_engine_code from aircraft_engines "
					+ "where horsepower >= 1000) and am.aircraft_model_code = a.aircraft_model_code "
					+ "and ae.aircraft_engine_code = a.aircraft_engine_code";

			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);

			resultSetDataSource = new JRResultSetDataSource(resultSet);

		} catch (Exception e) {
			e.printStackTrace();
			resultSetDataSource = new JREmptyDataSource();
		} finally {
			try {
				if (connection !=null){
					ConnectionFactory.closeConnection(connection);
				}
			} catch (SQLException sqe) {
				sqe.printStackTrace();
				resultSetDataSource = new JREmptyDataSource();
			}
		}

		return resultSetDataSource;
	}

}
