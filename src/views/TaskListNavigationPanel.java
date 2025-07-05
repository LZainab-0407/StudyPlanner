package views;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import controllers.TaskController;
import data.TaskManager;
import models.UserSession;

/**
 * A navigation panel that allows users to switch between 
 * Pending Tasks, Overdue Tasks, Completed Tasks, and All Tasks views.
 */
public class TaskListNavigationPanel extends JPanel{
	private JButton activeButton;
	
	/**
     * Creates a panel with buttons for task list filtering.
     *
     * @param mainContent the main content panel where the task lists are displayed
     */
	public TaskListNavigationPanel(JPanel mainContent) {
		this.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		
		// button for pending task
		JButton pendingTasksButton = new JButton("⏳ Pending Tasks");
		pendingTasksButton.setFocusable(false);
		pendingTasksButton.addActionListener(e -> {
			TaskController.displayTaskList(mainContent, null, ViewContext.TASK_LIST_PENDING);
		});
				
		// button for completed task
		JButton completedTasksButton = new JButton("✅ Completed Tasks");
		completedTasksButton.setFocusable(false);
		completedTasksButton.addActionListener(e -> {
			TaskController.displayTaskList(mainContent, null, ViewContext.TASK_LIST_COMPLETED);
		});
				
		// button for all tasks
		JButton allTasksButton = new JButton("📋 All Tasks");
		allTasksButton.setFocusable(false);
		allTasksButton.addActionListener(e -> {
			TaskController.displayTaskList(mainContent, null, ViewContext.TASK_LIST_ALL);
		});
		
		// button for overdue tasks
		JButton overdueTasksButton = new JButton("❗ Overdue Tasks");
		if(!TaskManager.getOverdueTasks().isEmpty()) {
			overdueTasksButton.setBackground(new Color(0xff99b0));
			overdueTasksButton.setToolTipText("You have overdue tasks!");
		}
		else {
			overdueTasksButton.setToolTipText("You have no overdue tasks. Good job!");
		}
		overdueTasksButton.setFocusable(false);
		overdueTasksButton.addActionListener(e -> {
			TaskController.displayTaskList(mainContent, null, ViewContext.TASK_LIST_OVERDUE);
		});
		
		// highlight the button selected
		ViewContext view = UserSession.getCurentViewContext();
		Color highlightColor = new Color(0xd6befa);
		switch(view) {
			case ViewContext.TASK_LIST_ALL: allTasksButton.setBackground(highlightColor);; break;
			case ViewContext.TASK_LIST_PENDING: pendingTasksButton.setBackground(highlightColor); break;
			case ViewContext.TASK_LIST_COMPLETED: completedTasksButton.setBackground(highlightColor); break;
			case ViewContext.TASK_LIST_OVERDUE: overdueTasksButton.setBackground(highlightColor); break;
			default:
				break;
		}
				
		this.add(pendingTasksButton);
		this.add(overdueTasksButton);
		this.add(completedTasksButton);
		this.add(allTasksButton);
	}
}
