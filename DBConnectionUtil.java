package com.shop.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


//Singelton design pattern
public class DBConnectionUtil extends CommonUtil {
	private static Connection connection;

	private DBConnectionUtil() {

	}
	
	/**
	 * Create Database connection for the given URL, Username and Password
	 * 
	 * @return Connection this returns SQL connection for MySql Database
	 * 
	 * @throws ClassNotFoundException
	 *             - Thrown when an application tries to load in a class through
	 *             its string name
	 * @throws SQLException
	 *             - An exception that provides information on a database access
	 *             error or other errors
	 * @throws IOException 
	 */

	public static Connection getDBConnection() throws SQLException, ClassNotFoundException, IOException {
		/*
		 * This create new connection objects when connection is closed or it is
		 * null
		 */
		properties.load(DBConnectionUtil.class.getResourceAsStream(Constants.PROPERTY_FILE));
		
		if (connection == null || connection.isClosed()) {
			Class.forName(properties.getProperty(Constants.DRIVER_NAME));
			connection = DriverManager.getConnection(properties.getProperty(Constants.URL),
					properties.getProperty(Constants.USERNAME), properties.getProperty(Constants.PASSWORD));
		}
		return connection;
	}

}
