package com.anil.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionFactory {
	
	private static Logger logger = LoggerFactory
			.getLogger(ConnectionFactory.class);
	
	public static final String DBO_DATABASE = "dbo";
	public static final String FLIGHTSTATS_DATABASE = "flightstats";
	private static final String DB_USER = "root";
	private static final String DB_PASSWORD = "password";
	private static final String DB_PROTOCOL_HOST = "jdbc:mysql://localhost:3306/";

	/**
	 * Private constructor so that all the objects are available through static
	 * methods only
	 */
	private ConnectionFactory() {
		// Do Nothing
	}

	/**
	 * This factory method will return a mySQL connection to the calling method
	 * and to the database whose name is passed to the method. >br>
	 * 
	 * It is the responsibilty of the calling method to use and dispose of the
	 * connection; this class does provide several utility methods for disposal
	 * 
	 * @param database
	 *            - The database to which connection is required
	 * @return Connection
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public static Connection getConnection(String database)
			throws SQLException, ClassNotFoundException {
		if (!(DBO_DATABASE.equalsIgnoreCase(database) || FLIGHTSTATS_DATABASE
				.equalsIgnoreCase(database))) {
			throw new SQLException(
					"The passed database does not exist or is not supported by the application.");
		}

		Class.forName("com.mysql.jdbc.Driver");

		Connection connection = DriverManager.getConnection(DB_PROTOCOL_HOST
				+ database + "?user=" + DB_USER + "&password=" + DB_PASSWORD);

		return connection;
	}

	/**
	 * This method will close the passed connection and set it to null
	 * 
	 * @param connection
	 * @throws SQLException
	 */
	public static void closeConnection(Connection connection)
			throws SQLException {
		connection.close();
		/*
		 * There is no point in setting the connection to null as the original
		 * reference to the Connection remains in the calling program.
		 * 
		 * Remember that Objects are passed by reference so you can change the
		 * behaviour by calling methods on the object. But the reference by
		 * itself is passed by value; so a copy of the reference gets passed to
		 * this program. If you set that reference to null only the passed
		 * reference is set to null whereas the original reference in the
		 * calling program still keeps pointing to the actual object. :)
		 */
		
		// connection = null;
	}

	/**
	 * This method will first close the passed connection(also closing any
	 * associated result sets) and then close the passed connection
	 * 
	 * @param statement
	 * @param connection
	 * @throws SQLException
	 */
	public static void closeStatementAndConnection(Statement statement,
			Connection connection) throws SQLException {
		statement.close();
		connection.close();
	}

	/**
	 * Testing purposes only
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Connection con = ConnectionFactory
					.getConnection(ConnectionFactory.DBO_DATABASE);
			logger.debug("Got connection to: "
					+ ConnectionFactory.DBO_DATABASE + " :" + con);
			con = ConnectionFactory
					.getConnection(ConnectionFactory.FLIGHTSTATS_DATABASE);
			logger.debug("Got connection to: "
					+ ConnectionFactory.FLIGHTSTATS_DATABASE + " :" + con);
			ConnectionFactory.closeConnection(con);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
