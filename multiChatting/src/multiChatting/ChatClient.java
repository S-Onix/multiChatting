package multiChatting;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Panel;
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

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatClient extends JFrame implements Runnable, ActionListener {

	private BufferedReader i;
	private PrintWriter o;
	private JTextArea output;
	private JTextField input;
	private JLabel label;
	private Thread listener;
	private String host;
	private JScrollPane jp;
	private JScrollBar jb;

	public ChatClient(String server) {
		super("채팅 프로그램");
		host = server;
		listener = new Thread(this);
		listener.start();

		// TODO : UI
		output = new JTextArea();
		jp = new JScrollPane(output);
		add(jp, "Center");

		output.setEnabled(false);
		Panel bottom = new Panel(new BorderLayout());
		label = new JLabel("사용자 이름");
		bottom.add(label, "West");
		input = new JTextField();
		bottom.add(input, "Center");
		input.addActionListener(this);
		add(bottom, "South");

		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(400, 300);
		this.setVisible(true);
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

			while (true) {
				String line = i.readLine();
				if (!line.equals("")) {
					output.append(line + "\n");
				}
				Dimension d = jp.getMaximumSize();
				jp.getVerticalScrollBar().setValue(jp.getVerticalScrollBar().getMaximum());
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

	public static void main(String[] args) {
		if (args.length > 0) {
			new ChatClient(args[0]);
		} else {
			new ChatClient("localhost");
		}
	}

}
