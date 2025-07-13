package data;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import models.PriorityLevel;
import models.Task;

/**
 * The {@code TaskManager} class provides static methods for managing the user's
 * task list. It supports task creation, filtering, sorting, and persistence to file.
 * <p>
 * This class serves as a central data handler for all task-related operations.
 * 
 * @author Labibah Zainab
 */
public class TaskManager {
	/**
	 * the list of tasks currently in memory
	 */
	private static ArrayList<Task> taskList = new ArrayList<Task>();
	
	/**
	 * the most recent list of tasks filtered by search or filter operations in the current user session
	 */
	private static ArrayList<Task> latestFilteredTasks = new ArrayList<Task>();
	
	/**
	 * flag to track unsaved changes
	 */
	private static Boolean taskListModified = false;
	
	/**
     * Adds a new task to the current task list.
     *
     * @param task the task to be added
     */
	public static void addTask(Task task) {
		taskList.add(task);
		taskListModified = true;
	}
	
	/**
	 * marks the task as completed or not completed
	 * 
	 * @param task the task to be marked
	 * @param isCompleted true if task is completed, false otherwise
	 */
	public static void setTaskComplete(Task task, Boolean isCompleted) {
		task.setCompleted(isCompleted);
		taskListModified = true;
	}
	
	/**
	 * Called when a task is edited. this marks taskList as modified
	 * If modified, tasklist is saved to google sheet when the app is closed, 
	 * or user is switched or user logs out
	 * 
	 * @param isModified 
	 */
	public static void flagTaskListAsModified(Boolean isModified) {
		taskListModified = isModified;
	}
	
	/**
	 * Returns true if there are unsaved changes, false otherwise
	 * @return
	 */
	public static Boolean isTaskListModified() {
		return taskListModified;
	}
	
	/**
     * Returns the list of tasks currently in memory.
     *
     * @return the full task list
     */
	public static ArrayList<Task> getTasks(){
		return taskList;
	}
	
	/**
     * Returns the most recent list of tasks filtered by search or filter operations 
     * in the current user session.
     *
     * @return the latest filtered task list
     */
	public static ArrayList<Task> getLatestFilteredTasks(){
		return latestFilteredTasks;
	}
	
	/**
	 * Returns a list of all pending (not completed) tasks.
	 */
	public static ArrayList<Task> getPendingTasks(){
		return taskList.stream().filter(task -> (!task.isCompleted() && !task.getStatus().equalsIgnoreCase("Overdue!")))
				.collect(Collectors.toCollection(ArrayList:: new));
	}
	
	/**
	 * Returns a list of all completed tasks.
	 */
	public static ArrayList<Task> getCompletedTasks() {
		return taskList.stream().filter(task -> task.isCompleted())
				.collect(Collectors.toCollection(ArrayList::new));
	}
	
	/**
	 * Returns a list of all overdue tasks.
	 */
	public static ArrayList<Task> getOverdueTasks() {
		return taskList.stream().filter(task -> task.getStatus().equalsIgnoreCase("Overdue!"))
				.collect(Collectors.toCollection(ArrayList::new));
	}
	
	/**
	 * Returns a map of dates to the number of overdue tasks that were due on each of the past {@code days}.
	 * Only includes tasks whose deadline was on that day and are still not completed.
	 * 
	 * @param days the number of past days to consider (e.g., 7 or 30)
	 * @return a map of LocalDate to count of overdue tasks on that date
	 */
	public static Map<LocalDate, Integer> getOverdueHistory(int days){
		Map<LocalDate, Integer> overdueHistory = new LinkedHashMap<>();
		LocalDate today = LocalDate.now();
		
		// map is initialized to all dates in the range and all overdue history count to 0
		for(int i = days; i >= 1; i--) {
			LocalDate date = today.minusDays(i);
			overdueHistory.put(date, 0);
		}
		
		for(Task t: taskList) {
			if(!t.isCompleted()) {
				LocalDate deadline = t.getDeadline();
				if (overdueHistory.containsKey(deadline) && deadline.isBefore(today)) {
					overdueHistory.put(deadline, overdueHistory.get(deadline) + 1);
				}
			}
		}
		return overdueHistory;
	}
	
	/**
     * Returns a list of all (incomplete) tasks that are due on the specified date.
     *
     * @param date the date to filter tasks by
     * @return a list of tasks with deadlines matching the given date
     */
	public static ArrayList<Task> getTasksOnDate(LocalDate date){
		ArrayList<Task> tasks = new ArrayList<>();
		
		for(Task t: taskList) {
			if (!t.isCompleted() && t.getDeadline().equals(date) ){
				tasks.add(t);
			}
		}
		
		return tasks;
	}
	
	/**
	 * Returns the number of tasks completed today.
	 */
	public static long getNumTasksCompletedToday() {
		LocalDate today = LocalDate.now();
		
		return taskList.stream()
				.filter(task -> task.isCompleted())
				.filter(task -> today.equals(task.getCompletionDate()))
				.count();
	}
	
