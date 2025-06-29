package views;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import data.TaskManager;
import models.UserSession;

public class MainFrame extends JFrame {
	
	public MainFrame() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(1000, 600);
		this.setLayout(new BorderLayout());
		this.setLocationRelativeTo(null);
		
		JPanel leftPanel = new JPanel();
		leftPanel.setPreferredSize(new Dimension(150, 500));
		this.add(leftPanel, BorderLayout.WEST);
		
		JPanel mainContent = new JPanel();
		mainContent.setLayout(new BorderLayout());
		this.add(mainContent, BorderLayout.CENTER);
		
		MyMenuBar myMenuBar = new MyMenuBar(this, mainContent);
		this.setJMenuBar(myMenuBar);
		
//		TaskManagerPanel taskManagerPanel = new TaskManagerPanel(this, mainContent);
//		this.add(taskManagerPanel, BorderLayout.EAST);
		
		JPanel rightPanel = generateRightPanel(this, mainContent);
		this.add(rightPanel, BorderLayout.EAST);
		
		this.setVisible(true);
	}
	
	/**
	 * generates a panel that sits on the right side of the main app.
	 * this is like a right hand menu list
	 * @param mainFrame
	 * @param mainContent
	 * @return rightPanel
	 */
	public static JPanel generateRightPanel(JFrame mainFrame, JPanel mainContent) {
		JPanel rightPanel = new JPanel();
		rightPanel.setPreferredSize(new Dimension(150, 500));
		rightPanel.setLayout(new BorderLayout());
		
		TaskManagerPanel taskManagerPanel = new TaskManagerPanel(mainContent);
		rightPanel.add(taskManagerPanel, BorderLayout.NORTH);
		
		JButton logOutButton = new JButton("Log Out");
		logOutButton.setFocusable(false);
		
		logOutButton.addActionListener(e -> {
			TaskManager.saveTasksForUser(UserSession.getCurrentUser().getUsername());
			UserSession.logout();
			mainFrame.dispose();
			new LogInFrame();
		});
		
		rightPanel.add(logOutButton, BorderLayout.SOUTH);
		
		return rightPanel;
	}
}
