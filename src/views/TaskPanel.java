package views;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import controllers.CalendarController;
import controllers.TaskController;
import data.TaskManager;
import models.PriorityLevel;
import models.Task;
import models.UserSession;

/**
 * Represents a UI panel for a single Task, including its details
 * and interactive controls (edit, delete, complete).
 *
 * This panel is reusable in different contexts (e.g., task list or calendar view).
 * Its behavior is adapted using the provided view context.
 */
public class TaskPanel extends JPanel{
	private Task task;
	JTextArea taskArea;
	JPanel mainContent;
	
	public TaskPanel(Task task, JPanel mainContent, ViewContext view) {
		this.task = task;
		this.taskArea = new JTextArea(task.toString());
		
		this.setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		JPanel taskInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		taskArea.setFont(new Font("Arial", Font.PLAIN, 10));
		taskArea.setEditable(false);
		taskArea.setOpaque(true);
		
		JScrollPane taskInfoPane = new JScrollPane(taskArea);
		taskInfoPane.setFocusable(false);
		
		taskInfoPanel.add(taskInfoPane);
		
		setPriorityColor();
		
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
			setPriorityColor();
//			if (completedBox.isSelected()) {
//				taskArea.setBackground(new Color(0x7bb66f));
//			}
//			else {
//				setPriorityColor();
//			}
			TaskManager.saveTasksForUser(UserSession.getCurrentUser().getUsername());
			TaskController.refresh(mainContent, view, task.getDeadline());
		});
		
		JPanel deleteButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JButton deleteButton = new IconOnlyButton("Delete task", new ImageIcon("Resources/icons/delete.png"));
		deleteButtonPanel.add(deleteButton);
		deleteButton.addActionListener(e -> {
			TaskManager.deleteTask(task);
			if(TaskManager.getLatestFilteredTasks().contains(task)) {
				TaskManager.getLatestFilteredTasks().remove(task);
			}
			TaskManager.saveTasksForUser(UserSession.getCurrentUser().getUsername());
			TaskController.refresh(mainContent, view, task.getDeadline());
		});
		
		JPanel editButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JButton editButton = new IconOnlyButton("Edit task", new ImageIcon("Resources/icons/edit.png"));
		editButtonPanel.add(editButton);
		editButton.addActionListener(e -> {
			TaskController.editTask(task, taskInfoPanel, mainContent, view);
		});
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5,5,5,5);
		
		// first row, first column
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		this.add(taskInfoPanel, gbc);
		
		// first row, second column
		gbc.gridx = 1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 0;
		gbc.weighty = 0;
		this.add(statusPanel, gbc);
		
		// first row, third column
		gbc.gridx = 2;
		this.add(completedBox, gbc);
					
		// first row, fourth column
		gbc.gridx = 3;
		this.add(deleteButtonPanel, gbc);
					
		// first row, fifth column
		gbc.gridx = 4;
		this.add(editButtonPanel, gbc);
	}
	
	/**
	 * Sets the background color of the task info area based on priority level.
	 * High = red, Medium = orange, Low = yellow.
	 */
	private void setPriorityColor() {
		taskArea.setBackground(TaskManager.getColorForTask(task));
	}
	
	
}
