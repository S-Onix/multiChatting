package multiChatting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatHandler extends Thread {
	private Socket s;
	private BufferedReader i;
	private PrintWriter o;
	private ChatServer server;

	public ChatHandler(ChatServer server, Socket s) throws IOException {
		this.s = s;
		this.server = server;
		InputStream ins = s.getInputStream();
		OutputStream os = s.getOutputStream();
		i = new BufferedReader(new InputStreamReader(ins));
		o = new PrintWriter(new OutputStreamWriter(os), true);
	}

	//프로토콜 처리하기
	/*
	 * 프로토콜 정의하기
	 * 
	 * */
	@Override
	public void run() {
		String name = "";
		try {
			name = i.readLine();
			server.register(this);
			broadcast(name + "님이 방문하셨습니다.");

			while (true) {
				String msg = i.readLine();
				broadcast(name + " - " + msg);
				if (msg.equals("exit"))
					break;
			}

			server.unRegister(this);
			broadcast(name + "님이 나가셨습니다");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				i.close();
				o.close();
				s.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	public void println(String message) {
		// TODO Auto-generated method stub
		o.println(message);
	}

	public void broadcast(String message) {
		server.broadcast(message);
	}

}
