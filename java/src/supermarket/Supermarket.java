package supermarket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import server.ServerInfo;
import sqlexcute.SqlExcute;

public class Supermarket implements ServerInfo {
	private static SqlExcute sqlExcute;
	private static String database;
	private static Socket socket;
	private static BufferedReader iBufferedReader;
	private static PrintWriter oPrintWriter;
	private static String name;
	private static Map<String, ChatMessage> chatingMap_targetname_chatmessage = new HashMap<String, ChatMessage>();

	public class ReadSocket extends Thread {
		public void run() {
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
					String[] strings = string.split(" ");
					switch (strings[0]) {
					case "login":
						if (strings[1].equals("true")) {
							System.out.println("login true");
							database = name + ".db";
							sqlExcute = new SqlExcute(database);
							@SuppressWarnings("unused")
							MainJFrame mainJFrame = new MainJFrame(sqlExcute, name, oPrintWriter,
									chatingMap_targetname_chatmessage);
							// System.out.println("under mainJFrame");
						}
						break;

					case "message":
						String temp_string = "";
						for (int i = 2; i < strings.length; i++) {
							temp_string = temp_string + " " + strings[i];
						}
						temp_string = temp_string.trim();
						System.out.println(strings[1] + " says : " + temp_string);

						String tablenameString = "chat_" + strings[1];
						String sql = "select * from " + tablenameString;
						ResultSet resultSet = sqlExcute.sql_select_execute(sql);
						if (resultSet == null) {
							sql = "CREATE TABLE " + tablenameString + " (SPEAKER TEXT NOT NULL, CONTENT TEXT)";
							sqlExcute.sql_execute(sql);
							System.out.println("create table end");
						}
						sql = "INSERT INTO " + tablenameString + " (SPEAKER,CONTENT) VALUES ('" + strings[1] + "', '"
								+ temp_string + "')";
						sqlExcute.sql_execute(sql);

						ChatMessage chatMessage_window = chatingMap_targetname_chatmessage.get(strings[1]);
						if (chatMessage_window == null) {
							System.out.println("Supermarket chatMessage_window == null");
							chatMessage_window = new ChatMessage(sqlExcute, name, strings[1], oPrintWriter,
									chatingMap_targetname_chatmessage);
							chatingMap_targetname_chatmessage.put(strings[1], chatMessage_window);
						} else {
							System.out.println("Supermarket chatMessage_window isnot null");
							chatMessage_window.addText(strings[1] + " said : " + temp_string);
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
				System.out.println("ReadSocket end");
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		Supermarket supermarket = new Supermarket();

		Login login_window = new Login();
		login_window.setVisible(true);
		name = login_window.getName();
		String password = login_window.getPassword();
		System.out.println("name = " + name);
		System.out.println("password = " + password);

		try {
			socket = new Socket(serverHost, serverPort);
			iBufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			oPrintWriter = new PrintWriter(socket.getOutputStream());
			supermarket.new ReadSocket().start();

			oPrintWriter.println("login " + name + " " + password);
			oPrintWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// System.out.println("end");
		// sqlExcute.close();
		// iBufferedReader.close();
		// oPrintWriter.close();
		// socket.close();
	}
}