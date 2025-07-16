package views;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JPanel;

import controllers.ThemeController;
import data.TaskManager;
import models.Task;

/**
 * A panel that displays a vertical list of TaskPanels.
 * Each task is shown with options to complete, edit, or delete it.
 */
public class TaskListPanel extends JPanel{
	JPanel mainContentPanel;
	
	public TaskListPanel(JPanel mainContent, ArrayList<Task> tasks, ViewContext view) {
		this.setLayout(new GridLayout(TaskManager.getTasks().size() + 2, 1, 5, 5));
		this.mainContentPanel = mainContent;

		for(Task task : tasks) {
			JPanel taskPanel = new TaskPanel(task, mainContentPanel, view);
			this.add(taskPanel);
		}
		ThemeController.applyTheme(this);
	}
	
}
