package data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import models.PriorityLevel;
import models.Task;

public class TaskManager {
	private static ArrayList<Task> taskList = new ArrayList<Task>();
	private static ArrayList<Task> completedTaskList = new ArrayList<Task>();
	
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
     * Returns the list of tasks currently in memory.
     */
	public static ArrayList<Task> getCompletedTasks(){
		return completedTaskList;
	}
	
	/*
	 * Rearranges task list according to priority level.
	 * high priority tasks are at the top'
	 */
	public static void prioritizeTaskList() {
		taskList.sort((t1, t2) -> t1.getPriorityLevel().compareTo(t2.getPriorityLevel()));
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
    
    /*
     * removes current task from task list
     */
    public static void deleteTask(Task task) {
    	taskList.remove(task);
    }
	
}
