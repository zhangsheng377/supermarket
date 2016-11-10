package supermarket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.*;

import server.ServerInfo;

public class Supermarket implements ServerInfo {
	private static Connection connection = null;
	private static Socket socket;
	private static BufferedReader iBufferedReader;
	private static PrintWriter oPrintWriter;

	public Supermarket() {
		// TODO 自动生成的构造函数存根
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

	public class ReadSocket extends Thread {
		public void run() {
			System.out.println("ReadSocket run start");
			System.out.println("ReadSocket socket isClosed :" + socket.isClosed());
			while (true) {
				String string = null;
				try {
					if (iBufferedReader.ready()) {
						string = iBufferedReader.readLine();
					}
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
					break;
				}
				if (string != null) {
					System.out.println("ReadSocket recieved : " + string);
				}
			}
			System.out.println("ReadSocket while over");
			try {
				System.out.println("ReadSocket close");
				iBufferedReader.close();
				oPrintWriter.close();
				socket.close();
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				System.out.println("ReadSocket end");
				e.printStackTrace();
			}

		}
	}

	public static void main(String[] args) {
		Supermarket supermarket = new Supermarket();

		Login login_window = new Login();
		login_window.setVisible(true);
		String name = login_window.getName();
		String password = login_window.getPassword();
		System.out.println("name = " + name);
		System.out.println("password = " + password);

		String resultString = null;
		String sql;
		// sql = "CREATE TABLE PEOPLE (NAME INT PRIMARY KEY NOT NULL, PASSWORD
		// TEXT NOT NULL)";
		// System.out.println("sql creat table = " + sql_execute(sql));
		// sql = "INSERT INTO PEOPLE (NAME,PASSWORD) VALUES ('abc', '1qa');";
		// System.out.println("sql insert table = " + sql_execute(sql));

		try {
			sql = "select count(*) from people where name='" + name + "' and password='" + password + "'";
			resultString = sql_select_execute(sql).getString(1);
		} catch (SQLException e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		}
		System.out.println(resultString);
		if (resultString.equals("1")) {
			System.out.println("login successed");
			try {
				socket = new Socket(InetAddress.getLocalHost(), serverPort);
				iBufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				oPrintWriter = new PrintWriter(socket.getOutputStream());
				ReadSocket readSocket = supermarket.new ReadSocket();
				readSocket.start();

				oPrintWriter.println("hello from client");
				oPrintWriter.flush();
				oPrintWriter.println("hello from client");
				oPrintWriter.flush();
				System.out.println("oPrintWriter is over");

			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		} else {
			System.out.println("login failed");
		}

		try {
			System.out.println("end");
			connection.close();
			//iBufferedReader.close();
			//oPrintWriter.close();
			//socket.close();
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			System.exit(0);
		}
	}
}