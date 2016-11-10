package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements ServerInfo{
	private static ServerSocket serverSocket;
	public Server(Integer port){
		try {
			serverSocket=new ServerSocket(port);
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
	}
	
	public class ClientSocket extends Thread {
		private Socket clientSocket;

		public ClientSocket(Socket s) {
			clientSocket = s;
		}

		public void run() {
			BufferedReader iBufferedReader = null;
			PrintWriter oPrintWriter = null;
			try {
				iBufferedReader = new BufferedReader(
						new InputStreamReader(clientSocket.getInputStream()));
				oPrintWriter = new PrintWriter(clientSocket.getOutputStream());
				while (true) {
					String string =iBufferedReader.readLine();
					if(string!=null){
						System.out.println(string);
						oPrintWriter.println("server received : "+string);
						oPrintWriter.flush();
					}
				}
			} catch (IOException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
				try {
					//System.out.println("clientSocket's isClosed : "+clientSocket.isClosed());
					iBufferedReader.close();
					oPrintWriter.close();
					clientSocket.close();
					System.out.println("clientSocket closed");
				} catch (IOException e1) {
					// TODO �Զ����ɵ� catch ��
					e1.printStackTrace();
				}
			}

		}
	}

	public static void main(String[] args) {
		// TODO �Զ����ɵķ������
		Server server=new Server(serverPort);
		while (true) {
			ClientSocket clientSocket = null;
			try {
				clientSocket = server.new ClientSocket(serverSocket.accept());
			} catch (IOException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
			clientSocket.start();
		}
	}
}
