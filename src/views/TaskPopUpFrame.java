package views;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import models.Task;

/**
 * A popup window that shows a single task in detail when a calendar dot is clicked.
 * Contains the same interactive controls as the TaskPanel.
 *
 * This popup is used in calendar views to preview and manage individual tasks.
 */
public class TaskPopUpFrame extends JFrame{
	/**
	 * The task represented by this dot
	 */
	private Task task;
	
	public TaskPopUpFrame(Task task, JPanel mainContent, ViewContext view) {
		this.task = task;
		this.setTitle(task.getTitle());
		this.setLocationRelativeTo(null);
		TaskPanel taskPanel = new TaskPanel(task, mainContent, view);
		this.add(taskPanel);
		this.pack();
		this.setVisible(true);
	}
}
