package views;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import controllers.CalendarController;
import controllers.TaskController;

public class MyMenuBar extends JMenuBar{
	JFrame parent;
	JPanel mainContent;
	
	public MyMenuBar(JFrame parent, JPanel mainContent) {
		this.parent = parent;
		this.mainContent = mainContent;
		
		// task menu
		JMenuItem addTaskItem = new JMenuItem("Add New Task");
		addTaskItem.addActionListener(e -> TaskController.addNewTask(parent, mainContent));
		JMenuItem viewTaskListItem = new JMenuItem("View Task List");
		viewTaskListItem.addActionListener(e -> {
			TaskController.displayTaskList(mainContent, null, ViewContext.TASK_LIST_PENDING);
		});
		JMenu tasksMenu = new JMenu("Task");
		tasksMenu.add(addTaskItem);
		tasksMenu.add(viewTaskListItem);
		
		// view menu
		JMenuItem calendarItem = new JMenuItem("Calendar");
		calendarItem.addActionListener(e -> {
			CalendarController.displayCalendar(mainContent);
		});
		JMenuItem taskListItem = new JMenuItem("Task List");
		taskListItem.addActionListener(e -> {
			TaskController.displayTaskList(mainContent, null, ViewContext.TASK_LIST_PENDING);
		});
		JMenu viewMenu = new JMenu("View");
		viewMenu.add(calendarItem);
		viewMenu.add(taskListItem);
		
		this.add(tasksMenu);
		this.add(viewMenu);
	}
	
	
}
