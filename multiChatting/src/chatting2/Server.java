package chatting2;

import java.io.*;
import java.util.*;
import java.net.*;

public class Server implements Runnable {

	Vector vc = new Vector();

	public void run() {
		ServerSocket ss = null;
		try {
			ss = new ServerSocket(6789);
			System.out.println("Client 접속 대기 ....");
		} catch (Exception e) {
			System.out.println(e);
		}
		while (true) {
			try {
				Socket s = ss.accept();
				System.out.println("Client 접속 성공 : " + s);
				Service sv = new Service(s);
				sv.start();
			} catch (Exception e) {
				System.out.println("Client 접속 실패 : " + e);
			}
		} // while end
	}// run() end

	public class Service extends Thread {
		BufferedReader in;
		OutputStream out;
		Socket s;
		String name;

		public Service(Socket s) {
			try {
				this.s = s;
				in = new BufferedReader(new InputStreamReader(s.getInputStream()));
				out = s.getOutputStream();
			} catch (Exception e) {
				System.out.println("Inner constructor : " + e);
			}
		}

		public void run() {
			while (true) {
				try {
					String msg = in.readLine();
					System.out.println("Server Receive : " + msg);
					if (msg.length() < 1)
						return;
					StringTokenizer st = new StringTokenizer(msg, "|");
					int protocol = Integer.parseInt(st.nextToken());
					switch (protocol) {

					/********************* 접 속 처 리 *********************/
					case 100: {
						name = st.nextToken();
						multicast("100|" + name);
						vc.addElement(this);
						for (int i = 0; i < vc.size(); i++) {
							Service sv = (Service) vc.elementAt(i);
							try {
								unicast("100|" + sv.name);
							} catch (Exception e) {
								System.out.println("server 100 : " + e);
							}
						}
					}
						break;
					// case 100 end

					/******************* 모든 사람과 대화 *******************/
					case 200: {
						String temp = st.nextToken();
						multicast("200|" + name + " ☞ " + temp);
					}
						break;
					// case 200 end

					case 250: {
						String to = st.nextToken();
						String temp = st.nextToken();
						for (int i = 0; i < vc.size(); i++) {
							Service sv = (Service) vc.elementAt(i);
							if (to.equals(sv.name)) {
								try {
									sv.unicast("200|♣" + name + "♣ " + temp);
								} catch (Exception e) {
									System.out.println("server 250 : " + e);
								}
								break;
							} // if end
						} // for end
					}
						break;
					// case 250 end

					case 500: {
						String temp = st.nextToken();
						multicast("500|" + name + "|" + temp);
						name = temp;
					}
						break;
					// case 500 end

					case 600: {
						String to = st.nextToken();
						String temp = st.nextToken();
						for (int i = 0; i < vc.size(); i++) {
							Service sv = (Service) vc.elementAt(i);
							if (to.equals(sv.name)) {
								try {
									sv.unicast("600|" + name + "|" + temp);
								} catch (Exception e) {
									System.out.println("server 600 : " + e);
								}
								break;
							}
						} // for end
					}
						break;
					// case 600 end

					case 900: {
						for (int i = 0; i < vc.size(); i++) {
							Service sv = (Service) vc.elementAt(i);
							if (name.equals(sv.name)) {
								vc.removeElementAt(i);
								break;
							}
						}
						multicast("900|" + name);
						try {
							in.close();
							out.close();
							return;
						} catch (Exception e) {
							System.out.println("서버나가기 에러 900 : " + e);
							return;
						}

					}
					// case 900 end

					}// switch end
				} catch (Exception e) {
					System.out.println("Server전체에러 : " + e);
					break;
				}
			} // while end
		}// run() end

		public void multicast(String msg) {
			synchronized (this) {
				for (int i = 0; i < vc.size(); i++) {
					Service sv = (Service) vc.elementAt(i);
					try {
						sv.unicast(msg);
					} catch (Exception e) {
						System.out.println("multicast : " + e);
					}
				} // for end
			} // synchronized end
		}// multicast() end

		public void unicast(String msg) throws Exception {
			synchronized (this) {
				try {
					out.write((msg + "\n").getBytes());
				} catch (Exception e) {
					System.out.println("unicast err : " + e);
				}
			}
		}
	}// Service class end

	public static void main(String[] args) {
		Server server = new Server();
		new Thread(server).start();
	}
}
