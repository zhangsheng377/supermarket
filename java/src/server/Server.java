package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import sqlexcute.SqlExcute;

public class Server implements ServerInfo {
	private static ServerSocket serverSocket;
	private SqlExcute sqlExcute;
	private String database = "test.db";
	private Map<String, PrintWriter> clientsMap = new HashMap<String, PrintWriter>();

	public Server(Integer port) {
		try {
			serverSocket = new ServerSocket(port);
			sqlExcute = new SqlExcute(database);
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public class ClientSocket extends Thread {
		private Socket clientSocket;
		private String name;

		public ClientSocket(Socket s) {
			clientSocket = s;
		}

		public void run() {
			BufferedReader iBufferedReader = null;
			PrintWriter oPrintWriter = null;
			try {
				iBufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				oPrintWriter = new PrintWriter(clientSocket.getOutputStream());
				while (true) {
					String string = iBufferedReader.readLine().trim();
					if (string != null) {
						System.out.println(string);
						String[] strings = string.split(" ");
						switch (strings[0]) {
						case "login":
							String sql = "select count(*) from people where name='" + strings[1] + "' and password='"
									+ strings[2] + "'";
							String result = null;
							try {
								result = sqlExcute.sql_select_execute(sql).getString(1);
							} catch (SQLException e) {
								System.out.println(e);
							}
							if (result == null || !result.equals("1")) {
								oPrintWriter.println("login false");
								oPrintWriter.flush();
							} else {
								oPrintWriter.println("login true");
								oPrintWriter.flush();
								clientsMap.put(strings[1], oPrintWriter);
								name = strings[1];
							}
							break;
						case "message":
							String temp_string = strings[0] + " " + name;
							for (int i = 2; i < strings.length; i++) {
								temp_string = temp_string + " " + strings[i];
							}
							PrintWriter targetPrintWriter = clientsMap.get(strings[1]);
							if (targetPrintWriter != null) {
								System.out.println("temp_string = " + temp_string);
								targetPrintWriter.println(temp_string);
								targetPrintWriter.flush();
							} else {
								// 未上线
							}
							break;

						default:
							break;
						}
					}
				}
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
				try {
					// System.out.println("clientSocket's isClosed :
					// "+clientSocket.isClosed());
					iBufferedReader.close();
					oPrintWriter.close();
					clientSocket.close();
					System.out.println("clientSocket closed");
				} catch (IOException e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				}
			}

		}
	}

	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		Server server = new Server(serverPort);
		while (true) {
			try {
				server.new ClientSocket(serverSocket.accept()).start();
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
	}
}
