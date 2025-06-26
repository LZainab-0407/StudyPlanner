package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import controllers.AppController;
import controllers.UserController;
import models.UserSession;

public class LogInFrame extends JFrame{
	JTextField usernameField;
	JTextField passwordField;
	
	public LogInFrame() {
		this.setSize(new Dimension(500, 500));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setLayout(new BorderLayout());
		
		JPanel topPanel = new JPanel();
		topPanel.setPreferredSize(new Dimension(500, 50));
		JLabel logInLabel = new JLabel("Log In", SwingConstants.CENTER);
		logInLabel.setFont(new Font("Calibri", Font.PLAIN, 15));
		topPanel.add(logInLabel);
		this.add(topPanel, BorderLayout.NORTH);
		
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridBagLayout());
		centerPanel.setPreferredSize(new Dimension(250, 250));
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		// first row, first column
		JLabel userNameLabel = new JLabel("Username: ");
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(5,5,5,5);
		centerPanel.add(userNameLabel, gbc);
		
		// first row, second column
		usernameField = new JTextField(10);
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
		centerPanel.add(usernameField, gbc);
		
		// Second row, first column
		JLabel passwordLabel = new JLabel("Password: ");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        centerPanel.add(passwordLabel, gbc);
        
        // Second row, second column
        passwordField = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        centerPanel.add(passwordField, gbc);
        
        // third row, first column
        JButton logInButton = new JButton("Log In");
        logInButton.setFocusable(false);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        centerPanel.add(logInButton, gbc);
        
        logInButton.addActionListener(e -> {
        	String username = usernameField.getText();
			String password = passwordField.getText();
			if (UserController.login(username, password)) {
				this.dispose();
				AppController.onLogInSuccess(UserSession.getCurrentUser());
			}
			else {
				JOptionPane.showMessageDialog(this, "Log in failed. Please recheck username and/or password.");
			}
        });
        
        this.add(centerPanel, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel();
        JLabel signUpPrompt = new JLabel("New User?", SwingConstants.CENTER);
        JButton signUpButton = new JButton("Sign Up");
        signUpButton.setFocusable(false);
        
        signUpButton.addActionListener(e -> {
        	AppController.redirectToSignUpPage();
        	this.dispose();
        });
      
		bottomPanel.add(signUpPrompt);
		bottomPanel.add(signUpButton);
		bottomPanel.setPreferredSize(new Dimension(500, 200));
		this.add(bottomPanel, BorderLayout.SOUTH);
		
		JPanel leftPanel = new JPanel();
		leftPanel.setPreferredSize(new Dimension(100, 500));
		this.add(leftPanel, BorderLayout.WEST);
		
		JPanel rightPanel = new JPanel();
		rightPanel.setPreferredSize(new Dimension(100, 500));
		this.add(rightPanel, BorderLayout.EAST);
		
		this.setVisible(true);
	}
}
