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

public class Admin extends JFrame {
	
	private String userName;
	
	private final int BORROWED = 1;
	
	private EditEmployees editEmployees;
	
	private JPanel mainPanel;
	
	private JLabel passwordLabel;
	private JPasswordField passwordField;
	
	private JLabel searchFieldLabel;
	private JTextField searchField;
	private JButton searchBooks;
	private JButton searchMembers;
	
	private JButton editEmployeesButton;
	
	private JTextArea results;
	
	public Admin(String userName) {
		
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
		
		this.searchFieldLabel = new JLabel("Search book/member:");
		this.searchFieldLabel.setBounds(400, 10, 180, 30);
		
		this.searchField = new JTextField("");
		this.searchField.setBounds(550, 10, 200, 30);
		
		this.searchBooks = new JButton("Search book");
		this.searchBooks.setBounds(500, 50, 200, 30);
		
		this.searchMembers = new JButton("Search member");
		this.searchMembers.setBounds(500, 90, 200, 30);
		
		this.editEmployeesButton = new JButton("Edit employees");
		this.editEmployeesButton.setBounds(50, 70, 200, 30);
		
		this.results = new JTextArea();
		this.results.setBounds(10, 130, 765, 390);
		this.results.setEditable(false);
		
		Pattern pLetters = Pattern.compile("[A-Za-z]");
		
		ActionListener searchBookListener = e -> { // Handles user searching books by title
			String answer = "";
			if (checkInput(this.searchField.getText(), pLetters)) {
				try {
					
					Connection con = DriverManager.getConnection("jdbc:mysql://localhost/library", this.userName, this.passwordField.getText());
					
					PreparedStatement p = con.prepareStatement("SELECT * FROM Books WHERE Title = ?");
					p.setString(1, searchField.getText());
					ResultSet set = p.executeQuery();
					
					while (set.next()){
	                    answer += ("BookID: " + set.getInt(1) + " \nTitle: " + set.getString(2) + " \nWriter: " + set.getString(3) +
	                    		" \nPages: " + set.getInt(4) + " \nClassification: " + set.getString(5) + " \nBorrowed: " +
	                    		(set.getInt(6) == BORROWED ? "Borrowed\n\n " : "Not borrowed\n\n "));
	                }
					
					PreparedStatement p1 = con.prepareStatement("SELECT Members.Name FROM BorrowedBooks, Books, Members WHERE BorrowedBooks.Book = Books.BookID AND BorrowedBooks.Member = Members.MemberID AND Books.Title = ?");
					p1.setString(1, this.searchField.getText());
					ResultSet set1 = p1.executeQuery();
					
					while (set1.next()) {
						answer += ("Borrowed by: " + set1.getString(1));
					}
					
					con.close();
					
					results.setText(answer);
					
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			} else {
				results.setText("Please enter a valid book name.");
			}
		};
		
		ActionListener searchMemberListener = e -> { // Handles user searching members by name
			String answer = "";
			if (checkInput(this.searchField.getText(), pLetters)) {
				try {
					
					Connection con = DriverManager.getConnection("jdbc:mysql://localhost/library", this.userName, this.passwordField.getText());
					
					PreparedStatement p = con.prepareStatement("SELECT * FROM Members WHERE Name = ?");
					p.setString(1, searchField.getText());
					ResultSet set = p.executeQuery();
					
					while (set.next()){
	                    answer += ("MemberID: " + set.getInt(1) + " \nName: " + set.getString(2) + " \nAddress: " + set.getString(3) +
	                    		" \nPhone number: " + set.getInt(4) + " \nLibrary card number: " + set.getInt(5) + "\n");
	                }
					
					PreparedStatement p1 = con.prepareStatement("SELECT Books.Title FROM BorrowedBooks, Books, Members WHERE BorrowedBooks.Book = Books.BookID AND BorrowedBooks.Member = Members.MemberID AND Members.Name = ?");
					p1.setString(1, this.searchField.getText());
					ResultSet set1 = p1.executeQuery();
					
					while (set1.next()) {
						answer += ("\nBorrowed: " + set1.getString(1));
					}
					
					con.close();
					
					results.setText(answer);
					
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			} else {
				results.setText("Please enter a valid member name.");
			}
		};
		
		ActionListener editEmployeesListener = e -> { // Proceed to the EditEmployees class
			this.dispose();
			this.editEmployees = new EditEmployees(this.userName);
		};
		
		this.searchBooks.addActionListener(searchBookListener);
		this.searchMembers.addActionListener(searchMemberListener);
		this.editEmployeesButton.addActionListener(editEmployeesListener);
		
		this.mainPanel.add(passwordLabel);
		this.mainPanel.add(passwordField);
		this.mainPanel.add(searchFieldLabel);
		this.mainPanel.add(searchField);
		this.mainPanel.add(searchBooks);
		this.mainPanel.add(searchMembers);
		this.mainPanel.add(editEmployeesButton);
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
