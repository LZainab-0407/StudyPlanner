package views;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainFrame extends JFrame {
	
	public MainFrame() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(1000, 800);
		this.setLayout(new BorderLayout());
		this.setLocationRelativeTo(null);
		
		JPanel leftPanel = new JPanel();
		leftPanel.setPreferredSize(new Dimension(150, 500));
		this.add(leftPanel, BorderLayout.WEST);
		
		JPanel mainContent = new JPanel();
		mainContent.setLayout(new BorderLayout());
		this.add(mainContent, BorderLayout.CENTER);
		
		MyMenuBar myMenuBar = new MyMenuBar(this, mainContent);
		this.setJMenuBar(myMenuBar);
		
		TaskManagerPanel taskManagerPanel = new TaskManagerPanel(this, mainContent);
		this.add(taskManagerPanel, BorderLayout.EAST);
		
		this.setVisible(true);
	}
}
