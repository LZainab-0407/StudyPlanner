package views;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import data.TaskManager;
import models.UserSession;

/**
 * MainFrame represents the primary application window that is launched after
 * a successful login. It contains the main content area (center), a left panel,
 * a right-side control panel with buttons, and a top menu bar.
 *
 * The default center view is the calendar dashboard.
 */
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
		CalendarView calendarView = new CalendarView();
		mainContent.add(calendarView, BorderLayout.CENTER);
		
		this.add(mainContent, BorderLayout.CENTER);
		
		MyMenuBar myMenuBar = new MyMenuBar(this, mainContent);
		this.setJMenuBar(myMenuBar);
		
		JPanel rightPanel = generateRightPanel(this, mainContent);
		this.add(rightPanel, BorderLayout.EAST);
		
		this.setVisible(true);
	}
	
	/**
    * Generates the right-side control panel which contains:
    * - Task management buttons (add, sort, etc.)
    * - A logout button that saves data and returns to login screen
    *
    * @param mainFrame the parent application frame
    * @param mainContent the central panel to refresh as needed
    * @return a configured JPanel for the right side of the layout
    */
	public static JPanel generateRightPanel(JFrame mainFrame, JPanel mainContent) {
		JPanel rightPanel = new JPanel();
		rightPanel.setPreferredSize(new Dimension(150, 500));
		rightPanel.setLayout(new BorderLayout());
		
		// Task manager controls (add task, sort task)
		TaskManagerPanel taskManagerPanel = new TaskManagerPanel(mainContent);
		rightPanel.add(taskManagerPanel, BorderLayout.NORTH);
		
		// Logout button
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
