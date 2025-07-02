package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controllers.CalendarController;
import models.PriorityLevel;

public class SearchPanel extends JPanel{
	private JFrame parent;
	private JTextField keywordField;
	private IconOnlyButton searchButton;
	private IconOnlyButton filterButton;
	private JComboBox<PriorityLevel> priorityBox;
	private JComboBox<String> statusBox;
	
	public SearchPanel(JPanel mainContent, JFrame parent) {
		this.parent = parent;
		this.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5 ));
		this.setPreferredSize(new Dimension(800, 100));
		
		JButton backButton = new IconOnlyButton("Go back to calendar", new ImageIcon("Resources/icons/back.png"));
		backButton.addActionListener(e -> {
			CalendarController.displayCalendar(mainContent);
		});
		
		keywordField = new JTextField(50);
		String placeholder = "Enter keyword...";
		keywordField.setForeground(Color.GRAY);
		keywordField.setFont(new Font("SansSerif", Font.ITALIC, 12));
		keywordField.setText(placeholder);
		// remove placeholder on focus
		keywordField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if(keywordField.getText().equals(placeholder)) {
					keywordField.setFont(new Font("SansSerif", Font.PLAIN, 12));
					keywordField.setText("");
					keywordField.setForeground(Color.black);
				}
			}
			@Override
			public void focusLost(FocusEvent e) {
				if(keywordField.getText().isEmpty()) {
					keywordField.setForeground(Color.GRAY);
					keywordField.setFont(new Font("SansSerif", Font.ITALIC, 12));
					keywordField.setText(placeholder);
				}
			}
		});
		
		searchButton = new IconOnlyButton("Search", new ImageIcon("Resources/icons/search-30.png"));
		
		filterButton = new IconOnlyButton("Filter", new ImageIcon("Resources/icons/filter-30.png"));
		filterButton.addActionListener(e -> {
			openFilterDialog();
		});
		
		this.add(backButton);
		this.add(keywordField);
		this.add(searchButton);
		this.add(filterButton);
	}
	
	private void openFilterDialog() {
		JDialog filterDialog = new JDialog(parent, "Filter tasks", true);
		filterDialog.setLayout(new BorderLayout(20,20));
		
		filterDialog.setSize(new Dimension(350, 300));
		filterDialog.setLocationRelativeTo(parent);
		
		JPanel filterPanel = new JPanel(new GridLayout(3,2,10,10));
		
		// add priorityBox
		JLabel priorityLevelLabel = new JLabel("Priority Level: ");
		filterPanel.add(priorityLevelLabel);
		priorityBox = new JComboBox<>(PriorityLevel.values());
		priorityBox.insertItemAt(null, 0);
		priorityBox.setSelectedIndex(0);
		filterPanel.add(priorityBox);
		
		// add statusBox
		JLabel statusLabel = new JLabel("Status: ");
		filterPanel.add(statusLabel);
		String[] statuses = {"Completed", "Overdue", "Due today", "Due in 7 days", "Due in 30 days"};
		statusBox = new JComboBox<>(statuses);
		statusBox.insertItemAt(null, 0);
		statusBox.setSelectedIndex(0);
		filterPanel.add(statusBox);
		
		JButton applyButton = new JButton("  Apply");
		applyButton.setFocusable(false);
		applyButton.addActionListener(e -> {
			
		});
		filterPanel.add(applyButton);
		
		JButton cancelButton = new JButton("  Cancel");
		cancelButton.setFocusable(false);
		cancelButton.addActionListener(e -> {
			filterDialog.dispose();
		});
		filterPanel.add(cancelButton);
		
		filterDialog.add(filterPanel, BorderLayout.CENTER);
		filterDialog.pack();
		filterDialog.setVisible(true);
	}
}
