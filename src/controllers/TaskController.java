package controllers;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.toedter.calendar.JDateChooser;

import data.TaskManager;
import models.PriorityLevel;
import models.Task;
import models.UserSession;
import views.TaskListPanel;

public class TaskController{

	public static void openAddNewTaskWindow(JFrame parentFrame, JPanel mainContent) {
		
		JFrame inputFrame = new JFrame("Add New Task");
		inputFrame.setSize(500, 500);
		inputFrame.setLocationRelativeTo(parentFrame);
		inputFrame.setLayout(new BorderLayout());
		
		// panel for input
		JPanel inputPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		// first row, first column
		JLabel titleLabel = new JLabel("Title: ");
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(5,5,5,5);
		inputPanel.add(titleLabel, gbc);
		
		// first row, second column
		JTextField titleField = new JTextField();
		gbc.gridx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		inputPanel.add(titleField, gbc);
		
		// second row, first column
		JLabel descriptionLabel = new JLabel("Description: ");
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		inputPanel.add(descriptionLabel, gbc);
		
		// second row, second column
		JTextArea descriptionArea = new JTextArea(4, 10);
		JScrollPane scrollPane = new JScrollPane(descriptionArea);
		gbc.gridx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		inputPanel.add(scrollPane, gbc);
		
		// third row, first column
		JLabel priorityLevelLabel = new JLabel("Priority Level: ");
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.fill = GridBagConstraints.NONE;
		inputPanel.add(priorityLevelLabel, gbc);
		
		// third row, second column
		JComboBox<PriorityLevel> priorityLevelBox = new JComboBox<>(PriorityLevel.values());
		gbc.gridx = 1;
		gbc.fill = GridBagConstraints.NONE;
		inputPanel.add(priorityLevelBox, gbc);
		
		// fourth row, first column
		JLabel deadlineLabel = new JLabel("Deadline: ");
		gbc.gridx = 0;
		gbc.gridy = 3;
		inputPanel.add(deadlineLabel, gbc);
		
		// fourth row, second column
		JDateChooser deadlinePicker = new JDateChooser();
		deadlinePicker.setDateFormatString("dd MMMMM yyyy");
		gbc.gridx = 1;
		inputPanel.add(deadlinePicker, gbc);
		
		inputFrame.add(inputPanel, BorderLayout.CENTER);
		inputFrame.setVisible(true);
		
		// create buttons to save or cancel in a new panel
		JPanel buttonPanel = new JPanel(new FlowLayout());
		JButton saveButton = new JButton("Save Task");
		saveButton.setFocusable(false);
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setFocusable(false);
		
		buttonPanel.add(saveButton);
		buttonPanel.add(cancelButton);
		
		inputFrame.add(buttonPanel, BorderLayout.SOUTH);
		
		// save button logic
		saveButton.addActionListener(e -> {
			String taskTitle = titleField.getText();
			String taskDescription = descriptionArea.getText();
			PriorityLevel taskPriorityLevel = (PriorityLevel) priorityLevelBox.getSelectedItem();
			
			Date selectedDate = deadlinePicker.getDate();
			if (selectedDate == null) {
				JOptionPane.showMessageDialog(inputFrame, "Please select a deadline date.");
				return; // returns from actionPerformed method and newTask is not created
			}
			// convert to LocalDate type referring to local zone
			LocalDate deadlineDate = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			// final deadline will be that day at 2359 hrs
			LocalDateTime taskDeadline = LocalDateTime.of(deadlineDate, LocalTime.of(23, 59));
			
			Task newTask = new Task(taskTitle, taskDescription, taskDeadline, taskPriorityLevel);
			// update task list
			TaskManager.addTask(newTask);
			// save updated task list to file
			TaskManager.saveTasksForUser(UserSession.getCurrentUser().getUsername());
			
			JOptionPane.showMessageDialog(inputFrame, "New task added!");
			inputFrame.dispose();
			
			refresh(mainContent);
		});
		
		// cancel button logic: close window
		cancelButton.addActionListener(e -> {inputFrame.dispose();});	
	}
	
	public static void displayTaskList(JPanel mainContent) {
		mainContent.removeAll();
		JLabel pendingTasksLabel = new JLabel("Pending Tasks", SwingConstants.CENTER);
		mainContent.add(pendingTasksLabel, BorderLayout.NORTH);
		TaskListPanel taskListPanel = new TaskListPanel(mainContent);
		mainContent.add(taskListPanel, BorderLayout.CENTER);
		mainContent.revalidate();
	    mainContent.repaint();
	}
	
