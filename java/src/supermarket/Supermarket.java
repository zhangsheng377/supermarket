package supermarket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import server.ServerInfo;
import sqlexcute.SqlExcute;

public class Supermarket implements ServerInfo {
	private static SqlExcute sqlExcute;
	private static String database="test.db";
	private static Socket socket;
	private static BufferedReader iBufferedReader;
	private static PrintWriter oPrintWriter;

	public Supermarket(String database) {
		// TODO 自动生成的构造函数存根
		sqlExcute=new SqlExcute(database);
	}

	public class ReadSocket extends Thread {
		public void run() {
			System.out.println("ReadSocket run start");
			System.out.println("ReadSocket socket isClosed :" + socket.isClosed());
			while (true) {
				String string = null;
				try {
					if (iBufferedReader.ready()) {
						string = iBufferedReader.readLine().trim();
					}
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
					break;
				}
				if (string != null) {
					System.out.println("ReadSocket recieved : " + string);
					String[] strings=string.split(" ");
					switch (strings[0]) {
					case "login":
						if(strings[1].equals("true")){
							ChatMessage chatMessage_window=new ChatMessage(sqlExcute.getConnection());
							chatMessage_window.setVisible(true);
							System.out.println("login true");
						}
						break;

					default:
						break;
					}
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
		Supermarket supermarket = new Supermarket(database);

		Login login_window = new Login();
		login_window.setVisible(true);
		String name = login_window.getName();
		String password = login_window.getPassword();
		System.out.println("name = " + name);
		System.out.println("password = " + password);
		
		try {
			socket = new Socket(serverHost, serverPort);
			iBufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			oPrintWriter = new PrintWriter(socket.getOutputStream());
			supermarket.new ReadSocket().start();
			
			oPrintWriter.println("login "+name+" "+password);
			oPrintWriter.flush();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

		/*ResultSet resultSet = null;
		String sql;
		String resultString = null;
		// sql = "CREATE TABLE PEOPLE (NAME INT PRIMARY KEY NOT NULL, PASSWORD
		// TEXT NOT NULL)";
		// System.out.println("sql creat table = " + sql_execute(sql));
		// sql = "INSERT INTO PEOPLE (NAME,PASSWORD) VALUES ('abc', '1qa');";
		// System.out.println("sql insert table = " + sql_execute(sql));

		sql = "select count(*) from people where name='" + name + "' and password='" + password + "'";
		resultSet=sqlExcute.sql_select_execute(sql);
		if(resultSet!=null){
			try {
				resultString=resultSet.getString(1);
			} catch (SQLException e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
		}
		System.out.println(resultString);
		if (resultString.equals("1")) {
			System.out.println("login successed");
			try {
				socket = new Socket(serverHost, serverPort);
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
		}*/

		System.out.println("end");
		sqlExcute.close();
		//iBufferedReader.close();
		//oPrintWriter.close();
		//socket.close();
	}
}