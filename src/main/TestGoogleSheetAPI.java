package main;

import java.util.ArrayList;

import data.SheetsServiceUtil;
import models.User;

// spreadsheet Id : 1iZvc_XNtGPA7FtMmm1Jhg-CA7Vb6-zIP-UCsyp1ivBM

public class TestGoogleSheetAPI {
	public static void main(String[] args) {
//		ArrayList<Task> tasks = new ArrayList<>();
//		
//		Task task1 = new Task("Task 1", "eat", LocalDate.now(), PriorityLevel.HIGH);
//		Task task2 = new Task("Task 2", "sleep", LocalDate.now(), PriorityLevel.MEDIUM);
//		Task task3 = new Task("Task 3", "code", LocalDate.now(), PriorityLevel.MEDIUM);
//		Task task4 = new Task("Task 4", "repeat", LocalDate.now(), PriorityLevel.LOW);
//		Task task5 = new Task("Task 5", "die", LocalDate.now(), PriorityLevel.LOW);
//		
//		tasks.add(task1);
//		tasks.add(task2);
//		tasks.add(task3);
//		tasks.add(task4);
//		tasks.add(task5);
		
		String spreadsheetId = "1iZvc_XNtGPA7FtMmm1Jhg-CA7Vb6-zIP-UCsyp1ivBM";
		User user1 = new User("mimm", "12345");
		
		SheetsServiceUtil.writeUserToSheet(user1);
		
		ArrayList<User> users = SheetsServiceUtil.readUsersFromSheets();
		for (User u : users) {
			System.out.println("Username: " + u.getUsername() + ", password: " + u.getPassword());
		}
		
//	    String range = "Sheet1!A1:E";
//	    
//	    SheetsServiceUtil.writeTasksToGoogleSheet(tasks, spreadsheetId, range);
//	 // Read tasks back from sheet
//	    ArrayList<Task> tasksFromSheet = SheetsServiceUtil.readTasksFromGoogleSheet(spreadsheetId, range);
//	    for (Task t : tasksFromSheet) {
//	        System.out.println(t.getTitle() + " | " + t.getDeadline());
//	    }
	}
}
