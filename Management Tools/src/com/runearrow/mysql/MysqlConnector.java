package com.runearrow.mysql;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MysqlConnector {

	private static ThreadLocal<Connection> connectionThread = new ThreadLocalConnection();
	static String server = "localhost";
	static String dataBaseName = "rs";
	private static String url = "jdbc:mysql://" + server + "/" + dataBaseName;
	private static String userName = "root";
	private static String passWord = "3Dd51io9";

	public static Connection getConnection() {

		return ((Connection) connectionThread.get());
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