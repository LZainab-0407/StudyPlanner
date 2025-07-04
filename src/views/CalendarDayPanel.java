package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;

import controllers.TaskController;
import data.TaskManager;
import models.PriorityLevel;
import models.Task;
import models.UserSession;

/**
 * A single day panel used in the calendar view.
 * <p>
 * Displays the day number and an optional options menu, and is designed to show
 * tasks or indicators related to that date in future enhancements.
 * This panel is styled with custom colors and border, and can be extended
 * with dots, hover effects, or event handling.
 */
public class CalendarDayPanel extends JPanel {
	private final YearMonth currentMonth;
	private final int dayOfMonth;
	private JPanel mainContent;
	
	/**
     * Constructs a calendar panel for a specific day number (1–31).
     *
     * @param day the numeric day of the month (e.g. 1, 15, 30)
     */
	public CalendarDayPanel(int day, YearMonth currentMonth, JPanel mainContent){
		this.dayOfMonth = day;
		this.currentMonth = currentMonth;
		this.mainContent = mainContent;
		this.setLayout(new BorderLayout(5, 5));
		this.setBorder(BorderFactory.createLineBorder(new Color(0x75bfec)));
		this.setBackground(new Color(0xdff2ff));
		
		JPanel topPanel = generateTopPanel(day);
		this.add(topPanel, BorderLayout.NORTH);
		
		JPanel dotsPanel = generateDotsPanel(day);
		this.add(dotsPanel, BorderLayout.CENTER);
	}
	
	/**
     * Creates the top panel of the calendar cell that includes the day label
     * and an icon-only "Options" button which opens a popup menu.
     *
     * @param day the day of the month to display
     * @return a configured JPanel to be used as the top row
     */
	private JPanel generateTopPanel(int day) {
		JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 2));
		topPanel.setBorder(BorderFactory.createLineBorder(new Color(0x75bfec)));
		JLabel dayLabel = new JLabel("  " + String.valueOf(day) );
		dayLabel.setVerticalAlignment(SwingConstants.TOP);
		dayLabel.setHorizontalAlignment(SwingConstants.CENTER);
		dayLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
		dayLabel.setForeground(new Color(0x538eec));
		topPanel.add(dayLabel);
		
		JPopupMenu optionsMenu = generatePopupMenu();
		// add options button
		JButton optionsButton = new IconOnlyButton("Options", new ImageIcon("Resources/icons/menu.png"));
		optionsButton.addActionListener(e -> {
			optionsMenu.show(optionsButton, optionsButton.getWidth(), optionsButton.getHeight());
		});
		topPanel.add(optionsButton);
		
		return topPanel;
	}
	
	 /**
     * Creates a popup menu with actions related to the day (e.g. view tasks, clear tasks).
     *
     * @return a JPopupMenu with day-related actions
     */
	private JPopupMenu generatePopupMenu() {
		JPopupMenu dayMenu = new JPopupMenu();
		
		LocalDate date = LocalDate.of(currentMonth.getYear(), currentMonth.getMonth(), dayOfMonth);
		
		JMenuItem showTasksItem = new JMenuItem("Show all tasks");
		showTasksItem.addActionListener(e -> {
			TaskController.displayTaskList(mainContent, date);
		});
		
		JMenuItem deleteTaskItem = new JMenuItem("Delete all tasks");
		deleteTaskItem.addActionListener(e -> {
			for (Task t : TaskManager.getTasksOnDate(date)) {
				TaskManager.deleteTask(t);
				if(TaskManager.getLatestFilteredTasks().contains(t)) {
					TaskManager.getLatestFilteredTasks().remove(t);
				}
				TaskManager.saveTasksForUser(UserSession.getCurrentUser().getUsername());
				TaskController.refresh(mainContent, ViewContext.CALENDAR, null);
			}
		});
		
		JMenuItem completeTaskItem = new JMenuItem("Complete all tasks");
		completeTaskItem.addActionListener(e ->{
			for (Task t: TaskManager.getTasksOnDate(date)) {
				t.setCompleted(true);
				TaskManager.saveTasksForUser(UserSession.getCurrentUser().getUsername());
				TaskController.refresh(mainContent, ViewContext.CALENDAR, null);
			}
		});
		
		dayMenu.add(showTasksItem);
		dayMenu.add(deleteTaskItem);
		dayMenu.add(completeTaskItem);
		
		return dayMenu;
	}
	
	/**
	 * Generates a small panel containing circular colored dots,
	 * each representing a task due on a specific day.
	 * The color of the dot indicates the task's priority level.
	 *
	 * @param day the day of the month for which to retrieve tasks
	 * @return a transparent JPanel with 0 or more small round dots
	 */
	private JPanel generateDotsPanel(int day) {
		JPanel dotsPanel = new JPanel(new GridLayout(0, 5, 5, 5));
		//JPanel dotsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 2));
		dotsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 5));
		dotsPanel.setOpaque(false);
		
		ArrayList<Task> tasks = TaskManager.getTasksOnDate(currentMonth.atDay(dayOfMonth));
		
		for(Task t: tasks) {
			JPanel dot = new Dot(t, getColorForDot(t), mainContent);
			dotsPanel.add(dot);
		}
		
		return dotsPanel;
	}
	
	/**
	 * Returns a color based on the priority level of the task.
	 *
	 * @param task the task whose priority will determine the color
	 * @return the color associated with the task's priority level
	 */
	private Color getColorForDot(Task task) {
		
		if(task.isCompleted()) {
			return new Color(0x7bb66f);
		}
		PriorityLevel priority = task.getPriorityLevel();
		switch (priority) {
		case PriorityLevel.HIGH: return new Color(0xe12901);
		case PriorityLevel.MEDIUM: return new Color(0xff9100);
		case PriorityLevel.LOW: return new Color(0xffdd00);
		}
		return Color.LIGHT_GRAY;
	}
	
}
