package views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controllers.CalendarController;
import data.TaskManager;

public class StatsView extends JPanel{
	
	public StatsView(JPanel mainContent) {
		this.setLayout(new BorderLayout(10, 10));
		
		JPanel dataPanel = new JPanel(new GridLayout(4, 1, 2, 2));
		dataPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		JLabel totaltasksLabel = new JLabel ("📋 Total tasks: " + TaskManager.getTasks().size());
		JLabel completedTodayLabel = new JLabel ("✅ Completed today: " + TaskManager.getNumTasksCompletedToday());
		JLabel completedThisWeekLabel = new JLabel ("📆 Completed this week: " + TaskManager.getNumTasksCompletedThisWeek());
		JLabel overdueLabel = new JLabel ("❗ Overdue tasks: " + TaskManager.getOverdueTasks().size());
		
		for(JLabel label: new JLabel[] {totaltasksLabel, completedTodayLabel, completedThisWeekLabel, overdueLabel}) {
			label.setFont(new Font("SansSerif", Font.BOLD, 14));
			dataPanel.add(label);
		}
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		buttonPanel.setPreferredSize(new Dimension(500, 50));
		
		JButton backButton = new IconOnlyButton("Go back to calendar", new ImageIcon("Resources/icons/back.png"));
		backButton.addActionListener(e -> {
			CalendarController.displayCalendar(mainContent);
		});
		buttonPanel.add(backButton);
		
		this.add(dataPanel, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);
	}

}
