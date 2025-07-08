package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Represents the main calendar UI component.
 * Allows users to toggle between a monthly and weekly view,
 * and navigate forward/backward through time.
 * 
 * @author Labibah Zainab
 */
public class CalendarView extends JPanel{
	
	/** Currently displayed month's year and month. */
	private YearMonth currentMonth;
	
	/** Start of currently displayed week. */
	private LocalDate startOfWeek;
	
	/** End of currently displayed week. */
	private LocalDate endOfWeek;
	
	/** Panel grid that contains day cells for the month. */
	private JPanel monthGrid;
	
	/** Panel grid that contains day cells for the week. */
	private JPanel weekGrid;
	
	/** The parent content panel where the calendar is displayed */
	private JPanel mainContent;
	
	/** View toggle button ("Week"/"Month") that is currently active */
	private JButton activeButton;
	
	/** Tracks whether the current view is month or week */
	private ViewContext view;
	
	/**
     * Constructs a calendar view panel depending on {@code ViewContext}.
     * <p>
     * </ul>
     * 	<li> If {@code ViewContext} is CALENDAR_WEEK, weekly view is shown <li>
     * 	<li> If {@code ViewContext} is CALENDAR_MONTH, monthly view is shown <li>
     * </ul>
     *
     * @param mainContent the main content panel where the calendar is displayed
     * @param view the current calendar view context (month/week)
     */
	public CalendarView(JPanel mainContent, ViewContext view){
		this.setLayout(new BorderLayout());
		
		this.currentMonth = YearMonth.now();
		this.startOfWeek = LocalDate.now().with(java.time.DayOfWeek.MONDAY);
		this.endOfWeek = startOfWeek.plusDays(6);
		
		this.mainContent = mainContent;
		this.view = view;
		
		JPanel topPanel = new JPanel(new BorderLayout());
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		
		JPanel navigationPanel = generateNavigationPanel(mainPanel);
		topPanel.add(navigationPanel, BorderLayout.CENTER);
		
		if (view.equals(ViewContext.CALENDAR_WEEK)){
			JPanel headerPanel = generateWeekHeaderPanel(mainPanel);
			weekGrid = generateCalendarWeekGrid();
			mainPanel.add(headerPanel, BorderLayout.NORTH);
			mainPanel.add(weekGrid, BorderLayout.CENTER);
		} else {
			JPanel headerPanel = generateMonthHeaderPanel(mainPanel);
			monthGrid = generateCalendarMonthGrid();
			mainPanel.add(headerPanel, BorderLayout.NORTH);
			mainPanel.add(monthGrid, BorderLayout.CENTER);
		}
		
		this.add(topPanel, BorderLayout.NORTH);
		this.add(mainPanel, BorderLayout.CENTER);
	}
	
	/**
     * Generates the navigation toggle bar between month and week views
     * 
     * @param mainPanel panel where a monthly or weekly view is to be shown
     */
	private JPanel generateNavigationPanel(JPanel mainPanel) {
		JPanel navigationPanel = new JPanel();
		navigationPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		
		JButton weekButton = new JButton("Week");
		weekButton.setFocusable(false);
		weekButton.addActionListener(e -> {
			updateCalendarWeek(mainPanel);
			highlightActiveButton(weekButton);
		});
		
		JButton monthButton = new JButton("Month");
		monthButton.setFocusable(false);
		monthButton.addActionListener(e -> {
			updateCalendarMonth(mainPanel);
			highlightActiveButton(monthButton);
		});
		
		if (view.equals(ViewContext.CALENDAR_WEEK)) {
			activeButton = weekButton;
			highlightActiveButton(weekButton);
		} else {
			activeButton = monthButton;
			highlightActiveButton(monthButton);
		}
		
		navigationPanel.add(weekButton);
		navigationPanel.add(monthButton);
		
		return navigationPanel;
	}

	 /**
     * Generates the month header panel with month name and navigation
     * 
     * @param mainPanel panel where the header panel is to be shown along with a monthly view
     */
	private JPanel generateMonthHeaderPanel(JPanel mainPanel) {
		JPanel headerPanel = new JPanel(new BorderLayout());
		headerPanel.setPreferredSize(new Dimension(500, 70));
		
		JLabel monthLabel = new JLabel(currentMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + 
											" " + currentMonth.getYear(), 
										SwingConstants.CENTER);
		monthLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
		
		JButton prevMonthButton = new IconOnlyButton("Previous month", new ImageIcon("Resources/icons/left-64.png"));
		prevMonthButton.addActionListener(e -> {
			currentMonth = currentMonth.minusMonths(1);
			monthLabel.setText(currentMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + 
					" " + currentMonth.getYear());
			updateCalendarMonth(mainPanel);
		});
		
		JButton nextMonthButton = new IconOnlyButton("Next month", new ImageIcon("Resources/icons/right-64.png"));
		nextMonthButton.addActionListener(e -> {
			currentMonth = currentMonth.plusMonths(1);
			monthLabel.setText(currentMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + 
					" " + currentMonth.getYear());
			updateCalendarMonth(mainPanel);
		});
		
		headerPanel.add(prevMonthButton, BorderLayout.WEST);
		headerPanel.add(monthLabel, BorderLayout.CENTER);
		headerPanel.add(nextMonthButton, BorderLayout.EAST);
		
		return headerPanel;
	}
	
