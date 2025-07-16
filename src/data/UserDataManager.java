package data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Properties;

import models.User;
import models.UserSession;

/**
 * Handles saving/loading user data
 */
public class UserDataManager {
	private static Boolean isLoggedIn;
	private static User userLoggedIn;
	private static final String CONFIG_PATH = "config/user.properties";
	
	public static User getUserLoggedIn() {
		return userLoggedIn;
	}
	
	public static Boolean getIsLoggedIn() {
		return isLoggedIn;
	}
	
	/**
	 * Sets isLoggedIn to b when called
	 * @param b
	 */
	public static void setIsLoggedIn(Boolean b) {
		isLoggedIn = b;
	}
	
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
	
	/**
	 * Saves the logged in user when app is closed. 
	 * If user logs out before app is closed, a user with a blank username is saved.
	 * If not, the current user is saved.
	 */
	public static void saveUserLoggedIn() {
		try {
			new File("config").mkdirs(); // ensure file exits
			Properties props = new Properties();
			if (isLoggedIn) {
				props.setProperty("username", UserSession.getCurrentUser().getUsername());
				props.setProperty("password", UserSession.getCurrentUser().getPassword());
				try (FileOutputStream fos = new FileOutputStream(CONFIG_PATH)){
					props.store(fos, "User settings");
				}
			}
			else {
				props.setProperty("username", "");
				props.setProperty("password", "");
				try (FileOutputStream fos = new FileOutputStream(CONFIG_PATH)){
					props.store(fos, "User settings");
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * sets UserLoggedIn to previously logged in user.
	 * If no such user exists (as in user logged out before closing the app), then userLoggedIn is set 
	 * to null, and isLoggedIn is set as false
	 */
	public static void loadUserLoggedIn() {
		File file = new File(CONFIG_PATH);
		if(file.exists()) {
			try(FileInputStream fis = new FileInputStream(file)){
				Properties props = new Properties();
				props.load(fis);
				String username = props.getProperty("username");
				String password = props.getProperty("password");
				if (username != null) {
					isLoggedIn = userExists(username);
					if (isLoggedIn) {
						userLoggedIn = authenticateUser(username, password);
					}
				}
				else {
					isLoggedIn = false;
					userLoggedIn = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			isLoggedIn = false;
			userLoggedIn = null;
		}
	}
}
