package data;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import views.Theme;

public class ThemeManager {
	private static Theme currentTheme;
	private static final String CONFIG_PATH = "config/theme.properties";
	
	/**
	 * Returns background color depending on the theme: 
	 * <ul>
	 * 	<li>LIGHT: returns white</li>
	 * 	<li>DARK: returns dark charcoal</li>
	 * </ul>
	 * @return background color
	 */
	public static Color getBackgroundColor() {
		return (currentTheme == Theme.DARK) ? new Color (0x2e2e2e) : Color.WHITE;
	}
	
	/**
	 * Returns foreground color depending on the theme: 
	 * <ul>
	 * 	<li>LIGHT: returns black</li>
	 * 	<li>DARK: returns white</li>
	 * </ul>
	 * @return foreground color
	 */
	public static Color getForegroundColor() {
		return (currentTheme.equals(Theme.DARK)) ? Color.WHITE : Color.BLACK;
	}
	
	/**
	 * Returns accent color depending on the theme: 
	 * <ul>
	 * 	<li>LIGHT: returns lavender (0xd6befa)</li>
	 * 	<li>DARK: returns purple (0xB696E7)</li>
	 * </ul>
	 * @return accent color
	 */
	public static Color getAccentColor() {
		return (currentTheme == Theme.DARK) ? new Color(0xb699e7) : new Color(0xd6befa);
	}
	
	/**
	 * Returns calendar day panel color depending on the theme: 
	 * <ul>
	 * 	<li>LIGHT: returns blue (0xdff2ff)</li>
	 * 	<li>DARK: returns gray</li>
	 * </ul>
	 * @return accent color
	 */
	public static Color getCalendarDayPanelColor() {
		return (currentTheme == Theme.DARK) ? Color.GRAY : new Color(0xdff2ff);
	}
	
	/**
	 * Returns the current theme
	 * @return
	 */
	public static Theme getCurrentTheme() {
		return currentTheme;
	}
	
	/**
	 * Toggles theme from light to dark or dark to light
	 */
	public static void toggleTheme() {
		currentTheme = (currentTheme == Theme.LIGHT) ? Theme.DARK : Theme.LIGHT;
		saveThemePreference();
	}
	
	/**
	 * Saves theme prefernce to a config file
	 */
	public static void saveThemePreference() {
		try {
			new File("config").mkdirs(); // ensure file exits
			Properties props = new Properties();
			props.setProperty("theme", currentTheme.name());
			try(FileOutputStream fos = new FileOutputStream(CONFIG_PATH)){
				props.store(fos, "Theme settings");
			}
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println("Failed to save theme preference.");
		}
	}
	
	/**
	 * Loads theme preference from config/theme.properties file and sets current theme 
	 * to the preference. 
	 * If this file does not exist yet, current theme is set to LIGHT.
	 */
	public static void loadThemePreference() {
		File file = new File(CONFIG_PATH);
		if(file.exists()) {
			try (FileInputStream fis = new FileInputStream(file)){
				Properties props = new Properties();
				props.load(fis);
				String themeName = props.getProperty("theme");
				// if themename is not null and theme name is dark, then set current theme to dark, or to light otherwise
				//currentTheme = (themeName != null && themeName.equalsIgnoreCase(Theme.DARK.name())) ? Theme.DARK : Theme.LIGHT;
				if(themeName != null) {
					if ( themeName.equalsIgnoreCase(Theme.DARK.name())) {
						currentTheme = Theme.DARK;
					} else {
						currentTheme = Theme.LIGHT;
					}
					//System.out.println(currentTheme.name());
				}
				
			} catch (Exception e) {
				// e.printStackTrace();
				System.out.println("Failed to load theme preference.");
			}
		}
		else {
			currentTheme = Theme.LIGHT;
		}
	}
}
