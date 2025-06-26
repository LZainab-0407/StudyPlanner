package models;

import java.io.Serializable;

/**
 * Models a user
 */
public class User implements Serializable{
	private static final long serialVersionUID = 1L;
	private String username;
	private String password;
	
	public User(String username, String password) {
		this.username = username;
		this.setPassword(password);
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
