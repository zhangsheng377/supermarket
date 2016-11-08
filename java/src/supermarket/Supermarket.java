package supermarket;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Supermarket extends JFrame{
	public Supermarket(){
		super();
	}
	
	public static void main(String[] args) {
		Login login_window=new Login();
		login_window.setVisible(true);
	}
}