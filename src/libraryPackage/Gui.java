package libraryPackage;

import java.awt.event.ActionListener;
import java.sql.*;
import java.sql.DriverManager;

import javax.swing.*;

public class Gui extends JFrame {	
	private JPanel mainPanel;
	
	private JLabel userNameLabel;
	private JLabel passwordLabel;
	
	private JTextField userNameInput;
	private JPasswordField passwordInput;
	
	private JButton loginButton;
	
	private LoginSuccessGui loggedInGui;
	
	public Gui() {
		
		this.setTitle("Library");
		this.setSize(800,600);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		this.mainPanel = new JPanel();
		this.mainPanel.setLayout(null);
		
		this.userNameLabel = new JLabel("Username");
		this.userNameLabel.setBounds(275, 100, 100, 30);
		
		this.passwordLabel = new JLabel("Password");
		this.passwordLabel.setBounds(275, 200, 100, 30);
		
		this.userNameInput = new JTextField("library_admin");
		this.userNameInput.setBounds(275, 130, 200, 30);
		
		this.passwordInput = new JPasswordField("abcabc123!");
		this.passwordInput.setBounds(275, 230, 200, 30);
		this.passwordInput.setEchoChar('*');
		
		ActionListener loginListener = e -> { // If both username and password are correct, proceed to the LoginSuccessGui class
			try {
				
				String userName = userNameInput.getText();
				String password = passwordInput.getText();
				
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost/library", userName, password);
				
				con.close();
				
				this.dispose();
				
				this.loggedInGui = new LoginSuccessGui(userName);
				
			} catch (Exception e2) {
				System.out.println(e2);
			}
		};
		
		this.loginButton = new JButton("Log in");
		this.loginButton.setBounds(275, 280, 200, 30);
		this.loginButton.addActionListener(loginListener);
		
		this.mainPanel.add(userNameLabel);
		this.mainPanel.add(userNameInput);
		this.mainPanel.add(passwordLabel);
		this.mainPanel.add(passwordInput);
		this.mainPanel.add(loginButton);
		
		this.add(mainPanel);
		this.setVisible(true);
		this.setResizable(false);
		
	}
	
}