	/**
     * Generates the week header panel with week range and navigation
     * 
     * @param mainPanel panel where the header panel is to be shown along with a weekly view
     */
	private JPanel generateWeekHeaderPanel(JPanel mainPanel) {
		JPanel headerPanel = new JPanel(new BorderLayout());
		headerPanel.setPreferredSize(new Dimension(500, 70));
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d");
		JLabel weekLabel = new JLabel(startOfWeek.format(formatter) + " - " + endOfWeek.format(formatter),
				SwingConstants.CENTER);
		weekLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
		
		JButton prevWeekButton = new IconOnlyButton("Previous week", new ImageIcon("Resources/icons/left-64.png"));
		prevWeekButton.addActionListener(e -> {
			this.startOfWeek = startOfWeek.minusDays(7);
			this.endOfWeek = startOfWeek.plusDays(6);
			weekLabel.setText(startOfWeek.format(formatter) + " - " + endOfWeek.format(formatter));
			updateCalendarWeek(mainPanel);
		});
		
		JButton nextWeekButton = new IconOnlyButton("Next week", new ImageIcon("Resources/icons/right-64.png"));
		nextWeekButton.addActionListener(e -> {
			this.startOfWeek = startOfWeek.plusDays(7);
			this.endOfWeek = startOfWeek.plusDays(6);
			weekLabel.setText(startOfWeek.format(formatter) + " - " + endOfWeek.format(formatter));
			updateCalendarWeek(mainPanel);
		});
		
		headerPanel.add(prevWeekButton, BorderLayout.WEST);
		headerPanel.add(weekLabel, BorderLayout.CENTER);
		headerPanel.add(nextWeekButton, BorderLayout.EAST);
		
		return headerPanel;
	}
	
	/**
     * Generates a panel that has weekday labels (Mon–Sun)
     */
	private JPanel generateWeekdayLabelPanel() {
		JPanel weekLabelPanel = new JPanel(new GridLayout(1, 7)); // 7 columns for 7 days of the week
		
		// add days Mon-Sun
		for(DayOfWeek day : DayOfWeek.values()) {
			JLabel dayLabel = new JLabel(day.getDisplayName(TextStyle.SHORT, Locale.ENGLISH), SwingConstants.CENTER);
			dayLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
			weekLabelPanel.add(dayLabel);
		}
		
		return weekLabelPanel;
	}
	
	 /**
     * Creates the grid view for the entire week.
     */
	private JPanel generateCalendarWeekGrid() {
		JPanel weekGridPanel = new JPanel(new BorderLayout());
		
		JPanel weekLabelPanel = generateWeekdayLabelPanel();
		weekGridPanel.add(weekLabelPanel, BorderLayout.NORTH);
		
		weekGrid = new JPanel(new GridLayout(1, 7));
		weekGrid.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		ArrayList<LocalDate> daysInWeek = new ArrayList<>();
		LocalDate current = startOfWeek;
		for(int i = 1; i <= 7; i++) {
			daysInWeek.add(current);
			current = current.plusDays(1);
		}
		
		for(LocalDate date: daysInWeek) {
			YearMonth datesMonth = YearMonth.of(date.getYear(), date.getMonth());
			CalendarDayPanel dayPanel = new CalendarDayPanel(date.getDayOfMonth(), datesMonth , mainContent, false);
			weekGrid.add(dayPanel);
		}
		
		weekGridPanel.add(weekGrid, BorderLayout.CENTER);
		return weekGridPanel;
	}
	
	/**
     * Creates the grid view for the entire month.
     */
	private JPanel generateCalendarMonthGrid() {
		JPanel calendarGridPanel = new JPanel(new BorderLayout());
		
		JPanel weekLabelPanel = generateWeekdayLabelPanel();
		calendarGridPanel.add(weekLabelPanel, BorderLayout.NORTH);
		
		monthGrid = new JPanel(new GridLayout(0, 7)); // 7 columns for 7 days of the week
		monthGrid.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		LocalDate firstOfMonth = currentMonth.atDay(1); 
		int startDay = firstOfMonth.getDayOfWeek().getValue();
		
		
		// add blanks before 1st of month
		for(int i = 1; i < startDay; i++) {
			monthGrid.add(new JLabel(""));
		}
		
		// add all days of the current month
		int daysInMonth = currentMonth.lengthOfMonth();
		for(int day = 1; day <= daysInMonth; day++) {
			CalendarDayPanel dayPanel = new CalendarDayPanel(day, currentMonth, mainContent, true);
			monthGrid.add(dayPanel);
		}
		
		calendarGridPanel.add(monthGrid, BorderLayout.CENTER);
		
		return calendarGridPanel;
	}
	
	/**
     * Highlights the button (week/month) that is currently selected.
     * 
     * @param button the button that is currently selected
     */
	private void highlightActiveButton(JButton button) {
		if(this.activeButton != null) {
			activeButton.setBackground(Color.WHITE);
		}
		activeButton = button;
		activeButton.setBackground(new Color(0xd6befa)); // lavender
	}
	
	/**
     * Updates the month view and repopulates calendar grid.
     * 
     * @param mainPanel panel where the month view is to be shown
     */
	private void updateCalendarMonth(JPanel mainPanel) {
		mainPanel.removeAll();
		
		JPanel headerPanel = generateMonthHeaderPanel(mainPanel);
		mainPanel.add(headerPanel, BorderLayout.NORTH);
		
		monthGrid = generateCalendarMonthGrid();
		mainPanel.add(monthGrid, BorderLayout.CENTER);
		mainPanel.revalidate();
		mainPanel.repaint();
	}
	
	/**
     * Updates the week view and repopulates calendar grid.
     * 
     * @param mainPanel panel where the week view is to be shown
     */
	private void updateCalendarWeek(JPanel mainPanel) {
		mainPanel.removeAll();
		
		JPanel headerPanel = generateWeekHeaderPanel(mainPanel);
		mainPanel.add(headerPanel, BorderLayout.NORTH);
		
		weekGrid = generateCalendarWeekGrid();
		mainPanel.add(weekGrid, BorderLayout.CENTER);
		
		mainPanel.revalidate();
		mainPanel.repaint();
	}

}
