package controllers;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import views.SearchView;

public class SearchController {
	
	public static void displaySearchView(JPanel mainContent, JFrame parent) {
		mainContent.removeAll();
		SearchView searchView = new SearchView(mainContent, parent);
		mainContent.add(searchView, BorderLayout.CENTER);
		mainContent.revalidate();
	    mainContent.repaint();
	}
}
