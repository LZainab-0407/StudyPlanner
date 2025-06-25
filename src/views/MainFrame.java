package views;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainFrame extends JFrame {
	
	public MainFrame() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(500, 500);
		this.setLayout(new BorderLayout());
		this.setLocationRelativeTo(null);
		
		JPanel mainContent = new JPanel();
		mainContent.setLayout(new BorderLayout());
		this.add(mainContent, BorderLayout.CENTER);
		
		MyMenuBar myMenuBar = new MyMenuBar(this, mainContent);
		this.setJMenuBar(myMenuBar);
		
		TaskManagerPanel taskManagerPanel = new TaskManagerPanel(this);
		this.add(taskManagerPanel, BorderLayout.EAST);
		
		this.setVisible(true);
	}
}
