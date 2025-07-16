package controllers;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import data.TaskManager;
import models.UserSession;
import views.SearchView;
import views.TaskListPanel;
import views.ViewContext;

/**
 * Controller class responsible for handling interactions related to task search and filtering.
 * <p>
 * Provides methods to display the search view and refresh the search results panel
 * with the most recent filtered task list stored in {@code TaskManager}.
 */
public class SearchController {
	
	/**
     * Displays the search interface in the provided main content panel.
     * <p>
     * This method removes all existing components from {@code mainContent} and
     * adds a new {@code SearchView}, which includes a keyword field and filter options.
     *
     * @param mainContent the panel in the main frame where the search view should be shown
     * @param parent      the parent frame (typically {@code MainFrame}) to support modal dialogs
     */
	public static void displaySearchView(JPanel mainContent, JFrame parent) {
		UserSession.setCurrentViewContext(ViewContext.SEARCH);
		mainContent.removeAll();
		SearchView searchView = new SearchView(mainContent, parent);
		mainContent.add(searchView, BorderLayout.CENTER);
		mainContent.revalidate();
	    mainContent.repaint();
	}
	
	/**
     * Refreshes the panel that displays the results of the most recent task search or filter operation.
     * <p>
     * This method retrieves the most recently filtered tasks from {@code TaskManager} and displays them
     * inside the provided {@code searchResultsPanel}.
     *
     * @param searchResultsPanel the panel where the latest filtered task list should be displayed
     */
	public static void refreshSearchResult(JPanel searchResultsPanel) {
		UserSession.setCurrentViewContext(ViewContext.SEARCH);
		searchResultsPanel.removeAll();
		TaskListPanel taskListPanel = new TaskListPanel(searchResultsPanel, TaskManager.getLatestFilteredTasks(), ViewContext.SEARCH);
		JScrollPane taskListPane = new JScrollPane(taskListPanel);
		searchResultsPanel.add(taskListPane, BorderLayout.CENTER);
		ThemeController.applyTheme(searchResultsPanel);
		searchResultsPanel.revalidate();
		searchResultsPanel.repaint();
	}
}
