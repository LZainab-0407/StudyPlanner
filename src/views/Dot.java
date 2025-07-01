package views;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import models.Task;

public class Dot extends JPanel {
	// the task that the dot represents
	private Task task;
	
	// color of the dot
	private Color color;
	
	// border color - semi-transparent black
	private final Color borderColor = new Color(0, 0, 0, 90);
	
	//
	JPanel mainContent;
	
	 /**
     * Constructs a dot panel associated with a task and its corresponding color.
     *
     * @param task the task associated with this dot (for metadata, hover, etc.)
     * @param color the color to fill the dot with (e.g., based on priority)
     */
	public Dot(Task task, Color color, JPanel mainContent) {
		this.task = task;
		this.color = color;
		
		this.setPreferredSize(new Dimension(14, 14));
		this.setOpaque(false);
		this.setToolTipText(task.getTitle());
		
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new DotTaskPopUpFrame(task, mainContent);
			}
		});
	}
	
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2D = (Graphics2D) g;
		int padding = 2;
		int diameter = getWidth() - (2 * padding);
		
		// fil dot
		g2D.setColor(color);
		g2D.fillOval(padding, padding, diameter, diameter);
		
		// draw border
		g2D.setColor(borderColor);
		g2D.setStroke(new BasicStroke(2));
		g2D.drawOval(padding, padding, diameter, diameter);
		
		g2D.dispose();
	}
	
}
