package data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;

import models.PriorityLevel;
import models.Task;

public class TaskManager {
	private static ArrayList<Task> taskList = new ArrayList<Task>();
	private static ArrayList<Task> latestFilteredTasks = new ArrayList<Task>();
	
	/**
     * Adds a new task to the current task list in memory.
     */
	public static void addTask(Task task) {
		taskList.add(task);
	}
	
	  /**
     * Returns the list of tasks currently in memory.
     */
	public static ArrayList<Task> getTasks(){
		return taskList;
	}
	
	 /**
     * 
     */
	public static ArrayList<Task> getLatestFilteredTasks(){
		return latestFilteredTasks;
	}
	
	/**
	 * Returns a list of all tasks that are due on the specified date.
	 * <p>
	 * This method filters the task list and returns only those tasks
	 * whose deadlines match the given {@code LocalDate}.
	 *
	 * @param date the date to check for task deadlines
	 * @return a list of tasks due on the specified date
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
	
	public static void setFilteredTasks(String keyword, PriorityLevel priorityLevel, String status){
		latestFilteredTasks.clear();
	
		for(Task task: taskList) {
			boolean matches = true;
			
			if(keyword != null && !keyword.isBlank() && !keyword.isEmpty()){
				if (!task.getTitle().toLowerCase().contains(keyword.toLowerCase()) && 
						!task.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
					matches = false;
				}
			}
					
			if(priorityLevel != null && !task.getPriorityLevel().equals(priorityLevel)) {
				matches = false;
			}
			
			//*************************  might have to be changed  ***************************************
			if(status != null && !task.getStatus().toLowerCase().contains(status.toLowerCase())) {
				matches = false;
			}
			
			if (matches) {
				latestFilteredTasks.add(task);
			}
		}
		
	}
	
	/**
	 * Rearranges task list according to priority level.
	 * high priority tasks are at the top'
	 */
	public static void prioritizeTaskList() {
		taskList.sort((t1, t2) -> t1.getPriorityLevel().compareTo(t2.getPriorityLevel()));
	}
	
	/**
	 * Sorts the tasklist according to deadline
	 */
	public static void sortTasksByDeadline() {
		taskList.sort((t1, t2) -> t1.getDeadline().compareTo(t2.getDeadline()));
	}
	
	/**
	 * Save the current user's task list to file
	 * @param username
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
	 * Loads a user's saved task list from a file
	 * @param username
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
     * Clears the current task list from memory.
     */
    public static void clearTasks() {
        taskList.clear();
    }
    
    /**
     * removes current task from task list
     */
    public static void deleteTask(Task task) {
    	taskList.remove(task);
    }
	
}
