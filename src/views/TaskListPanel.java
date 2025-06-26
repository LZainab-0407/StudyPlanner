package views;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import controllers.TaskController;
import data.TaskManager;
import models.PriorityLevel;
import models.Task;
import models.UserSession;

public class TaskListPanel extends JPanel{
	JFrame parentFrame;
	JPanel mainContentPanel;
	
	public TaskListPanel(JPanel mainContentPanel) {
		this.setLayout(new GridLayout(TaskManager.getTasks().size() + 2, 1, 5, 5));
		this.mainContentPanel = mainContentPanel;
//		int i = 1;
//		JTextArea listArea = new JTextArea();
//		listArea.append("Pending Tasks: \n\n");
		
		TaskManager.loadTasksForUser(UserSession.getCurrentUser().getUsername());
		for(Task task : TaskManager.getTasks()) {
			
			//JPanel taskPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			JPanel taskPanel = new JPanel(new GridBagLayout());
			
			JPanel taskInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			JTextArea taskArea = new JTextArea(task.toString());
			taskArea.setEditable(false);
			taskArea.setOpaque(true);
//			taskArea.setLineWrap(true);
//			taskArea.setWrapStyleWord(true);
			
			JScrollPane taskInfoPane = new JScrollPane(taskArea);
			taskInfoPane.setFocusable(false);
			
			taskInfoPanel.add(taskInfoPane);
			
			setPriorityColor(task, taskArea);
			
			JPanel boxPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
			JCheckBox completedBox = new JCheckBox();
			completedBox.setOpaque(false);
			boxPanel.add(completedBox);
			
			if(task.isCompleted()) {
				taskArea.setBackground(new Color(0x7bb66f));
				completedBox.setSelected(true);
			}
			
			completedBox.addActionListener(e -> {
				task.setCompleted(completedBox.isSelected());
				if (completedBox.isSelected()) {
					taskArea.setBackground(new Color(0x7bb66f));
				}
				else {
					setPriorityColor(task, taskArea);
				}
				TaskManager.saveTasksForUser(UserSession.getCurrentUser().getUsername());
			});
			
			
			JPanel deleteButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
			JButton deleteButton = new JButton("Delete");
			deleteButton.setFocusable(false);
			deleteButtonPanel.add(deleteButton);
			deleteButton.addActionListener(e -> {
				TaskManager.deleteTask(task);
				TaskManager.saveTasksForUser(UserSession.getCurrentUser().getUsername());
				TaskController.refresh(mainContentPanel); //***************************************
			});
			
			JPanel editButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
			JButton editButton = new JButton("Edit");
			editButton.setFocusable(false);
			editButtonPanel.add(editButton);
			editButton.addActionListener(e -> {
				TaskController.editTask(task, taskInfoPanel, mainContentPanel);
			});
			
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets(5,5,5,5);
			
			// first row, first column
			gbc.gridx = 0;
			gbc.gridy = 0;
			taskPanel.add(taskInfoPanel, gbc);
			
			// first row, second column
			gbc.gridx = 1;
			taskPanel.add(completedBox, gbc);
			
			// first row, third column
			gbc.gridx = 2;
			taskPanel.add(deleteButtonPanel, gbc);
			
			// first row, fourth column
			gbc.gridx = 3;
			taskPanel.add(editButtonPanel, gbc);
			
			this.add(taskPanel);
			//listArea.append(Integer.toString(i++) + "." + task.toString() + "\n");
		}
		
	}
	
	/**
	 * Sets color of task info depending on priority level
	 * @param task
	 * @param taskArea
	 */
	public static void setPriorityColor(Task task, JTextArea taskArea) {
		switch (task.getPriorityLevel()) {
		case PriorityLevel.HIGH: 
			taskArea.setBackground(new Color(0xe97f7f));
			break;
		case PriorityLevel.MEDIUM: 
			taskArea.setBackground(Color.orange);
			break;
		case PriorityLevel.LOW: 
			taskArea.setBackground(Color.yellow);
			break;
		}
	}
	
}
