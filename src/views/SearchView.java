package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controllers.CalendarController;
import controllers.SearchController;
import controllers.TaskController;
import controllers.ThemeController;
import data.TaskManager;
import data.ThemeManager;
import models.PriorityLevel;
import models.Task;

/**
 * The {@code SearchView} class provides a user interface panel for
 * searching and filtering tasks based on keyword, priority, and status.
 * It includes a search bar, a filter dialog, and a task result panel.
 *
 * <p>When the user types a keyword or applies filter options, the matching
 * tasks are displayed in a scrollable list. This view integrates with the
 * {@code TaskManager} and {@code TaskController} to update the task display.</p>
 *
 * <p>This class follows MVC principles and is designed to sit within the
 * main content area of the application.</p>
 *
 * @author Labibah Zainab
 */
public class SearchView extends JPanel{
	/** Top panel containing search field and buttons */
	JPanel searchPanel;
	
	/** Panel to display the search result task list */
	JPanel searchResultPanel;
	
	/** The parent frame, used for centering dialogs */
	private JFrame parent;
	
	/** Text field for entering the keyword to search */
	private JTextField keywordField;
	
	/** Icon button to trigger search */
	private IconOnlyButton searchButton;
	
	/** Icon button to open the filter dialog */
	private IconOnlyButton filterButton;
	
	/** Combo box for selecting task priority filter */
	private JComboBox<PriorityLevel> priorityBox;
	
	/** Combo box for selecting task status filter */
	private JComboBox<String> statusBox;
	
	/**
     * Constructs a new {@code SearchView} and sets up its UI components.
     *
     * @param mainContent the main content panel where the view resides
     * @param parent      the parent frame, used for centering popups
     */
	public SearchView(JPanel mainContent, JFrame parent) {
		this.parent = parent;
		this.setLayout(new BorderLayout(10, 10));
		
		this.searchPanel = generateSearchPanel(mainContent);
		this.searchResultPanel = new JPanel(new BorderLayout());
		ThemeController.applyTheme(searchResultPanel);
		
		this.add(searchPanel, BorderLayout.NORTH);
		this.add(searchResultPanel, BorderLayout.CENTER);
		ThemeController.applyTheme(this);
		
	}
	
	/**
     * Creates and returns the search panel that contains:
     * <ul>
     * 	<li>back button for going back to the main calendar dashboard</li>
     *  <li>keyword text field</li>
     * 	<li>search button</li>
     * 	<li>filter button</li>
     * </ul>
     *
     * @param mainContent the main content area for navigation
     * @return configured search input panel
     */
	private JPanel generateSearchPanel(JPanel mainContent) {
		JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5 ));
		
		searchPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5 ));
		searchPanel.setPreferredSize(new Dimension(800, 70));
		
