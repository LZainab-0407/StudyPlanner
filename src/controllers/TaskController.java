package controllers;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import data.TaskManager;
import models.PriorityLevel;
import models.Task;
import models.UserSession;
import views.TaskFormPanel;
import views.TaskListPanel;
import views.ViewContext;

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
			
			refreshTaskList(mainContent);
		});
		
		// cancel button logic: close window
		cancelButton.addActionListener(e -> {inputFrame.dispose();});	
	}
	
	/**
	 * Displays the task list panel in the main content area.
	 * <p>
	 * This method clears the mainContent panel and replaces it with a
	 * scrollable list of tasks, along with a header label at the top.
	 *
	 * @param mainContent the panel where the task list should be displayed
	 */
	public static void displayTaskList(JPanel mainContent) {
		mainContent.removeAll();
		JLabel pendingTasksLabel = new JLabel("Pending Tasks", SwingConstants.CENTER);
		pendingTasksLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
		mainContent.add(pendingTasksLabel, BorderLayout.NORTH);
		
		TaskManager.loadTasksForUser(UserSession.getCurrentUser().getUsername());
		TaskListPanel taskListPanel = new TaskListPanel(mainContent, TaskManager.getTasks());
		JScrollPane taskListPane = new JScrollPane(taskListPanel);
		mainContent.add(taskListPane, BorderLayout.CENTER);
		mainContent.revalidate();
	    mainContent.repaint();
	}
	
	/**
	 * Refreshes the task list panel after clicking any of the button/checkboxes
	 */
	public static void refreshTaskList(JPanel mainContent) {
		displayTaskList(mainContent);
	}
	
	public static void refresh(JPanel mainContent, ViewContext view) {
		switch (view) {
		case ViewContext.TASK_LIST: refreshTaskList(mainContent); break;
		case ViewContext.CALENDAR: CalendarController.refreshCalendar(mainContent); break;
		}
	}
	/**
	 * Edits a task after it has been created.
	 * Called when the edit button is pressed.
	 * if view is task list, then the task list UI is refreshed,
	 * if view is "calendar view", then calendar UI is refreshed
	 * @param task
	 */
	public static void editTask(Task task, JPanel infoPanel, JPanel mainContent, ViewContext view) {
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
			refresh(mainContent, view);
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
