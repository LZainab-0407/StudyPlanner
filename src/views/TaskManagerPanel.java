package views;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import controllers.TaskController;
import data.TaskManager;
import models.UserSession;

public class TaskManagerPanel extends JPanel{
	JButton addNewTaskButton;
	JFrame parentFrame;
	
	public TaskManagerPanel(JPanel mainContent){
//		this.parentFrame = parentFrame;
		this.setPreferredSize(new Dimension(150, 500));
		
		this.setLayout(new GridLayout(10, 1));
		
		addNewTaskButton = new JButton("Add New Task");
		addNewTaskButton.setFocusable(false);
		addNewTaskButton.addActionListener(e -> TaskController.addNewTask(parentFrame, mainContent));
		
		JButton sortTasksButton = new JButton("Sort Task List");
		sortTasksButton.setFocusable(false);
		sortTasksButton.addActionListener(e -> {
			JPopupMenu options = generatePopupMenu(mainContent);
			options.show(sortTasksButton, 0, sortTasksButton.getHeight());
		});
		
		this.add(addNewTaskButton);
		this.add(sortTasksButton);
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
			TaskController.refreshTaskList(mainContent);
		});
		
		JMenuItem byDeadlineItem = new JMenuItem("By Deadline");
		byDeadlineItem.addActionListener(e -> {
			TaskManager.sortTasksByDeadline();
			TaskManager.saveTasksForUser(UserSession.getCurrentUser().getUsername());
			TaskController.refreshTaskList(mainContent);
		});
		
		sortOptions.add(byPriorityItem);
		sortOptions.add(byDeadlineItem);
		
		return sortOptions;
	}
}