	/**
	 * Refreshes the task list panel after clicking any of the button/checkboxes
	 */
	public static void refresh(JPanel mainContent) {
		displayTaskList(mainContent);
	}
	
	/**
	 * Edits a task after it has been created.
	 * Called when the edit button is pressed.
	 * @param task
	 */
	public static void editTask(Task task, JPanel infoPanel, JPanel mainContent) {
		JFrame changeFrame = new JFrame("Edit Task");
		changeFrame.setLocationRelativeTo(null);
		changeFrame.setLayout(new BorderLayout());
		changeFrame.setSize(500, 500);
		
		JPanel editPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		// first row, first column
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(5,5,5,5);
		
		editPanel.add(infoPanel, gbc);
		
		// second row, first column
		gbc.gridy = 1;
		JLabel newTitleLabel = new JLabel("New Title: ");
		editPanel.add(newTitleLabel, gbc);
		
		// third row, first column
		gbc.gridy = 2;
		JLabel newDescriptionLabel = new JLabel("New Description: ");
		editPanel.add(newDescriptionLabel, gbc);
		
		// fourth row, first column
		gbc.gridy = 3;
		JLabel newPriorityLevelLabel = new JLabel("New Priority Level: ");
		editPanel.add(newPriorityLevelLabel, gbc);
		
		// fifth row, first column
		gbc.gridy = 4;
		JLabel newDeadlineLabel = new JLabel("New Deadline: ");
		editPanel.add(newDeadlineLabel, gbc);
		
		// second row, second column
		JTextField titleField = new JTextField();
		titleField.setText(task.getTitle());
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		editPanel.add(titleField, gbc);
		
		// third row, second column
		JTextArea descriptionArea = new JTextArea(4, 10);
		descriptionArea.setText(task.getDescription());
		JScrollPane scrollPane = new JScrollPane(descriptionArea);
		gbc.gridy = 2;
		editPanel.add(scrollPane, gbc);
		
		// fourth row, second column
		JComboBox<PriorityLevel> priorityLevelBox = new JComboBox<>(PriorityLevel.values());
		gbc.gridy = 3;
		gbc.fill = GridBagConstraints.NONE;
		editPanel.add(priorityLevelBox, gbc);
		
		// fifth row, second column
		JDateChooser deadlinePicker = new JDateChooser();
		deadlinePicker.setDateFormatString("dd MMMMM yyyy");
		gbc.gridy = 4;
		editPanel.add(deadlinePicker, gbc);
		
		
		JPanel buttonPanel = new JPanel(new FlowLayout());
		
		JButton saveButton = new JButton("Save Changes");
		saveButton.setFocusable(false);
		buttonPanel.add(saveButton);
		saveButton.addActionListener(e -> {
			task.setTitle(titleField.getText());
			task.setDescription(descriptionArea.getText());
			task.setPriorityLevel((PriorityLevel) priorityLevelBox.getSelectedItem());
			if (deadlinePicker.getDate() != null) {
				Date selectedDate = deadlinePicker.getDate();
				LocalDate deadlineDate = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				LocalDateTime taskDeadline = LocalDateTime.of(deadlineDate, LocalTime.of(23, 59));
				task.setDeadline(taskDeadline);
			}
			changeFrame.dispose();
			TaskManager.saveTasksForUser(UserSession.getCurrentUser().getUsername());
			TaskController.refresh(mainContent);
		});
		
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setFocusable(false);
		// cancel button logic: close window
		cancelButton.addActionListener(e -> {changeFrame.dispose();});
		buttonPanel.add(cancelButton);
		
		changeFrame.add(editPanel, BorderLayout.CENTER);
		changeFrame.add(buttonPanel, BorderLayout.SOUTH);
		changeFrame.setVisible(true);
	}
	
	public static JPanel generateTaskInfoPanel(Task task) {
		JPanel taskInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		JTextArea taskArea = new JTextArea(task.toString());
		taskArea.setEditable(false);
		taskArea.setOpaque(true);
		
		JScrollPane taskInfoPane = new JScrollPane(taskArea);
		taskInfoPane.setFocusable(false);
		
		taskInfoPanel.add(taskInfoPane);
		TaskListPanel.setPriorityColor(task, taskArea);
		return taskInfoPanel;
	}
	
}
