package views;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import controllers.TaskController;

public class TaskManagerPanel extends JPanel{
	JButton addNewTaskButton;
	JFrame parentFrame;
	
	public TaskManagerPanel(JFrame parentFrame, JPanel mainContent){
		this.parentFrame = parentFrame;
		this.setPreferredSize(new Dimension(150, 500));
		
		this.setLayout(new GridLayout(10, 1));
		
		addNewTaskButton = new JButton("Add New Task");
		addNewTaskButton.setFocusable(false);
		
		addNewTaskButton.addActionListener(e -> TaskController.addNewTask(parentFrame, mainContent));
		
		this.add(addNewTaskButton);
	}
}
