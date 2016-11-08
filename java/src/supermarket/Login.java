package supermarket;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JTextField;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class Login extends JDialog {

	private JPanel contentPane;
	private JTextField textField_1;
	private JPasswordField passwordField;
	
	private boolean isLogin=false;
	
	public boolean getIsLogin() {
		return isLogin;
	}

	/**
	 * Create the frame.
	 */
	public Login() {
		setTitle("Login");
		setModal(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(3, 2));
		
		JLabel lblNewLabel = new JLabel("name");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblNewLabel);
		
		textField_1 = new JTextField();
		contentPane.add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("password");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblNewLabel_1);
		
		passwordField = new JPasswordField();
		contentPane.add(passwordField);
		
		JButton btnNewButton = new JButton("login");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				isLogin=true;
				System.out.println("logined");
				dispose();
			}
		});
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("exit");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				isLogin=false;
				System.out.println("failed");
				dispose();
			}
		});
		contentPane.add(btnNewButton_1);
	}

}
