package views;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * A monthly calendar view component. Currently shows the current month.
 * Days are placed in a 7x7 grid (day names + 6 weeks).
 */
public class CalendarView extends JPanel{
	private YearMonth currentMonth;
	private JPanel calendarGrid;
	//private JFrame parentFrame;
	//private JPanel mainContent;
	
	public CalendarView(){
		// this.parentFrame = parentFrame;
		this.setLayout(new BorderLayout());
		this.currentMonth = YearMonth.now();
		
		addHeader();
		drawCalendar();
		
	}
	

    /**
     * Adds the top bar to calendar view with month navigation.
     */
	private void addHeader() {
		JPanel headerPanel = new JPanel(new BorderLayout());
		
		JLabel monthLabel = new JLabel(currentMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + 
											" " + currentMonth.getYear(), 
										SwingConstants.CENTER);
		monthLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
		
		JButton prevMonthButton = new JButton("←");
		prevMonthButton.setFocusable(false);
		prevMonthButton.addActionListener(e -> {
			currentMonth = currentMonth.minusMonths(1);
			monthLabel.setText(currentMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + 
					" " + currentMonth.getYear());
			updateCalendar();
		});
		
		JButton nextMonthButton = new JButton("→");
		nextMonthButton.setFocusable(false);
		nextMonthButton.addActionListener(e -> {
			currentMonth = currentMonth.plusMonths(1);
			monthLabel.setText(currentMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + 
					" " + currentMonth.getYear());
			updateCalendar();
		});
		
		headerPanel.add(prevMonthButton, BorderLayout.WEST);
		headerPanel.add(monthLabel, BorderLayout.CENTER);
		headerPanel.add(nextMonthButton, BorderLayout.EAST);
		this.add(headerPanel, BorderLayout.NORTH);
	}
	
	 /**
     * Builds the calendar grid for the current month.
     */
	private void drawCalendar() {
		if (this.calendarGrid != null) {
			this.remove(calendarGrid);
		}
		
		calendarGrid = new JPanel(new GridLayout(0, 7)); // 7 columns for 7 days of the week
		calendarGrid.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		// add days Sun-Sat
		for(DayOfWeek day : DayOfWeek.values()) {
			JLabel dayLabel = new JLabel(day.getDisplayName(TextStyle.SHORT, Locale.ENGLISH), SwingConstants.CENTER);
			dayLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
			calendarGrid.add(dayLabel);
		}
		
		LocalDate firstOfMonth = currentMonth.atDay(1); 
		int startDay = firstOfMonth.getDayOfWeek().getValue();
		
		
		// add blanks before 1st of month
		for(int i = 1; i < startDay; i++) {
			calendarGrid.add(new JLabel(""));
		}
		
		// add all days of the current month
		int daysInMonth = currentMonth.lengthOfMonth();
		for(int day = 1; day <= daysInMonth; day++) {
			int finalDay = day; // for use in lambda expression
			
			JButton dayButton = new JButton(String.valueOf(day));
			dayButton.setHorizontalAlignment(SwingConstants.RIGHT);
			dayButton.setVerticalAlignment(SwingConstants.TOP);
			
			dayButton.setFocusPainted(false);
			dayButton.setMargin(new Insets(5, 5, 5, 5));
			dayButton.addActionListener(e -> {
				LocalDate selectedDate = currentMonth.atDay(finalDay);
				// display tasks due on this day
				
			});
			calendarGrid.add(dayButton);
		}
		
		this.add(calendarGrid, BorderLayout.CENTER);
		this.revalidate();
		this.repaint();
	}
	
	/**
     * Refreshes the calendar when month is changed.
     */
	private void updateCalendar() {
		drawCalendar();
	}

}
