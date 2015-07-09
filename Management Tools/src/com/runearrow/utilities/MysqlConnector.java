package com.runearrow.utilities;

import com.mysql.jdbc.Connection;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MysqlConnector {

	private static ThreadLocal<Connection> connectionThread = new ThreadLocalConnection();
	static String server = "localhost";
	static String dataBaseName = "rs";
	private static String url = "jdbc:mysql://" + server + "/" + dataBaseName;
	private static String userName = "root";
	private static String passWord = getMysqlPassword();

	public static Connection getConnection() {

		return ((Connection) connectionThread.get());
	}

	private static String getMysqlPassword() {
		try (BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.home") + "/rootpass.dat"))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	return line;
		    }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static void release() throws SQLException {

		((Connection) connectionThread.get()).close();
		connectionThread.remove();
	}

	private static class ThreadLocalConnection extends ThreadLocal<Connection> {

		static {
			try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

		protected Connection initialValue() {

			return getConnection();
		}

		private Connection getConnection() {

			DriverManager.setLoginTimeout(15);
			try {
				return ((Connection) DriverManager.getConnection(MysqlConnector.url, MysqlConnector.userName, MysqlConnector.passWord));
			} catch (SQLException sql) {
				sql.printStackTrace();
			}
			return null;
		}

		public Connection get() {

			Connection con = super.get();
			try {
				if (!con.isClosed()) {
					return con;
				}
			} catch (SQLException sql) {
			}
			con = getConnection();
			super.set(con);
			return con;
		}
	}
}