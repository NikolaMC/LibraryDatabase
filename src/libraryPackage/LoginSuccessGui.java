package libraryPackage;

import java.awt.event.ActionListener;

import javax.swing.*;

public class LoginSuccessGui extends JFrame {
	
	private JPanel mainPanel;
	
	private JLabel chooseOptionLabel;
	
	private JButton chooseAdmin;
	private JButton chooseMember;
	
	private Admin adminGui;
	private Member memberGui;
	
	public LoginSuccessGui(String userName) {
		
		this.setTitle("Library");
		this.setSize(800,600);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		this.mainPanel = new JPanel();
		this.mainPanel.setLayout(null);
		
		this.chooseOptionLabel = new JLabel("Please select which one you are");
		this.chooseOptionLabel.setBounds(275, 100, 500, 30);
		
		ActionListener adminListener = e -> { // If user clicks on "Admin", proceed to the Admin class
			this.dispose();
			this.adminGui = new Admin(userName);
		};
		
		ActionListener memberListener = e -> { // If user clicks on "Member", proceed to the Member class
			this.dispose();
			this.memberGui = new Member(userName);
		};
		
		this.chooseAdmin = new JButton("Admin");
		this.chooseAdmin.setBounds(250, 280, 100, 30);
		this.chooseAdmin.addActionListener(adminListener);
		
		this.chooseMember = new JButton("Member");
		this.chooseMember.setBounds(400, 280, 100, 30);
		this.chooseMember.addActionListener(memberListener);
		
		this.mainPanel.add(chooseOptionLabel);
		this.mainPanel.add(chooseAdmin);
		this.mainPanel.add(chooseMember);
		
		this.add(mainPanel);
		this.setVisible(true);
		this.setResizable(false);
		
	}
	
}
