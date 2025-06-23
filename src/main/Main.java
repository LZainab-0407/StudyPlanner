package main;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import views.MyMenuBar;
import views.TaskManagerPanel;

public class Main {

	public static void main(String[] args) {
		JFrame frame = new JFrame("Planner");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 500);
		frame.setLayout(new BorderLayout());
		frame.setLocationRelativeTo(null);
		
		JPanel mainContent = new JPanel();
		mainContent.setLayout(new BorderLayout());
		frame.add(mainContent, BorderLayout.CENTER);
		
		MyMenuBar myMenuBar = new MyMenuBar(frame, mainContent);
		frame.setJMenuBar(myMenuBar);
		
		TaskManagerPanel taskManagerPanel = new TaskManagerPanel(frame);
		frame.add(taskManagerPanel, BorderLayout.EAST);
		
		frame.setVisible(true);
	}

}
