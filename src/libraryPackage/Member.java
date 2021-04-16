package libraryPackage;

import java.awt.event.ActionListener;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.*;

public class Member extends JFrame {
	
	private String userName;
	
	private final int BORROWED = 1;

	private JPanel mainPanel;
	
	private JLabel passwordLabel;
	private JPasswordField passwordField;
	
	private JLabel searchFieldLabel;
	private JTextField searchField;
	private JButton searchBooks;
	private JButton searchMagazines;
	
	private JLabel borrowBookLabel;
	private JTextField borrowBookField;
	private JButton borrowBook;
	
	private JTextArea results;
	
	public Member(String userName) {
		
		this.userName = userName;
		
		this.mainPanel = new JPanel();
		this.mainPanel.setLayout(null);
		
		this.setTitle("Library - Member");
		this.setSize(800,600);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		this.passwordLabel = new JLabel("Password:");
		this.passwordLabel.setBounds(10, 10, 60, 30);
		
		this.passwordField = new JPasswordField("abcabc123!");
		this.passwordField.setBounds(80, 10, 200, 30);
		this.passwordField.setEchoChar('*');
		
		this.searchFieldLabel = new JLabel("Search book/magazine:");
		this.searchFieldLabel.setBounds(400, 10, 180, 30);
		
		this.searchField = new JTextField("");
		this.searchField.setBounds(550, 10, 200, 30);
		
		this.searchBooks = new JButton("Search book");
		this.searchBooks.setBounds(500, 50, 200, 30);
		
		this.searchMagazines = new JButton("Search magazine");
		this.searchMagazines.setBounds(500, 90, 200, 30);
		
		this.borrowBookLabel = new JLabel("Enter the ID of the book you wish to borrow:");
		this.borrowBookLabel.setBounds(10, 50, 350, 30);
		
		this.borrowBookField = new JTextField("");
		this.borrowBookField.setBounds(10, 90, 200, 30);
		
		this.borrowBook = new JButton("Borrow book");
		this.borrowBook.setBounds(220, 90, 200, 30);
		
		this.results = new JTextArea();
		this.results.setBounds(10, 130, 765, 390);
		this.results.setEditable(false);
		
		Pattern pNumbers = Pattern.compile("[0-9]");
		Pattern pLetters = Pattern.compile("[A-Za-z]");
		
		ActionListener searchBookListener = e -> { // Handles searching books by their titles
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
					
					con.close();
					
					results.setText(answer);
					
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			} else {
				results.setText("Please enter a valid book name.");
			}
		};
		
		ActionListener searchMagazineListener = e -> { // Handles searching magazines by their titles
			String answer = "";
			if (checkInput(this.searchField.getText(), pLetters)) {
				try {
					
					Connection con = DriverManager.getConnection("jdbc:mysql://localhost/library", this.userName, this.passwordField.getText());
					
					PreparedStatement p = con.prepareStatement("SELECT * FROM Magazines WHERE Title = ?");
					p.setString(1, searchField.getText());
					ResultSet set = p.executeQuery();
					
					while (set.next()){
	                    answer += ("MagazineID: " + set.getInt(1) + " \nTitle: " + set.getString(2) + " \nShelf: " + 
	                    		set.getString(3) + " \nRelease date: " + set.getString(4) + "\n\n");
	                }
					
					con.close();
					
					results.setText(answer);
					
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			} else {
				results.setText("Please enter a valid magazine name.");
			}
		};
		
		ActionListener borrowBookListener = e -> { // Handles user borrowing a book
			
			if (this.borrowBookField.getText().equals("") || !checkInput(this.borrowBookField.getText(), pNumbers)) {
				results.setText("Please enter a valid book ID.");
			} else {
			
				PreparedStatement p = null;
				
				try {
	                {
	                	Connection con = DriverManager.getConnection("jdbc:mysql://localhost/library", this.userName, this.passwordField.getText());
	                    p = con.prepareStatement("INSERT INTO BorrowedBooks (Member, Book) VALUE (?, ?)");
	                    p.setInt(1, getUserId(this.userName, con));
	                    p.setInt(2, Integer.parseInt(this.borrowBookField.getText()));
	                    p.executeUpdate();
	                    con.close();
	                }
	                {
	                	Connection con = DriverManager.getConnection("jdbc:mysql://localhost/library", this.userName, this.passwordField.getText());
	                    p = con.prepareStatement("UPDATE Books SET Borrowed = ? WHERE BookID = ?");
	                    p.setInt(2, Integer.parseInt(this.borrowBookField.getText()));
	                    p.setInt(1, BORROWED);
	                    p.executeUpdate();
	                    con.close();
	                }
	            } catch (SQLException throwables) {
	                throwables.printStackTrace();
	                results.setText("Please enter a valid book ID and make sure that book isn't already borrowed.");
	            }
				
			}
			
		};
		
		this.searchBooks.addActionListener(searchBookListener);
		this.searchMagazines.addActionListener(searchMagazineListener);
		this.borrowBook.addActionListener(borrowBookListener);
		
		this.mainPanel.add(passwordLabel);
		this.mainPanel.add(passwordField);
		this.mainPanel.add(searchFieldLabel);
		this.mainPanel.add(searchField);
		this.mainPanel.add(searchBooks);
		this.mainPanel.add(searchMagazines);
		this.mainPanel.add(borrowBookLabel);
		this.mainPanel.add(borrowBookField);
		this.mainPanel.add(borrowBook);
		this.mainPanel.add(results);
		
		this.add(mainPanel);
		this.setVisible(true);
		this.setResizable(false);
		
	}
	
	private int getUserId(String userName, Connection con) { // Get the member ID of the logged in member
		try {
            PreparedStatement p = con.prepareStatement("SELECT MemberID FROM Members WHERE Name = ? ");
            p.setString(1,userName);
            ResultSet set = p.executeQuery();
            if(set.next()){
                return set.getInt(1);
            };
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
        return -1;
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
