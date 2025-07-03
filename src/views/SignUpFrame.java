package views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

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

public class SignUpFrame extends JFrame {
	
	public SignUpFrame() {
		this.setSize(new Dimension(500, 500));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setLayout(new BorderLayout());
		
		JPanel topPanel = new JPanel();
		topPanel.setPreferredSize(new Dimension(500, 50));
		JLabel signUpLabel = new JLabel("Sign Up", SwingConstants.CENTER);
		signUpLabel.setFont(new Font("Calibri", Font.PLAIN, 15));
		topPanel.add(signUpLabel);
		this.add(topPanel, BorderLayout.NORTH);
		
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridBagLayout());
		centerPanel.setPreferredSize(new Dimension(250, 250));
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		// first row, first column
		JLabel userNameLabel = new JLabel("Set username: ");
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(5,5,5,5);
		centerPanel.add(userNameLabel, gbc);
		
		// first row, second column
		JTextField usernameField = new JTextField(10);
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		centerPanel.add(usernameField, gbc);
		
		// Second row, first column
		JLabel passwordLabel = new JLabel("Set password: ");
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 0;
		centerPanel.add(passwordLabel, gbc);
		        
		// Second row, second column
		JTextField passwordField = new JTextField();
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		centerPanel.add(passwordField, gbc);
		        
		this.add(centerPanel, BorderLayout.CENTER);
		
		JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
		
		JButton submitButton = new JButton("Submit");
		submitButton.setFocusable(false);
		submitButton.addActionListener(e -> {
			String username = usernameField.getText();
			String password = passwordField.getText();
			if (UserController.signUp(username, password)) { // saves new user and logs in
				this.dispose();
				AppController.onLogInSuccess(UserSession.getCurrentUser());
				JOptionPane.showMessageDialog(this, "Welcome " + username);
			}
			else {
				JOptionPane.showMessageDialog(this, "Username taken. Please choose a different username.");
			}
		});
		bottomPanel.add(submitButton);
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setFocusable(false);
		cancelButton.addActionListener(e -> {
			this.dispose();
			new LogInFrame();
		});
		bottomPanel.add(cancelButton);
		
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
