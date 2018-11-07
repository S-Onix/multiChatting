package chatting2;

import java.io.*;
import java.util.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Login extends Frame implements ActionListener, Runnable {

	String myid;
	BufferedReader in;
	OutputStream out;
	Socket s;

	Chat chat = new Chat();
	ReName nameRe = new ReName();
	Paper paper = new Paper();

	Panel global = new Panel();
	GridLayout gridLayout1 = new GridLayout();
	Panel jPanel1 = new Panel();
	Panel jPanel2 = new Panel();
	Panel jPanel3 = new Panel();
	Label ip = new Label();
	Label name = new Label();
	TextField ipTF = new TextField();
	TextField nameTF = new TextField();
	Button cancel = new Button();
	Button ok = new Button();
	Font f = new Font("SansSerif", 0, 12);
	Label jl1 = new Label();
	Label jl2 = new Label();

	public Login() {
		super("Login!!");
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception {
		setBackground(new Color(249, 255, 255));
		// setLayout(null);

		global.setBounds(new Rectangle(3, 3, 246, 114));
		global.setLayout(gridLayout1);
		gridLayout1.setRows(3);
		gridLayout1.setColumns(1);
		gridLayout1.setVgap(5);

		jPanel3.setLayout(null);
		jPanel3.setBackground(Color.blue);

		jPanel2.setLayout(null);
		jPanel2.setBackground(Color.yellow);

		jPanel1.setLayout(null);
		jPanel1.setBackground(Color.green);
		ip.setFont(f);
		ip.setText("   I        P   : ");
		ip.setBounds(new Rectangle(6, 3, 66, 27));
		name.setBounds(new Rectangle(6, 0, 66, 27));
		name.setFont(f);
		name.setText("  대 화 명  : ");

		// jcomponent에 있다
		// ipTF.setNextFocusableComponent(nameTF);

		ipTF.setBounds(new Rectangle(78, 3, 163, 27));
		// nameTF.setNextFocusableComponent(ok);
		nameTF.setBounds(new Rectangle(78, 0, 163, 27));
		cancel.setFont(f);

		cancel.setLabel("취 소");
		cancel.setBounds(new Rectangle(126, 2, 67, 26));
		ok.setBounds(new Rectangle(48, 2, 67, 26));
		ok.setFont(f);

		// ok.setNextFocusableComponent(cancel);
		ok.setLabel("확 인");
		add(global, BorderLayout.CENTER);
		global.add(jPanel1, null);
		jPanel1.add(ip, null);
		jPanel1.add(ipTF, null);
		global.add(jPanel2, null);
		jPanel2.add(name, null);
		jPanel2.add(nameTF, null);
		global.add(jPanel3, null);
		jPanel3.add(cancel, null);
		jPanel3.add(ok, null);

		jl1.setFont(f);
		jl1.setText("자신한테 쪽지라니 쩝..");
		jl2.setFont(f);
		jl2.setText("자신한테 귓말을 보내는 사람두 인나용 ㅡ.ㅡ++");

		setBounds(200, 200, 259, 146);
		setVisible(true);

		ok.addActionListener(this);
		cancel.addActionListener(this);
		nameTF.addActionListener(this);

		chat.globalsend.addActionListener(this);
		chat.whomsend.addActionListener(this);
		chat.rename.addActionListener(this);
		chat.paper.addActionListener(this);
		chat.close.addActionListener(this);

		nameRe.ok.addActionListener(this);
		nameRe.cancel.addActionListener(this);
		nameRe.newname.addActionListener(this);

		paper.ok.addActionListener(this);
		paper.cancel.addActionListener(this);
		paper.answer.addActionListener(this);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				closeProcess();
				System.exit(0);
			}
		});
	}

	public void actionPerformed(ActionEvent e) {

		Object ob = e.getSource();

		/******************* 접속 처리 ****************/
		if (ob == ok || ob == nameTF)
			connectProcess();
		else if (ob == cancel)
			setVisible(false);
		/********************************************/

		/***************** 문자 보내기 *****************/
		else if (ob == chat.globalsend)
			sendProcess();
		else if (ob == chat.whomsend)
			whomProcess();
		/*********************************************/

		/***************** 대화명 변경 *****************/
		else if (ob == chat.rename) {
			nameRe.oldname.setText(myid);
			nameRe.setVisible(true);
		} else if (ob == nameRe.ok || ob == nameRe.newname)
			renameProcess();
		else if (ob == nameRe.cancel)
			nameRe.setVisible(false);
		/***************** 쪽지 보내기 *****************/
		else if (ob == chat.paper) {
			String whom = chat.list.getSelectedItem();
			if (whom.equals(myid)) {
				Notification n = new Notification(this, "이러지점 마!!");
				n.setLocation(this.getLocation());
				n.show();
				// JOptionPane.showMessageDialog(this, jl1, "이러지점 마!!",
				// JOptionPane.WARNING_MESSAGE);

				return;
			} else {
				paper.to.setText(whom);
				paper.from.setText(myid);
				paper.letter.setText("");
				paper.letter.requestFocus();
				paper.setVisible(true);
			}
		} else if (ob == paper.ok)
			paperProcess();
		else if (ob == paper.cancel) {
			paper.to.setText("");
			paper.from.setText("");
			paper.letter.setText("");
			paper.setVisible(false);
		} else if (ob == paper.answer) {
			String temp1 = paper.from.getText();
			String temp2 = paper.to.getText();
			paper.from.setText(temp2);
			paper.to.setText(temp1);
			paper.letter.append("\n -------------------------------- \n");
			paper.letter.requestFocus();
			paper.card.show(paper.south1, "ok");
		}
		/***************************************************/

		/********************* 대화 종료 *********************/

		else if (ob == chat.close) {
			closeProcess();
			System.exit(0);
		}
		/***************************************************/
	}// actionPerformed() end

	public void connectProcess() {
		myid = nameTF.getText().trim();
		if (myid == null)
			return;
		try {
			String host = ipTF.getText().trim();
			s = new Socket(host, 6789);
			setVisible(false);
			chat.globalsend.requestFocus();
			chat.setVisible(true);
			System.out.println("접속 성공");
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			out = s.getOutputStream();
			out.write(("100|" + myid + "\n").getBytes());
			new Thread(this).start();
		} catch (Exception e) {
			System.out.println("접속실패 에러 : " + e);
		}

	}// connectProcess() end

	public void sendProcess() {
		String msg = chat.globalsend.getText().trim();
		if (msg == null)
			return;
		try {
			out.write(("200|" + msg + "\n").getBytes());
		} catch (Exception e) {
			System.out.println("client globalsend : " + e);
		}
		chat.globalsend.setText("");
		chat.globalsend.requestFocus();

	}// sendProcess() end

	public void whomProcess() {
		String to = chat.list.getSelectedItem();
		String temp = chat.whomsend.getText().trim();

		if (to.equals(myid)) {
			JOptionPane.showMessageDialog(this, jl2, "?ㅡ.ㅜ?", JOptionPane.WARNING_MESSAGE);
			return;
		}

		if (to == null || temp == null)
			return;
		chat.area.append(myid + "가(" + to + "에게 속삭임) ▶ " + temp + "\n");
		try {
			out.write(("250|" + to + "|" + temp + "\n").getBytes());
		} catch (Exception e) {
			System.out.println("client whomsend : " + e);
		}
		chat.whomsend.setText("");
		chat.whomsend.requestFocus();

	}// whomProcess() end

	public void paperProcess() {
		String to = paper.to.getText().trim();
		String temp = paper.letter.getText().trim();
		paper.setVisible(false);
		String message = temp.replace('\n', '\\');
		try {
			out.write(("600|" + to + "|" + message + "\n").getBytes());
		} catch (Exception e) {
			System.out.println("client paper : " + e);
		}
		paper.to.setText("");
		paper.from.setText("");
		paper.letter.setText("");

	}// paperProcess() end

	public void renameProcess() {
		String temp = nameRe.newname.getText().trim();
		if (temp == null)
			return;
		nameRe.setVisible(false);
		nameRe.newname.setText("");
		myid = temp;
		try {
			out.write(("500|" + temp + "\n").getBytes());
		} catch (Exception e) {
			System.out.println("client rename : " + e);
		}

	}// renameProcess() end

	public void closeProcess() {
		chat.list.removeAll();
		chat.area.setText("");
		chat.globalsend.setText("");
		chat.whom.setText("");
		chat.whomsend.setText("");
		chat.setVisible(false);
		try {
			out.write(("900|\n").getBytes());
			/*
			 * in.close(); out.close(); System.exit(0);
			 */
		} catch (Exception e) {
			System.out.println("종료에러 : " + e);
		}

	}// closeProcess() end

	public void run() {
		while (true) {
			try {
				String msg = in.readLine();
				System.out.println("Client Receive : " + msg);
				if (msg == null)
					return;
				StringTokenizer st = new StringTokenizer(msg, "|");
				int protocol = Integer.parseInt(st.nextToken());
				switch (protocol) {
				case 100: {
					String temp = st.nextToken();
					chat.list.add(temp);
					chat.area.append("**알림**" + temp + "님이 입장하셨습니다\n");
				}
					break;
				// case 100 end

				case 200: {
					String temp = st.nextToken();
					chat.area.append(temp + "\n");
				}
					break;
				// case 200 end

				case 500: {
					String tempOld = st.nextToken();
					String tempNew = st.nextToken();
					for (int i = 0; i < chat.list.getItemCount(); i++) {
						if (tempOld.equals(chat.list.getItem(i))) {
							chat.list.replaceItem(tempNew, i);
							break;
						}
					}
					chat.area.append("▶알림◀ " + tempOld + "님이 " + tempNew + "님으로 대화명을 변경하였습니다.\n");
				}
					break;
				// case 500 end

				case 600: {
					String to = st.nextToken();
					String temp = st.nextToken();
					paper.to.setText(myid);
					paper.from.setText(to);
					paper.letter.setText("");
					paper.setVisible(true);
					paper.letter.append(temp.replace('\\', '\n'));
					paper.card.show(paper.south1, "answer");
				}
					break;
				// case 600 end

				case 900: {
					String temp = st.nextToken();
					if (temp.equals(myid)) {
						in.close();
						out.close();
						s.close();
						System.exit(0);
					}
					chat.list.remove(temp);
					chat.area.append("▷알림◁ " + temp + "님이 퇴장하셨습니다.\n");
				}
					break;
				// case 900 end

				}// switch end
			} catch (Exception e) {
				System.out.println("Client전체에러 : " + e);
				return;
			}
		} // while end
	}// run() end

	public static void main(String[] args) {
		Login login = new Login();
	}
}

class Notification extends Dialog implements ActionListener {
	String msg;

	public Notification(Frame f, String s) {
		super(f, "잠깐!", true);
		msg = s;
	}

	public void show() {
		Button b;
		add("North", new Label(msg, Label.CENTER));
		b = new Button("OK");
		b.addActionListener(this);

		Panel p = new Panel();
		p.add(b);
		add("South", p);
		setBackground(Color.gray);
		setSize(160, 100);
		super.show();
	}

	public void actionPerformed(ActionEvent e) {
		dispose();
	}
}
