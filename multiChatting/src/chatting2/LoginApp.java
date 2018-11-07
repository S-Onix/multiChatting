package chatting2;

import java.io.*;
import java.util.*;
import java.net.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class LoginApp extends JApplet implements ActionListener, Runnable {

	String myid;
	BufferedReader in;
	OutputStream out;
	Socket s;

	Chat chat = new Chat();
	ReName nameRe = new ReName();
	Paper paper = new Paper();

	JPanel global = new JPanel();
	GridLayout gridLayout1 = new GridLayout();
	JPanel jPanel1 = new JPanel();
	JPanel jPanel2 = new JPanel();
	JPanel jPanel3 = new JPanel();
	JLabel ip = new JLabel();
	JLabel name = new JLabel();
	JTextField ipTF = new JTextField();
	JTextField nameTF = new JTextField();
	JButton cancel = new JButton();
	JButton ok = new JButton();
	Font f = new Font("SansSerif", 0, 12);
	JLabel jl1 = new JLabel();
	JLabel jl2 = new JLabel();

	public void init() {
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception {
		this.getContentPane().setBackground(new Color(249, 255, 255));
		this.getContentPane().setLayout(null);
		global.setBorder(BorderFactory.createEtchedBorder());
		global.setOpaque(false);
		global.setBounds(new Rectangle(3, 3, 246, 114));
		global.setLayout(gridLayout1);
		gridLayout1.setRows(3);
		gridLayout1.setColumns(1);
		gridLayout1.setVgap(5);
		jPanel3.setBorder(BorderFactory.createEtchedBorder());
		jPanel3.setOpaque(false);
		jPanel3.setLayout(null);
		jPanel2.setOpaque(false);
		jPanel2.setLayout(null);
		jPanel1.setOpaque(false);
		jPanel1.setLayout(null);
		ip.setFont(new java.awt.Font("SansSerif", 0, 12));
		ip.setText("   I        P   : ");
		ip.setBounds(new Rectangle(6, 3, 66, 27));
		name.setBounds(new Rectangle(6, 0, 66, 27));
		name.setFont(new java.awt.Font("SansSerif", 0, 12));
		name.setText("  대 화 명  : ");
		ipTF.setNextFocusableComponent(nameTF);
		ipTF.setBounds(new Rectangle(78, 3, 163, 27));
		nameTF.setNextFocusableComponent(ok);
		nameTF.setBounds(new Rectangle(78, 0, 163, 27));
		cancel.setFont(new java.awt.Font("SansSerif", 0, 12));
		cancel.setBorder(BorderFactory.createRaisedBevelBorder());
		cancel.setText("취 소");
		cancel.setBounds(new Rectangle(126, 2, 67, 26));
		ok.setBounds(new Rectangle(48, 2, 67, 26));
		ok.setFont(new java.awt.Font("SansSerif", 0, 12));
		ok.setBorder(BorderFactory.createRaisedBevelBorder());
		ok.setNextFocusableComponent(cancel);
		ok.setText("확 인");
		this.getContentPane().add(global, null);
		global.add(jPanel1, null);
		jPanel1.add(ip, null);
		jPanel1.add(ipTF, null);
		global.add(jPanel2, null);
		jPanel2.add(name, null);
		jPanel2.add(nameTF, null);
		global.add(jPanel3, null);
		jPanel3.add(cancel, null);
		jPanel3.add(ok, null);
		ipTF.setEnabled(false);
		chat.area.setEnabled(false);

		jl1.setFont(f);
		jl1.setText("자신한테 쪽지라니 쩝..");
		jl2.setFont(f);
		jl2.setText("자신한테 귓말을 보내는 사람두 인나용 ㅡ.ㅡ++");
	}// init end

	public void start() {

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

	}// start end

	public void stop() {
		closeProcess();
	}

	public void destroy() {
		closeProcess();
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
				JOptionPane.showMessageDialog(this, jl1, "이러지점 마!!", JOptionPane.WARNING_MESSAGE);
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
			String host = getCodeBase().getHost();
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
		LoginApp login = new LoginApp();
	}
}
