package multiChatting;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

//Ư���� ����� ���� �� 
public class ChatServer {
	// ����ڵ��� ����
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

	// ���
	public void register(ChatHandler c) {
		handlers.addElement(c);
	}

	public void unRegister(Object o) {
		handlers.removeElement(o);
	}

	// ��� ����ڿ��� ����Ѵ�.
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
