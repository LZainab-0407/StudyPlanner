package views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import controllers.StatsController;
import data.TaskManager;
import models.UserSession;

/**
 * MainFrame represents the primary application window that is launched after
 * a successful login. It contains the main content area (center), a left panel,
 * a right-side control panel with buttons, and a top menu bar.
 *
 * The default center view is the calendar week dashboard.
 */
public class MainFrame extends JFrame {
	
	public MainFrame() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(1100, 650);
		this.setLayout(new BorderLayout());
		
		JPanel mainContent = new JPanel();
		mainContent.setLayout(new BorderLayout());
		
		JPanel leftPanel = generateLeftPanel(this, mainContent);
		this.add(leftPanel, BorderLayout.WEST);
		
		CalendarView calendarView = new CalendarView(mainContent, ViewContext.CALENDAR_WEEK);
		UserSession.setCurrentViewContext(ViewContext.CALENDAR_WEEK); 
		mainContent.add(calendarView, BorderLayout.CENTER);
		
		this.add(mainContent, BorderLayout.CENTER);
		
		MyMenuBar myMenuBar = new MyMenuBar(this, mainContent);
		this.setJMenuBar(myMenuBar);
		
		JPanel rightPanel = generateRightPanel(this, mainContent);
		this.add(rightPanel, BorderLayout.EAST);
		
		// save on close
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if(TaskManager.isTaskListModified()) {
					TaskManager.saveTasksForUser(UserSession.getCurrentUser().getUsername());
				}
				System.exit(0);
			}
			
		});
		
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	/**
    * Generates the right-side control panel which contains:
    * <ul>
    * 	<li> Task management buttons (add, sort, etc.) </li>
    * 	<li> A logout button that saves data and returns to login screen </li>
    * </ul>
    *
    * @param mainFrame the parent application frame
    * @param mainContent the central panel to refresh as needed
    * @return a configured JPanel for the right side of the layout
    */
	public static JPanel generateRightPanel(JFrame mainFrame, JPanel mainContent) {

		JPanel rightPanel = new JPanel();
		rightPanel.setPreferredSize(new Dimension(100, 500));
		rightPanel.setLayout(new BorderLayout(10, 10));
		//rightPanel.setBorder(BorderFactory.createLineBorder(new Color(0x0077D1), 2));
		
		// Task manager controls (add task, sort task)
		TaskManagerPanel taskManagerPanel = new TaskManagerPanel(mainContent, mainFrame);
		rightPanel.add(taskManagerPanel, BorderLayout.NORTH);
		
		// Logout button
		JButton logOutButton = new JButton("Log Out");
		logOutButton.setFocusable(false);
		
		logOutButton.addActionListener(e -> {
			//TaskManager.saveTasksForUser(UserSession.getCurrentUser().getUsername());
			if(TaskManager.isTaskListModified()) {
				TaskManager.saveTasksForUser(UserSession.getCurrentUser().getUsername());
			}
			UserSession.logout();
			mainFrame.dispose();
			new LogInFrame();
		});
		
		rightPanel.add(logOutButton, BorderLayout.SOUTH);
		
		return rightPanel;
	}
	
	/**
	    * Generates the right-side control panel which contains:
	    * <ul>
	    * 	<li> A Statistics button that switches to the statistics dashboard </li>
	    * 	<li> A Setting button that switches to settings dashboard </li>
	    * </ul>
	    *
	    * @param mainFrame the parent application frame
	    * @param mainContent the central panel to refresh as needed
	    * @return a configured JPanel for the left side of the layout
	    */
	public static JPanel generateLeftPanel(JFrame mainFrame, JPanel mainContent) {
		JPanel leftPanel = new JPanel();
		leftPanel.setPreferredSize(new Dimension(100, 500));
		leftPanel.setLayout(new GridLayout(6, 1, 0, 2));
		
		IconOnlyButton statsButton = new IconOnlyButton("Show statistics", new ImageIcon("Resources/icons/statistics-64.png"));
		statsButton.addActionListener(e -> {
			StatsController.displayStatsView(mainContent);
		});
		leftPanel.add(statsButton);
		
		return leftPanel;
	}
}
