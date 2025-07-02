package controllers;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import data.TaskManager;
import models.Task;
import views.SearchView;
import views.TaskListPanel;
import views.ViewContext;

public class SearchController {
	
	public static void displaySearchView(JPanel mainContent, JFrame parent) {
		mainContent.removeAll();
		SearchView searchView = new SearchView(mainContent, parent);
		mainContent.add(searchView, BorderLayout.CENTER);
		mainContent.revalidate();
	    mainContent.repaint();
	}
	
	public static void refreshSearchResult(JPanel searchResultsPanel) {
		searchResultsPanel.removeAll();
		TaskListPanel taskListPanel = new TaskListPanel(searchResultsPanel, TaskManager.getLatestFilteredTasks(), ViewContext.SEARCH);
		JScrollPane taskListPane = new JScrollPane(taskListPanel);
		searchResultsPanel.add(taskListPane, BorderLayout.CENTER);
		searchResultsPanel.revalidate();
		searchResultsPanel.repaint();
	}
}
