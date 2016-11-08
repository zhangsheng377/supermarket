package supermarket;

public class Supermarket {
	public Supermarket(){
	}
	
	public static void main(String[] args) {
		Login login_window=new Login();
		login_window.setVisible(true);
		System.out.println("isLogin = "+login_window.getIsLogin());
	}
}