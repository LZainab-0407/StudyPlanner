package controllers;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import models.UserSession;
import views.StatsView;
import views.ViewContext;

/**
 * The {@code StatsController} class handles logic related to displaying and updating
 * the statistics view dashboard.
 * <p>
 * This controller is responsible for switching to the stats view and updating its
 * visual data panels with pie charts, bar charts, or line charts.
 *
 * <p>MVC Role: Controller
 * Triggers updates to the {@code StatsView} UI based on user interaction
 *
 * @author Labibah Zainab
 */
public class StatsController {
	
	 /**
     * Displays the statistics dashboard view on the main content panel.
     * <p>
     * This method:
     * <ul>
     *   <li>Sets the current view context to {@code ViewContext.STATS}</li>
     *   <li>Removes existing content from the main panel</li>
     *   <li>Adds a new {@code StatsView} instance</li>
     *   <li>Triggers revalidation and repainting</li>
     * </ul>
     *
     * @param mainContent the main panel where the stats view should be displayed
     */
	public static void displayStatsView(JPanel mainContent) {
		UserSession.setCurrentViewContext(ViewContext.STATS);
		mainContent.removeAll();
		StatsView statsView = new StatsView(mainContent);
		mainContent.add(statsView, BorderLayout.CENTER);
		mainContent.revalidate();
	    mainContent.repaint();
	}
	
	 /**
     * Replaces the current content in a visual panel with a new chart.
     * <p>
     * Typically used when switching between pie chart, bar chart, or line chart visualizations
     * in the stats view dashboard.
     *
     * @param visualPanel the panel that holds visual/statistical data
     * @param visualDataPanel the new panel to display (e.g., pie chart, bar chart)
     */
	public static void refreshVisualPanel(JPanel visualPanel, JPanel visualDataPanel) {
		visualPanel.removeAll();
		visualPanel.add(visualDataPanel, BorderLayout.CENTER);
		visualPanel.revalidate();
		visualPanel.repaint();
	}
	
}