	/**
	 * Returns the number of tasks completed this week.
	 */
	public static long getNumTasksCompletedThisWeek() {
		LocalDate today = LocalDate.now();
		WeekFields weekFields = WeekFields.of(Locale.getDefault());
		int currentWeek = today.get(weekFields.weekOfWeekBasedYear());
		int currentYear = today.getYear();
		
		return taskList.stream()
				.filter(task -> task.isCompleted())
				.filter(task -> {
					LocalDate completionDate = task.getCompletionDate();
					return  completionDate.get(weekFields.weekOfWeekBasedYear()) == currentWeek && completionDate.getYear() == currentYear;
				}).count();
	}
	
	/**
	 * Returns the task completion rate (completed / total) as a percentage.
	 */
	public static double getCompletionRate() {
		double total = taskList.size();
		if (total == 0) {
			return 0;
		}
		return (getCompletedTasks().size() * 100) / total;
	}
	
	/**
     * Sets the {@code latestFilteredTasks} list based on a combination of keyword, priority level,
     * and status index (representing overdue, due today, or due in X days).
     *
     * @param keyword       the search keyword (may be null)
     * @param priorityLevel the priority level filter (may be null)
     * @param statusIndex   the status index from the status filter dropdown (see {@code SearchView})
     */
	public static void setFilteredTasks(String keyword, PriorityLevel priorityLevel, int statusIndex){
		latestFilteredTasks.clear();
	
		for(Task task: taskList) {
			boolean matches = true;
			
			if(keyword != null && !keyword.isBlank() && !keyword.isEmpty()){
				if (keyword.equalsIgnoreCase("All")) {
					// do nothing
					// all means all the tasks should be in filtered tasks
				}
				else if (!task.getTitle().toLowerCase().contains(keyword.toLowerCase()) && 
						!task.getDescription().toLowerCase().contains(keyword.toLowerCase()) && 
						!task.getStatus().toLowerCase().contains(keyword.toLowerCase())) {
					matches = false;
				}
			}
					
			if(priorityLevel != null && !task.getPriorityLevel().equals(priorityLevel)) {
				matches = false;
			}
			
			ArrayList<Task> completed = statusIndex == 1 ? getCompletedTasks() : null;
			ArrayList<Task> overdue = statusIndex == 2 ? getOverdueTasks() : null;
			ArrayList<Task> dueToday = statusIndex == 3 ? getTasksOnDate(LocalDate.now()) : null;
			ArrayList<Task> dueTomorrow = statusIndex == 4 ? getTasksOnDate(LocalDate.now().plusDays(1)): null;
			ArrayList<Task> dueIn3Days = statusIndex == 5 ? getTaskDueInNext(3, taskList) : null;
			ArrayList<Task> dueThisWeek = statusIndex == 6? getTasksDueThisWeek() : null;
			ArrayList<Task> dueNextWeek = statusIndex == 7? getTasksDueNextWeek(): null;
			ArrayList<Task> dueIn14Days = statusIndex == 8 ? getTaskDueInNext(14, taskList) : null;
			ArrayList<Task> dueIn30Days = statusIndex == 9 ? getTaskDueInNext(30, taskList) : null;
			
			switch(statusIndex) {
			
			case 1: // completed
					if (completed == null || !completed.contains(task)) {
						matches = false;
					}
					break;
			
			case 2: // Overdue
					if(overdue == null || !overdue.contains(task)) {
						matches = false;
					}
					break; 
						
			case 3: // due today 
					if(dueToday == null || !dueToday.contains(task)) {
						matches = false;
					}
					break; 
			
			case 4: // due tomorrow
					if(dueTomorrow == null || !dueTomorrow.contains(task)) {
						matches = false;
					}
					break;
			
			case 5: // due in 3 days
					if(dueIn3Days == null || !dueIn3Days.contains(task)) {
						matches = false;
					}
					break;
					
			case 6: // due this week
				if(dueThisWeek == null || !dueThisWeek.contains(task)) {
					matches = false;
				}
				break;
			
			case 7: // due next week
					if (dueNextWeek == null || !dueNextWeek.contains(task)) {
						matches = false;
					}
					break;
			
			case 8: // due in 14 days 
					if (dueIn14Days == null || !dueIn14Days.contains(task)) {
						matches = false;
					}
					break;
			
			case 9: // due in 30 days
					if (dueIn30Days == null || !dueIn30Days.contains(task)) {
						matches = false;
					}
					break;
			
			case 0: // statusBox selects null, or filter option not used
					// do nothing
					break;
			}
			
			if (matches) {
				latestFilteredTasks.add(task);
			}
		}
		
	}
	
