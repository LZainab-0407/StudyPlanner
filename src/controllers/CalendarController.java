package controllers;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import views.CalendarView;

/**
 * Controller class that handles calendar-related UI updates in the application.
 * <p>
 * This class follows the MVC design pattern and is responsible for 
 * managing the logic of displaying and refreshing the {@code CalendarView}
 * inside the main content panel.
 */
public class CalendarController {
	
	/**
     * Displays the calendar dashboard in the provided main content panel.
     * <p>
     * This method clears the existing content in the panel, 
     * creates a new {@code CalendarView} instance, and places it 
     * in the center of the layout.
     *
     * @param mainContent the panel in which the calendar view should be displayed
     */
	public static void displayCalendar(JPanel mainContent) {
		mainContent.removeAll();
		CalendarView calendarView = new CalendarView(mainContent);
		mainContent.add(calendarView, BorderLayout.CENTER);
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
     */
	public static void refreshCalendar(JPanel mainContent) {
		displayCalendar(mainContent);
	}
	
	public static void refreshDotPopUp() {
		
	}
}
