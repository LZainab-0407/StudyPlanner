package controllers;

import data.TaskManager;
import data.UserDataManager;
import models.User;
import models.UserSession;

/**
 * Controller class that handles user-related actions,
 * including login, signup, and logout.
 * Acts as the bridge between user views and data management.
 */
public class UserController {
	
	/**
     * Attempts to sign up a new user.
     * If the username already exists, returns false.
     * Otherwise, saves the new user and logs them in.
     *
     * @param username - the username for the new account
     * @param password - the password for the new account
     * @return true if signup successful, false if username exists
     */
	public static boolean signUp(String username, String password) {
		if(UserDataManager.userExists(username)) {
			return false;
		}
		User user = new User(username, password);
		UserDataManager.saveUser(user);
		UserSession.login(user); // automatically login new user
		TaskManager.loadTasksForUser(username); // load user's tasks
		return true;
	}
	
	/**
     * Attempts to log in an existing user.
     * If successful, logs in the user and loads their task data.
     *
     * @param username - the entered username
     * @param password - the entered password
     * @return true if login is successful, false otherwise
     */
	public static boolean login(String username, String password) {
		if(!UserDataManager.userExists(username)) {
			return false;
		}
		User user = UserDataManager.authenticateUser(username, password);
		
		if (user != null) {
			UserSession.login(user); // login user
			TaskManager.loadTasksForUser(username); // load user's tasks
			return true;
		}
		return false;
	}
	
	/**
     * Logs out the currently logged-in user.
     * Saves their task data and clears the session.
     */
	public static void logout() {
		User currentUser = UserSession.getCurrentUser();
		if (currentUser != null) {
			TaskManager.saveTasksForUser(currentUser.getUsername());
		}
		UserSession.logout();
	}
	
	
}