	/**
	 * Returns a list of tasks that are due within the next {@code days} days from today.
	 * <p>
	 * This method filters tasks based on their deadline. Only tasks with a non-null deadline
	 * that falls between today and {@code today + days} (inclusive) are included. Completed
	 * tasks within the deadline window are not included
	 * <p>
	 * The result is returned as an {@code ArrayList<Task>} 
	 *
	 * @param days  the number of days from today to include in the date range (e.g., 7, 15, or 30)
	 * @param tasks the list of tasks to search through
	 * @return an {@code ArrayList<Task>} containing tasks due within the specified date range
	 */
	public static ArrayList<Task> getTaskDueInNext(int days, ArrayList<Task> tasks){
		LocalDate today = LocalDate.now();
		LocalDate endDate = today.plusDays(days);
		List<Task> retList = tasks.stream()
				.filter(task -> !task.isCompleted())
				.filter(task -> {
					LocalDate deadline = task.getDeadline();
					return (deadline != null && !deadline.isBefore(today) && !deadline.isAfter(endDate));
				})
				.collect(Collectors.toList());
		
		return new ArrayList<>(retList);
	}
	
	/**
	 * Returns a list of all incomplete tasks that are due within the current week.
	 * The week starts on Monday and ends on Sunday, following the default locale.
	 *
	 * @return an {@code ArrayList} of tasks due this week and not completed
	 */
	public static ArrayList<Task> getTasksDueThisWeek(){
		LocalDate today = LocalDate.now();
		LocalDate startOfThisWeek = today.with(java.time.DayOfWeek.MONDAY);
		LocalDate endOfThisWeek = startOfThisWeek.plusDays(6);
		
		ArrayList<Task> retList = taskList.stream()
				.filter(task -> !task.isCompleted())
				.filter(task -> {
					LocalDate deadline = task.getDeadline();
					return !deadline.isBefore(startOfThisWeek) && !deadline.isAfter(endOfThisWeek);
			}).collect(Collectors.toCollection(ArrayList::new));
				
		return retList;
	}
	
	/**
	 * Returns a list of all incomplete tasks that are due within the next week.
	 * The next week is the full week immediately after the current one, from next Monday to next Sunday.
	 *
	 * @return an {@code ArrayList} of tasks due next week and not completed
	 */
	public static ArrayList<Task> getTasksDueNextWeek() {
		LocalDate today = LocalDate.now();
		LocalDate startOfNextWeek = today.with(java.time.DayOfWeek.SUNDAY).plusDays(1);
		LocalDate endOfNextWeek = startOfNextWeek.plusDays(6);
		
		ArrayList<Task> retList = taskList.stream()
				.filter(task -> !task.isCompleted())
				.filter(task -> {
					LocalDate deadline = task.getDeadline();
					return !deadline.isBefore(startOfNextWeek) && !deadline.isAfter(endOfNextWeek);
				}).collect(Collectors.toCollection(ArrayList::new));
		
		return retList;
	}
	
	/**
     * Sorts the task list by priority level (HIGH comes first).
     */
	public static void prioritizeTaskList() {
		taskList.sort((t1, t2) -> t1.getPriorityLevel().compareTo(t2.getPriorityLevel()));
	}
	
	/**
     * Sorts the task list by deadline date (due earliest comes first).
     */
	public static void sortTasksByDeadline() {
		taskList.sort((t1, t2) -> t1.getDeadline().compareTo(t2.getDeadline()));
	}
	
	 /**
     * Saves all tasks of the current user to the Google Sheet.
     * Clears any previous task entries of that user from the sheet.
     * 
     * @param username the username used to name the file
     */
	public static void saveTasksForUser(String username) {
		SheetsServiceUtil.writeTasksToSheet(taskList, username);
	}
	
	/**
     * Loads all tasks belonging to the given user from the Google Sheet.
     *
     * @param username the username of the user
     */
	public static void loadTasksForUser(String username) {
		ArrayList<Task> loadedTasks = SheetsServiceUtil.readTasksFromSheet(username);
		// make sure tasklist is empty
		taskList.clear();
		taskList.addAll(loadedTasks);
	}
	
	/**
	 * Returns a color based on the priority level of the task.
	 * </ul>
	 * 	<li> HIGH -> red <li>
	 * 	<li> MEDIUM -> orange <li>
	 * 	<li> LOW -> yellow <li>
	 * </ul>
	 * 
	 * If the task is completed, green color is returned no matter
	 * the priority level
	 *
	 * @param task the task whose priority will determine the color
	 * @return the color associated with the task's priority level (or green if completed)
	 */
	public static Color getColorForTask(Task task) {
		if(task.isCompleted()) {
			return new Color(0x7bb66f); // green
		}
		PriorityLevel priority = task.getPriorityLevel();
		switch (priority) {
		case PriorityLevel.HIGH: return new Color(0xff4d4d); // red
		case PriorityLevel.MEDIUM: return new Color(0xff9100); // orange
		case PriorityLevel.LOW: return new Color(0xffdd00); // yellow
		}
		return Color.LIGHT_GRAY;
	}
	
	/**
     * Clears all tasks from memory (does not affect saved files).
     */
    public static void clearTasks() {
        taskList.clear();
        taskListModified = true;
    }
    
    /**
     * Removes the given task from the current task list in memory.
     *
     * @param task the task to delete
     */
    public static void deleteTask(Task task) {
    	taskList.remove(task);
    	taskListModified = true;
    }
	
}
