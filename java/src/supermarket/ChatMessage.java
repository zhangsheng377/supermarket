package supermarket;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import sqlexcute.SqlExcute;

import javax.swing.JTextArea;
import java.awt.GridLayout;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javax.swing.JTextField;
import javax.swing.BoxLayout;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintWriter;
import java.awt.event.ActionEvent;

public class ChatMessage extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	@SuppressWarnings("unused")
	private SqlExcute sqlExcute;
	private JTextField textField;
	private JTextArea textArea = new JTextArea();
	@SuppressWarnings("unused")
	private String name;
	@SuppressWarnings("unused")
	private String targetName;
	@SuppressWarnings("unused")
	private PrintWriter targetPrintWriter;

	public void addText(String str) {
		textArea.setText(textArea.getText() + str + "\n");
	}

	/**
	 * Launch the application.
	 *
	 * public static void main(String[] args) { try { ChatMessage dialog = new
	 * ChatMessage(); dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	 * dialog.setVisible(true); } catch (Exception e) { e.printStackTrace(); } }
	 */

	/**
	 * Create the dialog.
	 */
	public ChatMessage(SqlExcute sqlExcute, String name, String targetName, PrintWriter targetPrintWriter,
			Map<String, ChatMessage> chatingMap_targetname_chatmessage) {
		this.sqlExcute = sqlExcute;
		this.name = name;
		this.targetName = targetName;
		this.targetPrintWriter = targetPrintWriter;

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.out.println("windowClosing");
				chatingMap_targetname_chatmessage.remove(targetName);
				dispose();
			}
		});
		setTitle(targetName);
		setModal(false);
		setVisible(true);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new GridLayout(0, 1, 0, 0));
		{
			textArea.setEditable(false);
			// JTextArea textArea = new JTextArea();
			contentPanel.add(textArea);
		}
		{
			JPanel buttonPane = new JPanel();
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));
			{
				textField = new JTextField();
				buttonPane.add(textField);
				textField.setColumns(10);
			}
			{
				JButton okButton = new JButton("send");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						String tempString = "message " + targetName + " " + textField.getText();
						targetPrintWriter.println(tempString);
						targetPrintWriter.flush();
						String sql = "insert into chat_" + targetName + " (SPEAKER,CONTENT) VALUES ('" + name + "', '"
								+ textField.getText() + "')";
						sqlExcute.sql_execute(sql);
						addText(name + " said : " + textField.getText());
						textField.setText(null);
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
		String sql = "select * from chat_" + targetName;
		ResultSet resultSet = sqlExcute.sql_select_execute(sql);
		try {
			while (resultSet != null && resultSet.next()) {
				addText(resultSet.getString(1) + " said : " + resultSet.getString(2));
				System.out.println(
						"in while resultSet.next : " + resultSet.getString(1) + " said : " + resultSet.getString(2));
			}
		} catch (SQLException e1) {
			System.out.println("while addText : error : " + e1);
		}
		System.out.println("ChatMessage over");
	}

}
