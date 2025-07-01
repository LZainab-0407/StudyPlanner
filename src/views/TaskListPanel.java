package views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import controllers.TaskController;
import data.TaskManager;
import models.PriorityLevel;
import models.Task;
import models.UserSession;

/**
 * Represents a task list panel that shows all tasks in the list tasks,
 * along with button to mark it complete, delete it, or edit it.
 */
public class TaskListPanel extends JPanel{
	JPanel mainContentPanel;
	
	public TaskListPanel(JPanel mainContentPanel, ArrayList<Task> tasks) {
		this.setLayout(new GridLayout(TaskManager.getTasks().size() + 2, 1, 5, 5));
		this.mainContentPanel = mainContentPanel;

		//TaskManager.loadTasksForUser(UserSession.getCurrentUser().getUsername());
		for(Task task : tasks) {
			
			JPanel taskPanel = new JPanel(new GridBagLayout());
			
			JPanel taskInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			JTextArea taskArea = new JTextArea(task.toString());
			taskArea.setFont(new Font("Arial", Font.PLAIN, 10));
			taskArea.setEditable(false);
			taskArea.setOpaque(true);
			
			JScrollPane taskInfoPane = new JScrollPane(taskArea);
			taskInfoPane.setFocusable(false);
			
			taskInfoPanel.add(taskInfoPane);
			
			setPriorityColor(task, taskArea);
			
			JPanel statusPanel = new JPanel();
			String status = task.getStatus();
			JLabel statusLabel = new JLabel(status);
			switch (status){
			case "Completed!": statusLabel.setForeground(new Color(0x008000)); break;
			case "Overdue!": statusLabel.setForeground(new Color(0xff2400)); break;
			}
			
			statusPanel.add(statusLabel);
			
			JPanel boxPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
			JCheckBox completedBox = new JCheckBox();
			completedBox.setToolTipText("Complete task");
			completedBox.setIcon(new ImageIcon("Resources/icons/unchecked.png"));
			completedBox.setSelectedIcon(new ImageIcon("Resources/icons/check.png"));
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
				TaskController.refreshTaskList(mainContentPanel);
			});
			
			
			JPanel deleteButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
			JButton deleteButton = new IconOnlyButton("Delete task", new ImageIcon("Resources/icons/delete.png"));
			deleteButtonPanel.add(deleteButton);
			deleteButton.addActionListener(e -> {
				TaskManager.deleteTask(task);
				TaskManager.saveTasksForUser(UserSession.getCurrentUser().getUsername());
				TaskController.refreshTaskList(mainContentPanel); //***************************************
			});
			
			JPanel editButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
			JButton editButton = new IconOnlyButton("Edit task", new ImageIcon("Resources/icons/edit.png"));
			editButtonPanel.add(editButton);
			editButton.addActionListener(e -> {
				TaskController.editTask(task, taskInfoPanel, mainContentPanel);
			});
			
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets(5,5,5,5);
			
			// first row, first column
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.fill = GridBagConstraints.BOTH;
			gbc.weightx = 1.0;
			gbc.weighty = 1.0;
			taskPanel.add(taskInfoPanel, gbc);
			
			// first row, second column
			gbc.gridx = 1;
			gbc.fill = GridBagConstraints.NONE;
			gbc.weightx = 0;
			gbc.weighty = 0;
			taskPanel.add(statusPanel, gbc);
			
			// first row, third column
			gbc.gridx = 2;
			taskPanel.add(completedBox, gbc);
			
			// first row, fourth column
			gbc.gridx = 3;
			taskPanel.add(deleteButtonPanel, gbc);
			
			// first row, fifth column
			gbc.gridx = 4;
			taskPanel.add(editButtonPanel, gbc);
			
			this.add(taskPanel);
		}
		
	}
	
	/**
	 * Sets color of task info depending on priority level
	 * @param task
	 * @param taskArea
	 */
	private void setPriorityColor(Task task, JTextArea taskArea) {
		switch (task.getPriorityLevel()) {
		case PriorityLevel.HIGH: 
			taskArea.setBackground(new Color(0xe97f7f));
			break;
		case PriorityLevel.MEDIUM: 
			taskArea.setBackground(new Color(0xff9100));
			break;
		case PriorityLevel.LOW: 
			taskArea.setBackground(new Color(0xf4c430));
			break;
		}
	}
	
	
	
}
