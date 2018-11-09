package multiChatting;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.List;
import java.awt.Panel;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatClient extends JFrame implements Runnable, ActionListener {

	private BufferedReader i;
	private PrintWriter o;

	private JPanel leftPanel;
	private JTextArea output;
	private JTextField input;
	private JLabel label;
	private Thread listener;
	private String host;
	private JScrollPane jp;
	private JScrollBar jb;

	private JPanel rightPanel;
	private JLabel listLabel;
	private List list;
	private JButton rename;
	private JButton paper;
	private JButton close;

	public ChatClient(String server) {
		super("채팅 프로그램");
		host = server;
		listener = new Thread(this);
		listener.start();

		// TODO : UI
		initLeftPanel();
		initRightPanel();

		this.add(leftPanel);
		this.add(rightPanel);

		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(530, 350);
		this.setVisible(true);
	}

	// 현재 잇는 것들을 leftPane으로 이동
	public void initLeftPanel() {
		leftPanel = new JPanel();
		leftPanel.setLayout(new BorderLayout());
		leftPanel.setBounds(new Rectangle(4, 3, 400, 300));

		output = new JTextArea();
		jp = new JScrollPane(output);
		leftPanel.add(jp, "Center");
		output.setEnabled(false);

		Panel bottom = new Panel(new BorderLayout());
		label = new JLabel("사용자 이름");
		bottom.add(label, "West");
		input = new JTextField();
		bottom.add(input, "Center");
		input.addActionListener(this);
		leftPanel.add(bottom, "South");

		leftPanel.setVisible(true);
	}

	// rigthPane 구성 후 Frame에 두 Pane 추가
	public void initRightPanel() {
		rightPanel = new JPanel();
		rightPanel.setLayout(null);
		rightPanel.setBounds(new Rectangle(410, 3, 100, 300));

		listLabel = new JLabel("접속자 목록");
		listLabel.setFont(new Font("SansSerif", 0, 12));
		listLabel.setBounds(new Rectangle(410, 0, 100, 50));

		list = new List();
		list.setBounds(new Rectangle(410, 50, 100, 150));

		rename = new JButton("이름재설정");
		rename.setFont(new Font("SansSerif", 0, 12));
		rename.setBounds(new Rectangle(410, 205, 100, 30));

		paper = new JButton("쪽지보내기");
		paper.setFont(new Font("SansSerif", 0, 12));
		paper.setBounds(new Rectangle(410, 240, 100, 30));

		close = new JButton("나가기");
		close.setFont(new Font("SansSerif", 0, 12));
		close.setBounds(new Rectangle(410, 275, 100, 30));

		rightPanel.add(listLabel);
		rightPanel.add(list);
		rightPanel.add(rename);
		rightPanel.add(paper);
		rightPanel.add(close);

		rightPanel.setVisible(true);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			Socket s = new Socket(host, 9830);
			InputStream ins = s.getInputStream();
			OutputStream os = s.getOutputStream();

			i = new BufferedReader(new InputStreamReader(ins));
			o = new PrintWriter(new OutputStreamWriter(os), true);
			StringTokenizer st;

			while (true) {
				String line = i.readLine();
				st = new StringTokenizer(line, "|");
				list.removeAll();
				
				while (st.hasMoreTokens()) {
					String line2 = st.nextToken();
					
					if (st.countTokens() == 0) {
						output.append(line2 + "\n");
						break;
					} else {
						list.add(line2);
					}
					Dimension d = jp.getMaximumSize();
					jp.getVerticalScrollBar().setValue(jp.getVerticalScrollBar().getMaximum());
				}
			}

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object c = e.getSource();
		if (c == input) {
			if (!input.getText().equals("")) {
				label.setText("메세지");
				o.println(input.getText());
			}

			input.setText("");
		}

	}

	public void listAdd(String user) {
		list.add(user);
	}

	public static void main(String[] args) {
		if (args.length > 0) {
			new ChatClient(args[0]);
		} else {
			new ChatClient("localhost");
		}
	}

}
