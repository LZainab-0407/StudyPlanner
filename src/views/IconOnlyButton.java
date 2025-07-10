package views;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.BevelBorder;

/**
 * A minimalist icon-only JButton for use in modern UI panels.
 * <p>
 * This button hides its background, border, and focus ring, making it
 * ideal for toolbar icons like delete, edit, or navigation.
 * <p>
 * Use this button when you want a clean, compact button that only shows an icon
 * and an optional tooltip for accessibility.
 */
public class IconOnlyButton extends JButton implements MouseListener{

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
		this.setCursor(new Cursor(Cursor.HAND_CURSOR));
		this.setToolTipText(toolTipText);
		this.setFocusable(false);
		this.addMouseListener(this);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// highlight when cursor on button
		if (e.getSource() == this) {
			// this.setBackground(Color.LIGHT_GRAY);
			this.setBackground(new Color(0xd6befa)); // lavender
			this.setContentAreaFilled(true);
			this.setOpaque(true);
//			this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
//			this.setBorderPainted(true);
			this.repaint();
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// remove highlight when cursor exits button
		if (e.getSource() == this) {
			this.setBackground(null);
			this.setContentAreaFilled(false);
			this.setOpaque(false);
//			this.setBorderPainted(false);
			this.repaint();
		}
		
	}
}
