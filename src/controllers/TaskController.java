package controllers;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
	 * Displays the task list panel in the main content area.
	 * <p>
	 * This method clears the mainContent panel and replaces it with a
	 * scrollable list of tasks, along with a header label at the top.
	 *
	 * @param mainContent the panel to be populated with the task list view
	 */
	public static void displayTaskList(JPanel mainContent) {
		mainContent.removeAll();
		JLabel pendingTasksLabel = new JLabel("Pending Tasks", SwingConstants.CENTER);
		pendingTasksLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
		mainContent.add(pendingTasksLabel, BorderLayout.NORTH);
		
		TaskManager.loadTasksForUser(UserSession.getCurrentUser().getUsername());
		TaskListPanel taskListPanel = new TaskListPanel(mainContent, TaskManager.getTasks(), ViewContext.TASK_LIST);
		
		JScrollPane taskListPane = new JScrollPane(taskListPanel);
		mainContent.add(taskListPane, BorderLayout.CENTER);
		mainContent.revalidate();
	    mainContent.repaint();
	}
	
	 /**
     * Displays the list of tasks that are due on a specific date.
     *
     * @param mainContent  the panel to update
     * @param date         the specific date for which tasks should be displayed
     */
	public static void displayTaskListOnDate(JPanel mainContent, LocalDate date) {
		mainContent.removeAll();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
		JLabel pendingTasksLabel = new JLabel("Tasks due on " + date.format(formatter), SwingConstants.CENTER);
		pendingTasksLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
		mainContent.add(pendingTasksLabel, BorderLayout.NORTH);
		
		TaskManager.loadTasksForUser(UserSession.getCurrentUser().getUsername());
		TaskListPanel taskListPanel = new TaskListPanel(mainContent, TaskManager.getTasksOnDate(date), ViewContext.TASK_LIST_ON_DATE);
		
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
     * Refreshes the view showing tasks due on a specific date.
     *
     * @param mainContent  the main content panel
     * @param date         the date to re-display tasks for
     */
	private static void refreshTaskListOnDate(JPanel mainContent, LocalDate date) {
		displayTaskListOnDate(mainContent, date);
		
	}
	
	 /**
     * Refreshes the full task list view.
     *
     * @param mainContent the panel to be refreshed
     */
	private static void refreshTaskList(JPanel mainContent) {
		displayTaskList(mainContent);
	}
	
	
	 /**
     * Refreshes the main content panel depending on the given view context.
     *
     * @param mainContent   the panel to update
     * @param view          the current UI view context
     * @param date          (optional) relevant date if the view context is date-based, null otherwise
     * @param searchResults (optional) if the view context is search-based, null otherwise 
     */
	public static void refresh(JPanel mainContent, ViewContext view, LocalDate date) {
		switch (view) {
		case ViewContext.TASK_LIST: refreshTaskList(mainContent); break;
		case ViewContext.TASK_LIST_ON_DATE: refreshTaskListOnDate(mainContent, date); break;
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
