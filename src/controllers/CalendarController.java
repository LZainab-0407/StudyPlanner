package controllers;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import models.UserSession;
import views.CalendarView;
import views.ViewContext;

/**
 * Controller class that handles calendar-related UI updates in the application.
 * <p>
 * This class follows the MVC design pattern and is responsible for 
 * managing the logic of displaying and refreshing the {@code CalendarView}
 * inside the main content panel.
 * 
 * @author Labibah Zainab
 */
public class CalendarController {
	
	/**
	 * Displays the calendar dashboard with either month or week view,
	 * depending on the given {@code ViewContext}.
	 *
	 * @param mainContent the main UI panel to show the calendar in
	 * @param view the calendar view mode (month or week)
	 */
	public static void displayCalendar(JPanel mainContent, ViewContext view) {
		UserSession.setCurrentViewContext(view);
		mainContent.removeAll();
		CalendarView calendarView = new CalendarView(mainContent, view);
		mainContent.add(calendarView, BorderLayout.CENTER);
		//ThemeController.applyTheme(mainContent);
		mainContent.revalidate();
	    mainContent.repaint();
	}
	
	
	 /**
     * Refreshes the calendar view by re-displaying it in the main content panel.
     * <p>
     * This is typically called after a state change that affects the calendar,
     * such as editing, adding, or deleting a task.
     *
     * @param mainContent the panel containing the calendar view to be refreshed
     * @param view the calendar view mode (month or week)
     */
	public static void refreshCalendar(JPanel mainContent, ViewContext view) {
		displayCalendar(mainContent, view);
	}
}
