package controllers;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import models.UserSession;
import views.StatsView;
import views.ViewContext;

public class StatsController {
	
	public static void displayStatsView(JPanel mainContent) {
		UserSession.setCurrentViewContext(ViewContext.STATS);
		mainContent.removeAll();
		StatsView statsView = new StatsView(mainContent);
		mainContent.add(statsView, BorderLayout.CENTER);
		mainContent.revalidate();
	    mainContent.repaint();
	}
}
