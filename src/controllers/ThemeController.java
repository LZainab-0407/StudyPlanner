package controllers;

import java.awt.Component;

import data.ThemeManager;

public class ThemeController {
	
	/**
	 * Applies current theme to a component
	 * @param comp component to which to apply the theme
	 */
	public static void applyTheme(Component comp) {
		comp.setBackground(ThemeManager.getBackgroundColor());
		comp.setForeground(ThemeManager.getForegroundColor());
	}
}
