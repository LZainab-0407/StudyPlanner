package main;

import javax.swing.JFrame;

import views.TaskManagerPanel;

public class Main {

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setSize(600, 600);
		frame.setLocationRelativeTo(null);
		
		TaskManagerPanel taskManagerPanel = new TaskManagerPanel();
		frame.add(taskManagerPanel);
		frame.setVisible(true);
	}

}
