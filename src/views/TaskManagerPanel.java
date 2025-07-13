package views;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import controllers.SearchController;
import controllers.TaskController;
import data.TaskManager;
import models.UserSession;

public class TaskManagerPanel extends JPanel{
	JFrame parent;
	
	public TaskManagerPanel(JPanel mainContent, JFrame parent){
		this.parent = parent;
		this.setPreferredSize(new Dimension(50, 500));
		
		this.setLayout(new GridLayout(6, 1, 0, 2));
		
		IconOnlyButton addNewTaskButton = new IconOnlyButton("Add new task", new ImageIcon("Resources/icons/add-64-green.png"));
		addNewTaskButton.addActionListener(e -> TaskController.addNewTask(parent, mainContent));
		
		IconOnlyButton showTasklistButton = new IconOnlyButton("Show task list", 
													new ImageIcon("Resources/icons/tasklist-64.png"));
		showTasklistButton.addActionListener(e -> {
			TaskController.displayTaskList(mainContent, null, ViewContext.TASK_LIST_PENDING);
		});
		
		IconOnlyButton sortTasksButton = new IconOnlyButton("Sort task list " + "(Go to task list to see sorted tasks)", 
													new ImageIcon("Resources/icons/sort-64.png"));
		sortTasksButton.addActionListener(e -> {
			JPopupMenu options = generatePopupMenu(mainContent);
			options.show(sortTasksButton, 0, sortTasksButton.getHeight());
		});
		
		IconOnlyButton searchButton = new IconOnlyButton("Search tasks", new ImageIcon("Resources/icons/search-64-arcade.png"));
		searchButton.addActionListener(e -> {
			//UserSession.setCurrentViewContext();
			SearchController.displaySearchView(mainContent, parent);
		});
		
		IconOnlyButton saveButton = new IconOnlyButton("Save now", new ImageIcon("Resources/icons/save-64.png"));
		saveButton.addActionListener(e -> {
			String[] responses = {"Continue", "Cancel"};
			int response = JOptionPane.showOptionDialog(parent, 
							"Changes are saved automatcally to cloud when you log out or close the app.\n" +
							"Click continue to save changes now.",  
							"Save now?",
							JOptionPane.OK_CANCEL_OPTION, 
							JOptionPane.INFORMATION_MESSAGE, 
							new ImageIcon("Resources/icons/save-48.png"), 
							responses, 0);
			switch(response) {
			case 0 : TaskManager.saveTasksForUser(UserSession.getCurrentUser().getUsername()); break;
			case 1: break;
			}
		});
		
		this.add(addNewTaskButton);
		this.add(showTasklistButton);
		this.add(sortTasksButton);
		this.add(searchButton);
		this.add(saveButton);
	}
	
	/**
	 * Generates a popup menu that allows the user to choose how to sort tasks.
	 * <p>
	 * The popup includes options to sort the task list by:
	 * <ul>
	 *   <li>Priority level (HIGH, MEDIUM, LOW)</li>
	 *   <li>Deadline (earliest to latest)</li>
	 * </ul>
	 * When an option is selected, the corresponding sorting method is applied
	 * to the task list, and the UI is refreshed by calling {@code TaskController.refresh(mainContent)}.
	 *
	 * @param mainContent the panel containing the task list, used to trigger a visual refresh
	 * @return a {@link JPopupMenu} containing sorting options as {@link JMenuItem}s
	 */
	public static JPopupMenu generatePopupMenu(JPanel mainContent) {
		JPopupMenu sortOptions = new JPopupMenu();
		JMenuItem byPriorityItem = new JMenuItem("By Priority Level");
		byPriorityItem.addActionListener(e -> {
			TaskManager.prioritizeTaskList();
			TaskManager.saveTasksForUser(UserSession.getCurrentUser().getUsername());
			TaskController.refresh(mainContent, UserSession.getCurentViewContext(), null);
		});
		
		JMenuItem byDeadlineItem = new JMenuItem("By Deadline");
		byDeadlineItem.addActionListener(e -> {
			TaskManager.sortTasksByDeadline();
			TaskManager.saveTasksForUser(UserSession.getCurrentUser().getUsername());
			TaskController.refresh(mainContent, UserSession.getCurentViewContext(), null);
		});
		
		sortOptions.add(byPriorityItem);
		sortOptions.add(byDeadlineItem);
		
		return sortOptions;
	}
}
