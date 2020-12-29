package com.hotmail.abechanta.tetcon;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbAccess {
	private static final String dbDriver = "org.apache.derby.jdbc.ClientDriver";
	private static final String dbName = "localdb";
	private static final String dbHost = "//localhost:1527";
	private static final String dbConn = "jdbc:derby:" + dbHost + "/" + dbName + ";create=true";
	private static final String dbUser = "local";
	private static final String dbPasswd = "abeabe";
	private static Connection conn = null;

	public static Connection getConn(boolean bAutoCommit) throws SQLException {
		if (conn == null) {
			try {
				Class.forName(dbDriver);
				Connection tmpConn = null;
				tmpConn = DriverManager.getConnection(dbConn, dbUser, dbPasswd);
				conn = tmpConn;
			} catch (ClassNotFoundException ex) {
				ex.printStackTrace();
				throw new SQLException("db connection not found.");
			}
		}
		conn.setAutoCommit(bAutoCommit);
		return conn;
	}

	public static void destroy() {
		if (conn != null) {
			try {
				conn.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			conn = null;
		}
	}

}
