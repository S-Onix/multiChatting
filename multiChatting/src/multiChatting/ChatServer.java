package multiChatting;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

//특정된 사용자 저장 및 
public class ChatServer {
	// 사용자들의 집합
	private Vector<ChatHandler> handlers;

	public ChatServer(int port) {
		try {
			ServerSocket server = new ServerSocket(port);
			handlers = new Vector();

			while (true) {
				Socket client = server.accept();
				ChatHandler c = new ChatHandler(this, client);
				c.start();

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//
	public Object getHandler(int index) {
		return handlers.elementAt(index);
	}

	// 등록
	public void register(ChatHandler c) {
		handlers.addElement(c);
	}

	public void unRegister(Object o) {
		handlers.removeElement(o);
	}

	// 모든 사용자에게 방송한다.
	public void broadcast(String message) {
		synchronized(handlers) {
			int n = handlers.size();
			for (int i = 0; i < n; i++) {
				ChatHandler c = (ChatHandler) handlers.elementAt(i);
				try{
					c.println(message);
				}catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}
	}
	
	public static void main(String[] args) {
		new ChatServer(9830);
	}

}
