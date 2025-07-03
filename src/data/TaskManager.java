package data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
     * Adds a new task to the current task list.
     *
     * @param task the task to be added
     */
	public static void addTask(Task task) {
		taskList.add(task);
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
     * Returns a list of all tasks that are due on the specified date.
     *
     * @param date the date to filter tasks by
     * @return a list of tasks with deadlines matching the given date
     */
	public static ArrayList<Task> getTasksOnDate(LocalDate date){
		ArrayList<Task> tasks = new ArrayList<>();
		
		for(Task t: taskList) {
			if (t.getDeadline().equals(date)){
				tasks.add(t);
			}
		}
		
		return tasks;
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
			
			ArrayList<Task> dueIn7Days = statusIndex == 3 ? getTaskDueInNext(7, taskList) : null;
			ArrayList<Task> dueIn14Days = statusIndex == 4 ? getTaskDueInNext(14, taskList) : null;
			ArrayList<Task> dueIn30Days = statusIndex == 5 ? getTaskDueInNext(30, taskList) : null;
			
			switch(statusIndex) {
			
			case 1: // Overdue
				if(!task.getStatus().equals("Overdue!")) {
					matches = false;
				}
						break; 
						
			case 2: // due today 
					if(!task.getStatus().equals("Due today")) {
						matches = false;
					}
					break; 
			
			case 3: // due in 7 days
					if (dueIn7Days == null || !dueIn7Days.contains(task)) {
						matches = false;
					}
					break;
			
			case 4: // due in 14 days 
					if (dueIn14Days == null || !dueIn14Days.contains(task)) {
						matches = false;
					}
					break;
			
			case 5: // due in 30 days
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
	 * tasks within the deadline window are also included
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
				.filter(task -> {
					LocalDate deadline = task.getDeadline();
					return (deadline != null && !deadline.isBefore(today) && !deadline.isAfter(endDate));
				})
				.collect(Collectors.toList());
		
		return new ArrayList<>(retList);
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
     * Saves the current user's task list to a serialized file in the {@code data/} directory.
     *
     * @param username the username used to name the file
     */
	public static void saveTasksForUser(String username) {
		try {
			// Ensure that the data/ directory exists before writing
            new File("data").mkdirs();
			FileOutputStream output = new FileOutputStream("data/" + username + "_tasks.ser");
			ObjectOutputStream oos = new ObjectOutputStream(output);
			// old task list is removed
			oos.reset();
			// new task list is written to file
			oos.writeObject(taskList);
			oos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	 /**
     * Loads the user's task list from a serialized file in the {@code data/} directory.
     *
     * @param username the username used to locate the file
     */
	@SuppressWarnings("unchecked")
	public static void loadTasksForUser(String username) {
		try {
			FileInputStream input = new FileInputStream("data/" + username + "_tasks.ser");
			ObjectInputStream ois = new ObjectInputStream(input);
			taskList = (ArrayList<Task>) ois.readObject();
			ois.close();
		}catch (Exception e){
			taskList = new ArrayList<>();
		}
	}
	
	/**
     * Clears all tasks from memory (does not affect saved files).
     */
    public static void clearTasks() {
        taskList.clear();
    }
    
    /**
     * Removes the given task from the current task list in memory.
     *
     * @param task the task to delete
     */
    public static void deleteTask(Task task) {
    	taskList.remove(task);
    }
	
}
