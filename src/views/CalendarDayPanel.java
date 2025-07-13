package views;

import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import controllers.TaskController;
import controllers.ThemeController;
import data.TaskManager;
import models.Task;
import models.UserSession;

/**
 * A panel representing a single day in the calendar grid.
 * Displays task indicators or an optional scrollable task list depending on the view context.
 * 
 * @author Labibah Zainab
 */
public class CalendarDayPanel extends JPanel {
	private final YearMonth currentMonth;
	private final int dayOfMonth;
	private JPanel mainContent;
	
	 /**
     * Constructs a day panel for a specific day in a calendar.
     *
     * @param dayOfMonth the day number
     * @param currentMonth the month-year of the calendar
     * @param mainContent the main panel to update when actions are triggered
     * @param isMonth whether the panel is in month or week view, true if month view, false if week view
     */
	public CalendarDayPanel(int day, YearMonth currentMonth, JPanel mainContent, boolean isMonth){
		this.dayOfMonth = day;
		this.currentMonth = currentMonth;
		this.mainContent = mainContent;
		this.setLayout(new BorderLayout());
		
		this.setBackground(new Color(0xdff2ff)); // blue
	
		JPanel topPanel = generateTopPanel(day);
		this.add(topPanel, BorderLayout.NORTH);
		
		if (isMonth) {
			JPanel dotsPanel = generateDotsPanel(day);
			this.add(dotsPanel, BorderLayout.CENTER);
			// highlight today
			if(LocalDate.now().equals(LocalDate.of(currentMonth.getYear(), currentMonth.getMonth(), dayOfMonth))) {
				this.setBackground(new Color(0xd6befa)); // lavender
			}
		}
		else {
			JPanel taskTitleListPanel = generateTaskTitleListPanel(mainContent);
			JScrollPane scrollPane = new JScrollPane(taskTitleListPanel);
			scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			// highlight today
			if(LocalDate.now().equals(LocalDate.of(currentMonth.getYear(), currentMonth.getMonth(), dayOfMonth))) {
				topPanel.setBackground(new Color(0xd6befa)); // lavender
			}
			this.add(scrollPane, BorderLayout.CENTER);
		}
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
		
		ThemeController.applyTheme(topPanel);
		
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
			TaskController.displayTaskList(mainContent, date, ViewContext.TASK_LIST_ON_DATE);
		});
		
		JMenuItem deleteTaskItem = new JMenuItem("Delete all tasks");
		deleteTaskItem.addActionListener(e -> {
			for (Task t : TaskManager.getTasksOnDate(date)) {
				TaskManager.deleteTask(t);
				if(TaskManager.getLatestFilteredTasks().contains(t)) {
					TaskManager.getLatestFilteredTasks().remove(t);
				}
				TaskManager.saveTasksForUser(UserSession.getCurrentUser().getUsername());
				TaskController.refresh(mainContent, UserSession.getCurentViewContext(), null);
			}
		});
		
		JMenuItem completeTaskItem = new JMenuItem("Complete all tasks");
		completeTaskItem.addActionListener(e ->{
			for (Task t: TaskManager.getTasksOnDate(date)) {
				t.setCompleted(true);
				TaskManager.saveTasksForUser(UserSession.getCurrentUser().getUsername());
				TaskController.refresh(mainContent, UserSession.getCurentViewContext(), null);
			}
		});
		
		dayMenu.add(showTasksItem);
		dayMenu.add(deleteTaskItem);
		dayMenu.add(completeTaskItem);
		
		return dayMenu;
	}
	
	/**
	 * Generates a small panel containing circular colored clickable dots,
	 * each representing a task due on a specific day.
	 * The color of the dot indicates the task's priority level.
	 *
	 * @param day the day of the month for which to retrieve tasks
	 * @return a transparent JPanel with 0 or more small round dots
	 */
	private JPanel generateDotsPanel(int day) {
		JPanel dotsPanel = new JPanel(new GridLayout(0, 5, 5, 0));
		//JPanel dotsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 2));
		//dotsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 5));
		dotsPanel.setBorder(BorderFactory.createLineBorder(new Color(0x75bfec)));
		dotsPanel.setOpaque(false);
		
		// show pending tasks
		for(Task t: TaskManager.getTasksOnDate(currentMonth.atDay(dayOfMonth))) {
			JPanel dot = new Dot(t, TaskManager.getColorForTask(t), mainContent);
			dotsPanel.add(dot);
		}
		
		// show completed tasks as well
		for(Task t: TaskManager.getCompletedTasks()) {
			if(t.getDeadline().equals(currentMonth.atDay(dayOfMonth))) {
				JPanel dot = new Dot(t, TaskManager.getColorForTask(t), mainContent);
				dotsPanel.add(dot);
			}
		}
		
		return dotsPanel;
	}
	
	 /**
     * Builds a panel containing colored clickable task information panels for the week view.
     * The color of the panel indicates the task's priority level
     * 
     * @param mainContent the main panel to update when actions are triggered
     */
	private JPanel generateTaskTitleListPanel(JPanel mainContent) {
		ArrayList<Task> tasks = TaskManager.getTasksOnDate(currentMonth.atDay(dayOfMonth));
		JPanel TaskTitleListPanel = new JPanel((new GridLayout(tasks.size() + 2, 1, 5, 5)));
		
		// show pendings tasks
		for(Task task : tasks) {
			JPanel taskTitlePanel = new TaskTitlePanel(task, TaskManager.getColorForTask(task), mainContent);
			ThemeController.applyTheme(taskTitlePanel);
			TaskTitleListPanel.add(taskTitlePanel);
		}
		
		// show completed tasks as well
		for(Task task: TaskManager.getCompletedTasks()) {
			if(task.getDeadline().equals(currentMonth.atDay(dayOfMonth))) {
				JPanel taskTitlePanel = new TaskTitlePanel(task, TaskManager.getColorForTask(task), mainContent);
				ThemeController.applyTheme(taskTitlePanel);
				TaskTitleListPanel.add(taskTitlePanel);
			}
		}
		
		ThemeController.applyTheme(TaskTitleListPanel);
		return TaskTitleListPanel;
	}
	
	
}
