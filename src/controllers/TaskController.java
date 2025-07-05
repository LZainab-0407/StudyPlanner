package controllers;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.swing.ImageIcon;
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
import views.IconOnlyButton;
import views.TaskFormPanel;
import views.TaskListNavigationPanel;
import views.TaskListPanel;
import views.ViewContext;

/**
* Controller class that handles task-related operations such as
* adding, editing, displaying, and refreshing tasks in the UI.
* <p>
* This controller follows the MVC architecture and manages interactions
* between task data and the visual components (views) of the application.
*/
public class TaskController{
	
	 /**
     * Opens a new window that allows the user to create and add a new task.
     *
     * @param parentFrame  the parent frame to center the popup on
     * @param mainContent  the main content panel to refresh after task creation
     */
	public static void addNewTask(JFrame parentFrame, JPanel mainContent, ViewContext view) {
		
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
			
			refresh(mainContent, view, null);
		});
		
		// cancel button logic: close window
		cancelButton.addActionListener(e -> {inputFrame.dispose();});	
	}
	
	/**
	 * Displays a task list in the main content area.
	 * <p>
	 * This method clears and updates the {@code mainContent} panel with a scrollable
	 * list of tasks. If a {@code date} is provided, only tasks due on that date
	 * will be shown. If {@code date} is {@code null}, all tasks for the user are displayed.
	 * A header label and a "Back to Calendar" button are also added to the view.
	 *
	 * @param mainContent the panel in the main frame where content should be shown
	 * @param date        the specific date to filter tasks by, or {@code null} to show all tasks
	 * @param view        the current {@code ViewContext} in which the tasks are being viewed
	 */
	public static void displayTaskList(JPanel mainContent, LocalDate date, ViewContext view) {
		UserSession.setCurrentViewContext(view);
		mainContent.removeAll();
		JPanel taskListPanel = new JPanel();
		
		if(date != null) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
			JLabel pendingTasksLabel = new JLabel("Tasks due on " + date.format(formatter), SwingConstants.CENTER);
			pendingTasksLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
			mainContent.add(pendingTasksLabel, BorderLayout.NORTH);
			taskListPanel = new TaskListPanel(mainContent, TaskManager.getTasksOnDate(date), ViewContext.TASK_LIST_ON_DATE);
		}
		else {
			JPanel topPanel = new JPanel(new BorderLayout(10, 10));
			topPanel.setPreferredSize(new Dimension(500, 70));
			
			TaskListNavigationPanel taskListNavigationPanel = new TaskListNavigationPanel(mainContent);
			
			JLabel topLabel = new JLabel("", SwingConstants.CENTER);
			topLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
			
			topPanel.add(taskListNavigationPanel, BorderLayout.NORTH);
			topPanel.add(topLabel, BorderLayout.SOUTH);
			
			switch(view) {
			
			case ViewContext.TASK_LIST_ALL: 
				topLabel.setText("Showing All Tasks");
				taskListPanel = new TaskListPanel(mainContent, TaskManager.getTasks(), view);
				break;
			
			case ViewContext.TASK_LIST_PENDING:
				topLabel.setText("Showing Pending Tasks");
				taskListPanel = new TaskListPanel(mainContent, TaskManager.getPendingTasks(), view);
				break;
				
			case ViewContext.TASK_LIST_COMPLETED:
				topLabel.setText("Showing Completed Tasks");
				taskListPanel = new TaskListPanel(mainContent, TaskManager.getCompletedTasks(), view);
				break;
				
			case ViewContext.TASK_LIST_OVERDUE: 
				topLabel.setText("Showing Overdue Tasks");
				taskListPanel = new TaskListPanel(mainContent, TaskManager.getOverdueTasks(), view);
				break;
				
			default: 
				break;
			}
			mainContent.add(topPanel, BorderLayout.NORTH);
		}
		
		JScrollPane taskListPane = new JScrollPane(taskListPanel);
		mainContent.add(taskListPane, BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		buttonPanel.setPreferredSize(new Dimension(500, 50));
		
		JButton backButton = new IconOnlyButton("Go back to calendar", new ImageIcon("Resources/icons/back.png"));
		backButton.addActionListener(e -> {
			CalendarController.displayCalendar(mainContent);
		});
		buttonPanel.add(backButton);
		
		mainContent.add(buttonPanel, BorderLayout.SOUTH);
		mainContent.revalidate();
	    mainContent.repaint();
	}
	
	/**
	 * Refreshes the task list view by re-displaying the appropriate task list,
	 * depending on whether a specific {@code date} was previously active.
	 *
	 * @param mainContent the panel to update
	 * @param date        the specific date to re-display tasks for, or {@code null} for all tasks
	 * @param view        the current {@code ViewContext}, which determines what to refresh
	 */
	private static void refreshTaskList(JPanel mainContent, LocalDate date, ViewContext view) {
		displayTaskList(mainContent, date, view);
		
	}
	
	
	/**
	 * Refreshes the main content panel depending on the user's current view context.
	 * <p>
	 * This acts as a centralized method to rerender content based on a UI event like
	 * saving a task, deleting a task, or marking a task complete.
	 *
	 * @param mainContent   the main panel to refresh
	 * @param view          the current {@code ViewContext}, which determines what to refresh
	 * @param date          the date relevant to the view (e.g., for task list on a specific date),
	 *                      or {@code null} if not applicable
	 */
	public static void refresh(JPanel mainContent, ViewContext view, LocalDate date) {
		switch (view) {
			case ViewContext.TASK_LIST_ALL: refreshTaskList(mainContent, null, view); break;
			case ViewContext.TASK_LIST_PENDING: refreshTaskList(mainContent, null, view); break;
			case ViewContext.TASK_LIST_COMPLETED:refreshTaskList(mainContent, null, view); break;
			case ViewContext.TASK_LIST_OVERDUE:refreshTaskList(mainContent, null, view); break;
			
			case ViewContext.TASK_LIST_ON_DATE: refreshTaskList(mainContent, date, view); break;
			case ViewContext.CALENDAR: CalendarController.refreshCalendar(mainContent); break;
			case ViewContext.SEARCH:  SearchController.refreshSearchResult(mainContent); break;
			default:
				break;
		}
	}
	
	/**
     * Opens an editing window for a given task.
     * <p>
     * The task can be updated and saved. Once saved, the UI is refreshed
     * based on the view context (task list or calendar).
     *
     * @param task         the task to be edited
     * @param infoPanel    the panel containing task preview info (optional for reuse)
     * @param mainContent  the main content panel to refresh after editing
     * @param view         the view context where the edit is being made
     */
	public static void editTask(Task task, JPanel infoPanel, JPanel mainContent, ViewContext view) {
		JFrame changeFrame = new JFrame("Edit Task");
		changeFrame.setLayout(new BorderLayout(20, 20));
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
			refresh(mainContent, view, task.getDeadline());
		});
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setFocusable(false);
		// cancel button logic: close window
		cancelButton.addActionListener(e -> {changeFrame.dispose();});
		buttonPanel.add(cancelButton);
		
		JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
		topPanel.add(infoPanel);
		changeFrame.add(topPanel, BorderLayout.NORTH);
		changeFrame.add(inputPanel, BorderLayout.CENTER);
		changeFrame.add(buttonPanel, BorderLayout.SOUTH);
		changeFrame.setLocationRelativeTo(null);
		changeFrame.setVisible(true);
	}
	
}
