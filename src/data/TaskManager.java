package data;

import java.util.ArrayList;

import models.Task;

public class TaskManager {
	private static ArrayList<Task> taskList = new ArrayList<Task>();
	
	public static void addTask(Task task) {
		taskList.add(task);
	}
	
	public static ArrayList<Task> getTasks(){
		return taskList;
	}
	
}
