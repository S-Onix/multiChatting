package chatting2;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Chat extends JFrame implements ItemListener, AdjustmentListener {
	JPanel global = new JPanel();
	JPanel jPanel1 = new JPanel();
	GridLayout gridLayout1 = new GridLayout();
	JButton rename = new JButton();
	JButton close = new JButton();
	JButton paper = new JButton();
	JTextField globalsend = new JTextField();
	JTextField whomsend = new JTextField();
	JLabel whom = new JLabel();
	List list = new List();
	JScrollPane jsP = new JScrollPane();
	JTextArea area = new JTextArea();
	JScrollBar jsB;

	public Chat() {
		super("즐거운 체팅.. ^^*");
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception {
		this.getContentPane().setBackground(new Color(236, 197, 255));
		this.setResizable(false);
		this.getContentPane().setLayout(null);
		global.setEnabled(false);
		global.setBorder(BorderFactory.createEtchedBorder());
		global.setOpaque(false);
		global.setRequestFocusEnabled(false);
		global.setBounds(new Rectangle(4, 3, 441, 309));
		global.setLayout(null);
		jPanel1.setOpaque(false);
		jPanel1.setBounds(new Rectangle(316, 219, 120, 87));
		jPanel1.setLayout(gridLayout1);
		gridLayout1.setRows(3);
		gridLayout1.setColumns(1);
		gridLayout1.setVgap(5);
		rename.setFont(new java.awt.Font("SansSerif", 0, 12));
		rename.setBorder(BorderFactory.createRaisedBevelBorder());
		rename.setText("대화명변경");
		close.setFont(new java.awt.Font("SansSerif", 0, 12));
		close.setBorder(BorderFactory.createRaisedBevelBorder());
		close.setText("나    가    기");
		paper.setFont(new java.awt.Font("SansSerif", 0, 12));
		paper.setBorder(BorderFactory.createRaisedBevelBorder());
		paper.setText("쪽지보내기");
		globalsend.setBounds(new Rectangle(7, 249, 306, 27));
		whomsend.setBounds(new Rectangle(70, 278, 243, 27));
		whom.setEnabled(false);
		whom.setFont(new java.awt.Font("SansSerif", 0, 12));
		whom.setRequestFocusEnabled(false);
		whom.setBounds(new Rectangle(7, 278, 61, 27));
		list.setBounds(new Rectangle(316, 5, 120, 207));
		jsP.setAutoscrolls(true);
		jsP.setBounds(new Rectangle(7, 5, 306, 238));
		this.getContentPane().add(global, null);
		jPanel1.add(rename, null);
		jPanel1.add(paper, null);
		jPanel1.add(close, null);
		global.add(globalsend, null);
		global.add(whomsend, null);
		global.add(whom, null);
		global.add(jPanel1, null);
		global.add(list, null);
		global.add(jsP, null);
		jsP.getViewport().add(area, null);
		setBounds(250, 200, 455, 340);
		// setVisible(true);

		list.addItemListener(this);
	}

	public void itemStateChanged(ItemEvent e) {
		String item = list.getSelectedItem();
		whom.setText(item);
	}

	public void adjustmentValueChanged(AdjustmentEvent e) {
		jsB.setValue(jsB.getMaximum());
	}
	/*
	 * public static void main(String[] args) { Chat chat = new Chat(); }
	 */
}
