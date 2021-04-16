package libraryPackage;

import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.*;

public class EditEmployees extends JFrame {
	
	private String userName;

	private JPanel mainPanel;
	
	private JLabel passwordLabel;
	private JPasswordField passwordField;
	
	private JLabel searchLabel;
	private JTextField searchField;
	private JButton searchButton;
	
	private JLabel editNameLabel;
	private JTextField editNameField;
	private JButton editNameButton;
	
	private JLabel editAddressLabel;
	private JTextField editAddressField;
	private JButton editAddressButton;
	
	private JLabel editPhone1Label;
	private JTextField editPhone1Field;
	private JButton editPhone1Button;
	
	private JLabel editPhone2Label;
	private JTextField editPhone2Field;
	private JButton editPhone2Button;
	
	private JLabel editWageLabel;
	private JTextField editWageField;
	private JButton editWageButton;
	
	private JLabel editVacationLabel;
	private JTextField editVacationField;
	private JButton editVacationButton;
	
	private JLabel employeeIdLabel;
	private JTextField employeeIdField;
	
	private JTextArea results;
	
	public EditEmployees(String userName) {
		
		this.userName = userName;
		
		this.mainPanel = new JPanel();
		this.mainPanel.setLayout(null);
		
		this.setTitle("Library - Admin");
		this.setSize(800,600);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		this.passwordLabel = new JLabel("Password:");
		this.passwordLabel.setBounds(10, 10, 60, 30);
		
		this.passwordField = new JPasswordField("abcabc123!");
		this.passwordField.setBounds(80, 10, 200, 30);
		this.passwordField.setEchoChar('*');
		
		this.searchLabel = new JLabel("Enter the name of the employee you want to search:");
		this.searchLabel.setBounds(10, 50, 350, 30);
		
		this.searchField = new JTextField("");
		this.searchField.setBounds(10, 80, 200, 30);
		
		this.searchButton = new JButton("Search employee");
		this.searchButton.setBounds(10, 120, 200, 30);
		
		this.editNameLabel = new JLabel("Edit name:");
		this.editNameLabel.setBounds(10, 170, 200, 30);
		
		this.editNameField = new JTextField("");
		this.editNameField.setBounds(10, 200, 200, 30);
		
		this.editNameButton = new JButton("Edit name");
		this.editNameButton.setBounds(10, 240, 200, 30);
		
		this.editAddressLabel = new JLabel("Edit address:");
		this.editAddressLabel.setBounds(10, 290, 200, 30);
		
		this.editAddressField = new JTextField("");
		this.editAddressField.setBounds(10, 320, 200, 30);
		
		this.editAddressButton = new JButton("Edit address");
		this.editAddressButton.setBounds(10, 360, 200, 30);
		
		this.editPhone1Label = new JLabel("Edit 1st phone number:");
		this.editPhone1Label.setBounds(10, 410, 200, 30);
		
		this.editPhone1Field = new JTextField("");
		this.editPhone1Field.setBounds(10, 440, 200, 30);
		
		this.editPhone1Button = new JButton("Edit 1st phone number");
		this.editPhone1Button.setBounds(10, 480, 200, 30);
		
		this.editPhone2Label = new JLabel("Edit 2nd phone number:");
		this.editPhone2Label.setBounds(230, 410, 200, 30);
		
		this.editPhone2Field = new JTextField("");
		this.editPhone2Field.setBounds(230, 440, 200, 30);
		
		this.editPhone2Button = new JButton("Edit 2nd phone number");
		this.editPhone2Button.setBounds(230, 480, 200, 30);
		
		this.editWageLabel = new JLabel("Edit wage:");
		this.editWageLabel.setBounds(230, 290, 200, 30);
		
		this.editWageField = new JTextField("");
		this.editWageField.setBounds(230, 320, 200, 30);
		
		this.editWageButton = new JButton("Edit wage");
		this.editWageButton.setBounds(230, 360, 200, 30);
		
		this.editVacationLabel = new JLabel("Edit vacation days:");
		this.editVacationLabel.setBounds(450, 290, 200, 30);
		
		this.editVacationField = new JTextField("");
		this.editVacationField.setBounds(450, 320, 200, 30);
		
		this.editVacationButton = new JButton("Edit vacation days");
		this.editVacationButton.setBounds(450, 360, 200, 30);
		
		this.employeeIdLabel = new JLabel("ID:");
		this.employeeIdLabel.setBounds(490, 440, 50, 30);
		
		this.employeeIdField = new JTextField("");
		this.employeeIdField.setBounds(520, 440, 50, 30);
		
		this.results = new JTextArea();
		this.results.setBounds(330, 10, 440, 270);
		this.results.setEditable(false);
		
		Pattern pNumbers = Pattern.compile("[0-9]");
		Pattern pLetters = Pattern.compile("[A-Za-z]");
		
		ActionListener searchEmployeeListener = e -> { // Handles user searching employees by name
			String answer = "";
			if (checkInput(this.searchField.getText(), pLetters)) {
				try {
					
					Connection con = DriverManager.getConnection("jdbc:mysql://localhost/library", this.userName, this.passwordField.getText());
					
					PreparedStatement p = con.prepareStatement("SELECT * FROM Employees WHERE Name = ?");
					p.setString(1, searchField.getText());
					ResultSet set = p.executeQuery();
					
					while (set.next()){
	                    answer += ("EmployeeID: " + set.getInt(1) + " \nName: " + set.getString(2) + " \nAddress: " + set.getString(3) +
	                    		" \nPhone number 1: " + set.getInt(4) + " \nPhone number 2: " + set.getInt(5) + "\nWage: " + set.getInt(6) + 
	                    		"\nVacation days left: " + set.getInt(7) + "\n\n");
	                    
	                    this.editNameField.setText(set.getString(2));
	                    this.editAddressField.setText(set.getString(3));
	                    this.editPhone1Field.setText(Integer.toString(set.getInt(4)));
	                    this.editPhone2Field.setText(Integer.toString(set.getInt(5)));
	                    this.editWageField.setText(Integer.toString(set.getInt(6)));
	                    this.editVacationField.setText(Integer.toString(set.getInt(7)));
	                    this.employeeIdField.setText(Integer.toString(set.getInt(1)));
	                }
					
					con.close();
					
					results.setText(answer);
					
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			} else {
				results.setText("Please enter a valid employee name.");
			}
		};
		
		ActionListener editNameListener = e -> { // Handles user editing employee name
			if (!checkInput(this.editNameField.getText(), pLetters) || this.editNameField.getText().equals("")) {
				results.setText("Please enter a valid name.");
			} else {
				try {
					
					Connection con = DriverManager.getConnection("jdbc:mysql://localhost/library", this.userName, this.passwordField.getText());
					
					PreparedStatement p = con.prepareStatement("UPDATE Employees SET Name = ? WHERE EmployeeID = ?");
					p.setString(1, this.editNameField.getText());
					p.setInt(2, Integer.parseInt(this.employeeIdField.getText()));
					p.executeUpdate();
					
					con.close();
					
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		};
		
		ActionListener editAddressListener = e -> { // Handles user editing employee address
			if (this.editAddressField.getText().equals("")) {
				results.setText("Please enter a valid Address.");
			} else {
				try {
					
					Connection con = DriverManager.getConnection("jdbc:mysql://localhost/library", this.userName, this.passwordField.getText());
					
					PreparedStatement p = con.prepareStatement("UPDATE Employees SET Address = ? WHERE EmployeeID = ?");
					p.setString(1, this.editAddressField.getText());
					p.setInt(2, Integer.parseInt(this.employeeIdField.getText()));
					p.executeUpdate();
					
					con.close();
					
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		};
		
		ActionListener editPhone1Listener = e -> { // Handles user editing 1st employee phone number
			if (!checkInput(this.editPhone1Field.getText(), pNumbers) || this.editPhone1Field.getText().equals("")) {
				results.setText("Please enter a valid phone number.");
			} else {
				try {
					
					Connection con = DriverManager.getConnection("jdbc:mysql://localhost/library", this.userName, this.passwordField.getText());
					
					PreparedStatement p = con.prepareStatement("UPDATE Employees SET PhoneNumber1 = ? WHERE EmployeeID = ?");
					p.setInt(1, Integer.parseInt(this.editPhone1Field.getText()));
					p.setInt(2, Integer.parseInt(this.employeeIdField.getText()));
					p.executeUpdate();
					
					con.close();
					
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		};
		
		ActionListener editPhone2Listener = e -> { // Handles user editing 2nd employee phone number
			if (!checkInput(this.editPhone2Field.getText(), pNumbers) || this.editPhone2Field.getText().equals("")) {
				results.setText("Please enter a valid phone number.");
			} else {
				try {
					
					Connection con = DriverManager.getConnection("jdbc:mysql://localhost/library", this.userName, this.passwordField.getText());
					
					PreparedStatement p = con.prepareStatement("UPDATE Employees SET PhoneNumber2 = ? WHERE EmployeeID = ?");
					p.setInt(1, Integer.parseInt(this.editPhone2Field.getText()));
					p.setInt(2, Integer.parseInt(this.employeeIdField.getText()));
					p.executeUpdate();
					
					con.close();
					
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		};
		
		ActionListener editWageListener = e -> { // Handles user editing employee wage
			if (!checkInput(this.editWageField.getText(), pNumbers) || this.editWageField.getText().equals("")) {
				results.setText("Please enter a valid wage.");
			} else {
				try {
					
					Connection con = DriverManager.getConnection("jdbc:mysql://localhost/library", this.userName, this.passwordField.getText());
					
					PreparedStatement p = con.prepareStatement("UPDATE Employees SET Wage = ? WHERE EmployeeID = ?");
					p.setInt(1, Integer.parseInt(this.editWageField.getText()));
					p.setInt(2, Integer.parseInt(this.employeeIdField.getText()));
					p.executeUpdate();
					
					con.close();
					
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		};
		
		ActionListener editVacationListener = e -> { // Handles user editing employee vacation days
			if (!checkInput(this.editVacationField.getText(), pNumbers) || this.editVacationField.getText().equals("")) {
				results.setText("Please enter a valid wage.");
			} else {
				try {
					
					Connection con = DriverManager.getConnection("jdbc:mysql://localhost/library", this.userName, this.passwordField.getText());
					
					PreparedStatement p = con.prepareStatement("UPDATE Employees SET VacationDaysLeft = ? WHERE EmployeeID = ?");
					p.setInt(1, Integer.parseInt(this.editVacationField.getText()));
					p.setInt(2, Integer.parseInt(this.employeeIdField.getText()));
					p.executeUpdate();
					
					con.close();
					
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		};
		
		this.searchButton.addActionListener(searchEmployeeListener);
		this.editNameButton.addActionListener(editNameListener);
		this.editAddressButton.addActionListener(editAddressListener);
		this.editPhone1Button.addActionListener(editPhone1Listener);
		this.editPhone2Button.addActionListener(editPhone2Listener);
		this.editWageButton.addActionListener(editWageListener);
		this.editVacationButton.addActionListener(editVacationListener);
		
		this.mainPanel.add(passwordLabel);
		this.mainPanel.add(passwordField);
		this.mainPanel.add(searchLabel);
		this.mainPanel.add(searchField);
		this.mainPanel.add(searchButton);
		this.mainPanel.add(editNameLabel);
		this.mainPanel.add(editNameField);
		this.mainPanel.add(editNameButton);
		this.mainPanel.add(editAddressLabel);
		this.mainPanel.add(editAddressField);
		this.mainPanel.add(editAddressButton);
		this.mainPanel.add(editPhone1Label);
		this.mainPanel.add(editPhone1Field);
		this.mainPanel.add(editPhone1Button);
		this.mainPanel.add(editPhone2Label);
		this.mainPanel.add(editPhone2Field);
		this.mainPanel.add(editPhone2Button);
		this.mainPanel.add(editWageLabel);
		this.mainPanel.add(editWageField);
		this.mainPanel.add(editWageButton);
		this.mainPanel.add(editVacationLabel);
		this.mainPanel.add(editVacationField);
		this.mainPanel.add(editVacationButton);
		this.mainPanel.add(employeeIdLabel);
		this.mainPanel.add(employeeIdField);
		this.mainPanel.add(results);
		
		this.add(mainPanel);
		this.setVisible(true);
		this.setResizable(false);
		
	}
	
	private boolean checkInput(String input, Pattern p) { // Checking if user input is valid
		
		boolean validInput = false;
		
		Matcher matcher = p.matcher(input);
		
		while (matcher.find()) {
			if (matcher.group().length() != 0) {
				validInput = true;
			}
		}
		
		return validInput;
		
	}
	
}
