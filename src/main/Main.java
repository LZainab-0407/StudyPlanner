package main;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import views.TaskManagerPanel;

public class Main {

	public static void main(String[] args) {
		JFrame frame = new JFrame("Planner");
		frame.setSize(500, 500);
		frame.setLayout(new BorderLayout());
		frame.setLocationRelativeTo(null);
		
		TaskManagerPanel taskManagerPanel = new TaskManagerPanel(frame);
		frame.add(taskManagerPanel, BorderLayout.EAST);
		frame.setVisible(true);

	}

}
