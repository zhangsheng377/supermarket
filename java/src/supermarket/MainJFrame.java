package supermarket;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintWriter;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import sqlexcute.SqlExcute;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainJFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;

	/**
	 * Create the frame.
	 */
	public MainJFrame(SqlExcute sqlExcute, String name, PrintWriter targetPrintWriter,
			Map<String, ChatMessage> chatingMap_targetname_chatmessage) {
		// 原计划是获取好友列表，然后显示的。但时间不够了
		setTitle(name + " chat with ...");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.out.println("mainJFrame windowClosing");
				// textField.setText(null);
				dispose();
			}
		});
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(0, 1, 0, 0));

		JLabel lblNewLabel = new JLabel("target name :");
		contentPane.add(lblNewLabel);

		textField = new JTextField();
		contentPane.add(textField);
		textField.setColumns(10);

		JButton btnNewButton = new JButton("chat");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ChatMessage chatMessage_window = chatingMap_targetname_chatmessage.get(textField.getText());
				if (chatMessage_window == null) {
					
					chatMessage_window = new ChatMessage(sqlExcute, name, textField.getText(), targetPrintWriter,
							chatingMap_targetname_chatmessage);
					System.out.println("chatMessage_window == null");
					chatingMap_targetname_chatmessage.put(textField.getText(), chatMessage_window);
				} else {
					//chatMessage_window.setVisible(true);
				}
			}
		});
		contentPane.add(btnNewButton);
		setVisible(true);
	}

}