//		JButton backButton = new IconOnlyButton("Go back to calendar", new ImageIcon("Resources/icons/back_arrow-48.png"));
//		backButton.addActionListener(e -> {
//			CalendarController.displayCalendar(mainContent, ViewContext.CALENDAR_WEEK);
//		});
		
		BackButton backButton = new BackButton(mainContent, new ImageIcon("Resources/icons/back_arrow-48.png"));
		
		// placeholder keyword field
		keywordField = new JTextField(50);
		String placeholder = "Enter keyword...";
		keywordField.setForeground(Color.GRAY);
		keywordField.setFont(new Font("SansSerif", Font.ITALIC, 14));
		keywordField.setText(placeholder);
		
		// handle placeholder appearance
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
				if(keywordField.getText().trim().isEmpty()) {
					keywordField.setForeground(Color.GRAY);
					keywordField.setFont(new Font("SansSerif", Font.ITALIC, 12));
					keywordField.setText(placeholder);
				}
			}
		});
		
		// search button functionality
		searchButton = new IconOnlyButton("Search", new ImageIcon("Resources/icons/search-48-arcade.png"));
		searchButton.addActionListener(e -> {
			if(keywordField.getText().trim().isBlank() ||
					keywordField.getText().equalsIgnoreCase("Enter keyword...")) {
				JOptionPane.showMessageDialog(null, "Please enter a keyword for your search,"
						+ " or enter 'all' for all tasks.");
				return;
			}
			TaskManager.setFilteredTasks(keywordField.getText(), null, 0);
			TaskController.refresh(searchResultPanel, ViewContext.SEARCH, null);
		});
		
		// filter button functionality
		filterButton = new IconOnlyButton("Filter", new ImageIcon("Resources/icons/filter-48-arcade.png"));
		filterButton.addActionListener(e -> {
			openFilterDialog();
		});
		
		searchPanel.add(backButton);
		searchPanel.add(keywordField);
		searchPanel.add(searchButton);
		searchPanel.add(filterButton);
		
		ThemeController.applyTheme(searchPanel);
		
		return searchPanel;
	}
	
	 /**
     * Opens a modal filter dialog where users can:
     * <ul>
     * 	<li>enter a keyword </li>
     *  <li>filter by priority</li>
     * 	<li>filter by task status (overdue, due today, etc.)</li>
     * </ul>
     *
     * After clicking "Apply", tasks are filtered and displayed.
     */
	private void openFilterDialog() {
		JDialog filterDialog = new JDialog(parent, "Filter tasks", true);
		filterDialog.setLayout(new BorderLayout(20,20));
		
		filterDialog.setSize(new Dimension(350, 300));
		filterDialog.setLocationRelativeTo(parent);
		
		JPanel filterPanel = new JPanel(new GridLayout(4,2,10,10));
		
		// add keywordField set to text from search panel kewword field
		JLabel keywordLabel = new JLabel("Keyword: ");
		keywordLabel.setForeground(ThemeManager.getForegroundColor());
		filterPanel.add(keywordLabel);
		JTextField filterKeywordField = new JTextField(10);
		filterKeywordField.setText(keywordField.getText());
		filterPanel.add(filterKeywordField);
		
		// add priorityBox (drop-down)
		JLabel priorityLevelLabel = new JLabel("Priority Level: ");
		priorityLevelLabel.setForeground(ThemeManager.getForegroundColor());
		filterPanel.add(priorityLevelLabel);
		priorityBox = new JComboBox<>(PriorityLevel.values());
		priorityBox.insertItemAt(null, 0);
		priorityBox.setSelectedIndex(0);
		filterPanel.add(priorityBox);
		
		// add statusBox (drop-down)
		JLabel statusLabel = new JLabel("Status: ");
		statusLabel.setForeground(ThemeManager.getForegroundColor());
		filterPanel.add(statusLabel);
		String[] statuses = {"Completed", "Overdue", "Due today", "Due tomorrow","Due in 3 days", "Due this week", 
				"Due next week", "Due in 14 days" , "Due in 30 days"};
		statusBox = new JComboBox<>(statuses);
		statusBox.insertItemAt(null, 0);
		statusBox.setSelectedIndex(0);
		filterPanel.add(statusBox);
		
		// apply button for applying filters
		JButton applyButton = new JButton("Apply");
		applyButton.setFocusable(false);
		applyButton.addActionListener(e -> {
			if(keywordField.getText().trim().isBlank() ||
					filterKeywordField.getText().equalsIgnoreCase("Enter keyword...")) {
				JOptionPane.showMessageDialog(null, "Please enter a keyword for your search,"
						+ " or enter 'all' for all tasks.");
				return;
			}
			TaskManager.setFilteredTasks(filterKeywordField.getText(), 
					(PriorityLevel) priorityBox.getSelectedItem(), statusBox.getSelectedIndex());
			TaskController.refresh(searchResultPanel, ViewContext.SEARCH, null);
		});
		filterPanel.add(applyButton);
		
		// cancel button (filter dialog disappears going back to original search view)
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setFocusable(false);
		cancelButton.addActionListener(e -> {
			filterDialog.dispose();
		});
		filterPanel.add(cancelButton);
		
		ThemeController.applyTheme(filterPanel);
		
		filterDialog.add(filterPanel, BorderLayout.CENTER);
		filterDialog.pack();
		filterDialog.setVisible(true);
	}
}
