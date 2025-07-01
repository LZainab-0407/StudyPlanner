package views;

import javax.swing.JFrame;
import javax.swing.JPanel;

import models.Task;

/**
 * A popup window that shows a single task in detail when a calendar dot is clicked.
 * Contains the same interactive controls as the TaskPanel.
 *
 * This popup is used in calendar views to preview and manage individual tasks.
 */
public class DotTaskPopUpFrame extends JFrame{
	/**
	 * The task represented by this dot
	 */
	private Task task;
	
	public DotTaskPopUpFrame(Task task, JPanel mainContent) {
		this.task = task;
		this.setTitle(task.getTitle());
		this.setLocationRelativeTo(null);
		TaskPanel taskPanel = new TaskPanel(task, mainContent, ViewContext.CALENDAR);
		this.add(taskPanel);
		this.pack();
		this.setVisible(true);
	}
}
