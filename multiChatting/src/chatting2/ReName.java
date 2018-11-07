package chatting2;
import java.awt.GridLayout;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame; 
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

public class ReName extends JFrame {
	JPanel jPanel1 = new JPanel();
	GridLayout gridLayout1 = new GridLayout();
	JPanel jPanel2 = new JPanel();
	JPanel jPanel3 = new JPanel();
	JPanel jPanel4 = new JPanel();
	JLabel l1 = new JLabel();
	JLabel l2 = new JLabel();
	JLabel oldname = new JLabel();
	JTextField newname = new JTextField();
	JPanel jPanel5 = new JPanel();
	GridLayout gridLayout2 = new GridLayout();
	JButton ok = new JButton();
	JButton cancel = new JButton();

	public ReName() {
		super("대화명 변경");
		try {
			jbInit();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	
	private void jbInit() throws Exception {
		this.getContentPane().setBackground(UIManager.getColor("info"));
		this.setResizable(false);
		this.getContentPane().setLayout(null);
		jPanel1.setBorder(BorderFactory.createEtchedBorder());
		jPanel1.setOpaque(false);
		jPanel1.setBounds(new Rectangle(3, 3, 249, 114));
		jPanel1.setLayout(gridLayout1);
		gridLayout1.setRows(3);
		gridLayout1.setColumns(1);
		gridLayout1.setVgap(5);
		jPanel2.setLayout(null);
		jPanel3.setLayout(null);
		jPanel4.setLayout(null);
		l1.setRequestFocusEnabled(false);
		l1.setHorizontalAlignment(SwingConstants.CENTER);
		l1.setText("Old Name : ");
		l1.setBounds(new Rectangle(6, 3, 84, 27));
		l2.setBounds(new Rectangle(6, 4, 84, 27));
		l2.setRequestFocusEnabled(false);
		l2.setHorizontalAlignment(SwingConstants.CENTER);
		l2.setText("New Name : ");
		oldname.setEnabled(false);
    oldname.setFont(new java.awt.Font("SansSerif", 0, 12));
		oldname.setRequestFocusEnabled(false);
		oldname.setHorizontalAlignment(SwingConstants.CENTER);
		oldname.setBounds(new Rectangle(97, 3, 138, 27));
		newname.setNextFocusableComponent(ok);
		newname.setBounds(new Rectangle(97, 4, 141, 27));
		jPanel4.setOpaque(false);
		jPanel3.setOpaque(false);
		jPanel2.setOpaque(false);
		jPanel5.setBorder(BorderFactory.createEtchedBorder());
		jPanel5.setOpaque(false);
		jPanel5.setBounds(new Rectangle(52, 3, 141, 27));
		jPanel5.setLayout(gridLayout2);
		gridLayout2.setColumns(2);
		gridLayout2.setHgap(5);
		ok.setFont(new java.awt.Font("SansSerif", 0, 12));
		ok.setBorder(BorderFactory.createRaisedBevelBorder());
		ok.setText("변 경");
		cancel.setFont(new java.awt.Font("SansSerif", 0, 12));
		cancel.setBorder(BorderFactory.createRaisedBevelBorder());
		cancel.setText("취 소");
		this.getContentPane().add(jPanel1, null);
		jPanel1.add(jPanel2, null);
		jPanel2.add(l1, null);
		jPanel1.add(jPanel3, null);
		jPanel1.add(jPanel4, null);
		jPanel3.add(l2, null);
		jPanel3.add(newname, null);
		jPanel2.add(oldname, null);
		jPanel4.add(jPanel5, null);
		jPanel5.add(ok, null);
		jPanel5.add(cancel, null);
		setBounds(200,200,261,145);
		//setVisible(true);
	}
	 
	/*
	public static void main(String[] args) {
	ReName reName = new ReName();
	}
	*/
}
