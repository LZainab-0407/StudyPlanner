package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;

public class CalendarDayPanel extends JPanel {
	
	CalendarDayPanel(int day){
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createLineBorder(new Color(0x75bfec)));
		this.setBackground(new Color(0xdff2ff));
		
//		// day number label, aligned top right
//		JLabel dayLabel = new JLabel(String.valueOf(day) + " ");
//		dayLabel.setVerticalAlignment(SwingConstants.TOP);
//		dayLabel.setHorizontalAlignment(SwingConstants.CENTER);
//		dayLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
//		this.add(dayLabel, BorderLayout.NORTH);
//		
//		JPopupMenu optionsMenu = generatePopupMenu();
//		// add options button
//		JButton optionsButton = new IconOnlyButton("Options", new ImageIcon("Resources/icons/menu.png"));
//		//optionsButton.setHorizontalAlignment(SwingConstants.RIGHT);
//		optionsButton.addActionListener(e -> {
//			optionsMenu.show(optionsButton, optionsButton.getWidth(), optionsButton.getHeight());
//		});
		JPanel topPanel = generateTopPanel(day);
		this.add(topPanel, BorderLayout.NORTH);
		
	}
	private JPanel generateTopPanel(int day) {
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.setBorder(BorderFactory.createLineBorder(new Color(0x75bfec)));
		JLabel dayLabel = new JLabel("  " + String.valueOf(day) );
		dayLabel.setVerticalAlignment(SwingConstants.TOP);
		dayLabel.setHorizontalAlignment(SwingConstants.CENTER);
		dayLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
		dayLabel.setForeground(new Color(0x538eec));
		topPanel.add(dayLabel, BorderLayout.WEST);
		
		JPopupMenu optionsMenu = generatePopupMenu();
		// add options button
		JButton optionsButton = new IconOnlyButton("Options", new ImageIcon("Resources/icons/menu.png"));
		//optionsButton.setHorizontalAlignment(SwingConstants.RIGHT);
		optionsButton.addActionListener(e -> {
			optionsMenu.show(optionsButton, optionsButton.getWidth(), optionsButton.getHeight());
		});
		topPanel.add(optionsButton, BorderLayout.EAST);
		
		return topPanel;
	}
	
	private JPopupMenu generatePopupMenu() {
		JPopupMenu dayMenu = new JPopupMenu();
		
		JMenuItem showTasksItem = new JMenuItem("Show pending tasks");
		JMenuItem clearTaskItem = new JMenuItem("Clear all tasks");
		
		dayMenu.add(showTasksItem);
		dayMenu.add(clearTaskItem);
		
		return dayMenu;
	}

	
}
