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

	@Override
	public void run() {
		String name = "";
		try {
			name = i.readLine();
			server.register(this);
			broadcast(name + "¥‘¿Ã πÊπÆ«œºÃΩ¿¥œ¥Ÿ.");

			while (true) {
				String msg = i.readLine();
				broadcast(name + " - " + msg);
				if (msg.equals("exit"))
					break;
			}

			server.unRegister(this);
			broadcast(name + "¥‘¿Ã ≥™∞°ºÃΩ¿¥œ¥Ÿ");

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
