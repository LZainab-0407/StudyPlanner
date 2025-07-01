package controllers;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import views.CalendarView;

public class CalendarController {
	
	public static void displayCalendar(JPanel mainContent) {
		mainContent.removeAll();
		JLabel pendingTasksLabel = new JLabel("Pending Tasks", SwingConstants.CENTER);
		mainContent.add(pendingTasksLabel, BorderLayout.NORTH);
		
		CalendarView calendarView = new CalendarView(mainContent);
		
		mainContent.add(calendarView, BorderLayout.CENTER);
		mainContent.revalidate();
	    mainContent.repaint();
	}
	
	public static void refreshCalendar(JPanel mainContent) {
		displayCalendar(mainContent);
	}
}
