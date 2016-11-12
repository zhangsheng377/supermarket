package sqlexcute;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlExcute {
	private static Connection connection;

	public SqlExcute(String database) {
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:" + database);
			System.out.println("Opened database : " + database + " successfully");
		} catch (ClassNotFoundException | SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

	public SqlExcute(Connection conn) {
		connection = conn;
	}

	public Connection getConnection() {
		return connection;
	}

	public void close() {
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

	public ResultSet sql_select_execute(String sql) {
		Statement stmt;
		ResultSet rs;
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			System.out.println("sql_select_execute " + sql + " : error : " + e);
			return null;
		}
		return rs;
	}

	public boolean sql_execute(String sql) {
		Statement stmt;
		try {
			stmt = connection.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (SQLException e) {
			System.out.println("sql_execute " + sql + " : error : " + e);
			return false;
		}
		return true;
	}
}
