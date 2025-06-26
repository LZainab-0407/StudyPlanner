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
	// File path where all user data will be stored
	private static final String FILE_PATH = "data/users.ser";
	
	 /**
     * Load all registered users from the file.
     * Returns an empty list if the file does not exist or is corrupted.
     */
	@SuppressWarnings("unchecked")
	public static ArrayList<User> loadUsers(){
		try {
			FileInputStream input = new FileInputStream(FILE_PATH);
			ObjectInputStream ois = new ObjectInputStream(input);
			ArrayList<User> returnList = (ArrayList<User>) ois.readObject();
			ois.close();
			return returnList;
		} catch (Exception e) {
			return new ArrayList<User>();
		}
	}
	
	/**
     * Save a new user to the user file.
     * Adds the user to the existing list and writes the full list to disk.
     */
	public static void saveUser(User user) {
		ArrayList<User> users = loadUsers();
		users.add(user);
		try {
			// Ensure data/ directory exists
	        new File("data").mkdirs();

			FileOutputStream output = new FileOutputStream(FILE_PATH);
			ObjectOutputStream oos = new ObjectOutputStream(output);
			oos.writeObject(users);
			oos.close();
		}catch (IOException e) {
			e.printStackTrace();
		}
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
