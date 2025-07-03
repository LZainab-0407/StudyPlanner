package views;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
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
		
		JButton addNewTaskButton = new IconOnlyButton("Add to task list", new ImageIcon("Resources/icons/add_task.png"));
		addNewTaskButton.addActionListener(e -> TaskController.addNewTask(parent, mainContent, ViewContext.CALENDAR));
		
		JButton sortTasksButton = new IconOnlyButton("Sort task list", new ImageIcon("Resources/icons/sort.png"));
		sortTasksButton.addActionListener(e -> {
			JPopupMenu options = generatePopupMenu(mainContent);
			options.show(sortTasksButton, 0, sortTasksButton.getHeight());
		});
		
		JButton searchButton = new IconOnlyButton("Search tasks", new ImageIcon("Resources/icons/search.png"));
		searchButton.addActionListener(e -> {
			SearchController.displaySearchView(mainContent, parent);
		});
		
		this.add(addNewTaskButton);
		this.add(sortTasksButton);
		this.add(searchButton);
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
			TaskController.refresh(mainContent, ViewContext.TASK_LIST, null);
		});
		
		JMenuItem byDeadlineItem = new JMenuItem("By Deadline");
		byDeadlineItem.addActionListener(e -> {
			TaskManager.sortTasksByDeadline();
			TaskManager.saveTasksForUser(UserSession.getCurrentUser().getUsername());
			TaskController.refresh(mainContent, ViewContext.TASK_LIST, null);
		});
		
		sortOptions.add(byPriorityItem);
		sortOptions.add(byDeadlineItem);
		
		return sortOptions;
	}
}
