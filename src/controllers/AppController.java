package controllers;

import data.TaskManager;
import data.ThemeManager;
import data.UserDataManager;
import models.User;
import models.UserSession;
import views.LogInFrame;
import views.MainFrame;
import views.SignUpFrame;

/**
 * AppController serves as the central point for launching the application
 * and handling major transitions between views, such as login → main UI.
 */
public class AppController {

	/**
     * Starts the application.
     * Called from Main.java at startup.
     */
    public static void startApp() {
    	UserDataManager.loadUserLoggedIn();
    	
    	// if user was logged in when app was closed
    	if(UserDataManager.getIsLoggedIn()) {
    		User user = UserDataManager.getUserLoggedIn();
    		if (UserController.login(user.getUsername(), user.getPassword())) {
				onLogInSuccess(UserSession.getCurrentUser());
			}
    	}
    	
    	// if user logged out before app was closed
    	else {
    		new LogInFrame(); // Launch login view
    	}
    }
    
    /**
     * Called after a successful login.
     * Opens the main dash board, after which the logInFrame should be disposed
     *
     * @param user The successfully authenticated user
     */
    public static void onLogInSuccess(User user) {
    	ThemeManager.loadThemePreference();
    	UserDataManager.setIsLoggedIn(true);
    	new MainFrame();
    }
    
    /**
     * called when the sign up button on log in page is pressed.
     */
    public static void redirectToSignUpPage() {
    	new SignUpFrame();
    }
    
}
