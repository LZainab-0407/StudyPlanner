package models;

import views.ViewContext;

/**
 * Holds the logged-in user
 */
public class UserSession {
	private static User currentUser;
	private static ViewContext currentView;
	
	public static void login(User user) {
		currentUser = user;
	}
	
	public static ViewContext getCurentViewContext() {
		return currentView;
	}
	
	public static void setCurrentViewContext(ViewContext view) {
		currentView = view;
	}

	public static User getCurrentUser() {
		return currentUser;
	}
	
	public static void logout() {
		currentUser = null;
	}
}
