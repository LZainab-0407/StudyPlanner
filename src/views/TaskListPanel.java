package views;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import data.TaskManager;
import models.Task;

public class TaskListPanel extends JPanel{
	JFrame parentFrame;
	
	public TaskListPanel() {
		this.setLayout(new BorderLayout());
	
		int i = 1;
		JTextArea listArea = new JTextArea();
		listArea.append("Pending Tasks: \n\n");
		for(Task task : TaskManager.getTasks()) {
			listArea.append(Integer.toString(i++) + "." + task.toString() + "\n");
		}
		
		
		this.add(new JScrollPane(listArea), BorderLayout.CENTER);
	}
}
