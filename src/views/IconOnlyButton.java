package views;

import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * A minimalist icon-only JButton for use in modern UI panels.
 * <p>
 * This button hides its background, border, and focus ring, making it
 * ideal for toolbar icons like delete, edit, or navigation.
 * <p>
 * Use this button when you want a clean, compact button that only shows an icon
 * and an optional tooltip for accessibility.
 */
public class IconOnlyButton extends JButton{
	
	/**
     * Creates a transparent, icon-only button with a tooltip.
     *
     * @param toolTipText the tooltip text shown on hover (e.g. "Delete Task")
     * @param icon the ImageIcon to display on the button
     */
	public IconOnlyButton(String toolTipText, ImageIcon icon) {
		this.setIcon(icon);
		this.setFocusable(false);
		this.setContentAreaFilled(false);
		this.setBorderPainted(false);
		this.setFocusPainted(false);
		this.setOpaque(false);
		this.setToolTipText(toolTipText);
		this.setPreferredSize(new Dimension(20, 20));
		this.setFocusable(false);
	}
}
