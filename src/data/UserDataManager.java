package data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import models.User;

/**
 * Handles saving/loading user data
 */
public class UserDataManager {
	
	 /**
     * Load all registered users from the Users tab of the Google sheet.
     */
	public static ArrayList<User> loadUsers(){
		return SheetsServiceUtil.readUsersFromSheets();
	}
	
	/**
     * Save a new user.
     * Adds the user to the existing list in Users tab of the Google sheet.
     */
	public static void saveUser(User user) {
		SheetsServiceUtil.writeUserToSheet(user);
	}

	/**
     * Authenticate a user by checking the provided username and password
     * against the stored list of users.
     * Returns the matched User object or null if not found.
     */
	public static User authenticateUser(String username, String password) {
		ArrayList<User> users = loadUsers();
		for(User u : users) {
			if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
				return u;
			};
		}
		return null;
	}
	
	/**
     * Check if a given username already exists in the user list.
     * Useful during signup to prevent duplicate usernames.
     */
	public static boolean userExists(String username) {
		ArrayList<User> users = loadUsers();
		for(User u : users) {
			if (u.getUsername().equals(username)) {
				return true;
			};
		}
		return false;
	}
}
