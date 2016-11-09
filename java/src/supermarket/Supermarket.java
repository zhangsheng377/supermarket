package supermarket;

import java.sql.*;

public class Supermarket {
	private static Connection connection = null;

	public static void init() {
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:test.db");
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Opened database successfully");
	}

	public static ResultSet sql_select_execute(String sql) {
		Statement stmt;
		ResultSet rs;
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return null;
		}
		return rs;
	}

	public static boolean sql_execute(String sql) {
		Statement stmt;
		try {
			stmt = connection.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static void main(String[] args) {
		init();

		Login login_window = new Login();
		login_window.setVisible(true);
		String name = login_window.getName();
		String password = login_window.getPassword();
		System.out.println("name = " + name);
		System.out.println("password = " + password);

		String sql;
		// sql = "CREATE TABLE PEOPLE (NAME INT PRIMARY KEY NOT NULL, PASSWORD
		// TEXT NOT NULL)";
		// System.out.println("sql creat table = " + sql_execute(sql));
		// sql = "INSERT INTO PEOPLE (NAME,PASSWORD) VALUES ('abc', '1qa');";
		// System.out.println("sql insert table = " + sql_execute(sql));

		try {
			sql = "select count(*) from people where name='" + name + "' and password='" + password + "'";
			System.out.println(sql_select_execute(sql).getString(1));
		} catch (SQLException e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		}

		try {
			connection.close();
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			// e.printStackTrace();
			System.exit(0);
		}
	}
}