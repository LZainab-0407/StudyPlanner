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
import views.TaskFormPanel;
import views.TaskListPanel;

public class TaskController{
	
	/**
	 * opens a new window with options and fields to create and add
	 * a new task
	 * @param parentFrame
	 * @param mainContent
	 */
	public static void addNewTask(JFrame parentFrame, JPanel mainContent) {
		
		JFrame inputFrame = new JFrame("Add New Task");
		inputFrame.setSize(500, 500);
		inputFrame.setLocationRelativeTo(parentFrame);
		inputFrame.setLayout(new BorderLayout());
		
		TaskFormPanel inputPanel = new TaskFormPanel();
		inputPanel.setTitleFieldText("");
		inputPanel.setDescriptionText("");
		inputPanel.setPriorityLevelBoxOption(PriorityLevel.HIGH);
		inputPanel.setDeadlinePickerdate(new Date());
		
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
			String taskTitle = inputPanel.getTitleText();
			String taskDescription = inputPanel.getDescriptionText();
			PriorityLevel taskPriorityLevel = inputPanel.getPriorityLevelFromBox();
			if(inputPanel.getDeadlineFromPicker() == null) {
				JOptionPane.showMessageDialog(inputPanel, "Please select a deadline");
				return; // return from action performed method
			}
			LocalDate taskDeadline = inputPanel.getDeadlineFromPicker();
			
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
		JScrollPane taskListPane = new JScrollPane(taskListPanel);
		mainContent.add(taskListPane, BorderLayout.CENTER);
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
		
		TaskFormPanel inputPanel = new TaskFormPanel();
		inputPanel.setTitleFieldText(task.getTitle());
		inputPanel.setDescriptionText(task.getDescription());
		inputPanel.setPriorityLevelBoxOption(task.getPriorityLevel());
		inputPanel.setDeadlinePickerdate(task.getDeadline());
		
		JPanel buttonPanel = new JPanel(new FlowLayout());
		
		JButton saveButton = new JButton("Save Changes");
		saveButton.setFocusable(false);
		buttonPanel.add(saveButton);
		
		saveButton.addActionListener(e -> {
			task.setTitle(inputPanel.getTitleText());
			task.setDescription(inputPanel.getDescriptionText());
			task.setPriorityLevel(inputPanel.getPriorityLevelFromBox());
			if(inputPanel.getDeadlineFromPicker() == null) {
				JOptionPane.showMessageDialog(inputPanel, "Please select a deadline");
				return; // return from action performed method
			}
			task.setDeadline(inputPanel.getDeadlineFromPicker());
			
			changeFrame.dispose();
			TaskManager.saveTasksForUser(UserSession.getCurrentUser().getUsername());
			TaskController.refresh(mainContent);
		});
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setFocusable(false);
		// cancel button logic: close window
		cancelButton.addActionListener(e -> {changeFrame.dispose();});
		buttonPanel.add(cancelButton);
		
		changeFrame.add(infoPanel, BorderLayout.NORTH);
		changeFrame.add(inputPanel, BorderLayout.CENTER);
		changeFrame.add(buttonPanel, BorderLayout.SOUTH);
		changeFrame.setVisible(true);
	}
	
}
