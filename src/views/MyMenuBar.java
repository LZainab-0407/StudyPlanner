package views;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import controllers.TaskController;

public class MyMenuBar extends JMenuBar{
	JFrame parent;
	JPanel mainContent;
	
	public MyMenuBar(JFrame parent, JPanel mainContent) {
		this.parent = parent;
		this.mainContent = mainContent;
		
		JMenuItem addTaskItem = new JMenuItem("Add New Task");
		addTaskItem.addActionListener(e -> TaskController.addNewTask(parent, mainContent));
		
		JMenuItem viewTaskListItem = new JMenuItem("View Task List");
		viewTaskListItem.addActionListener(e -> {
			TaskController.displayTaskList(mainContent);
		});
		
		JMenu tasksMenu = new JMenu("Task");
		
		tasksMenu.add(addTaskItem);
		tasksMenu.add(viewTaskListItem);
		
		this.add(tasksMenu);
	}
	
	
}
