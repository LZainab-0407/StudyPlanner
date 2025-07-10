package views;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import controllers.CalendarController;

public class BackButton extends IconOnlyButton{
	
	JPanel mainContent;
	
	public BackButton(JPanel mainContent) {
		super("Go back to calendar", new ImageIcon("Resources/icons/back_arrow-64.png"));
		this.mainContent = mainContent;
		JPopupMenu options = generatePopup();
		this.addActionListener(e -> {
			options.show(this, this.getWidth()/2, this.getHeight()/2);
		});
	}

	public BackButton(JPanel mainContent, ImageIcon icon) {
		super("Go back to calendar", icon);
		this.mainContent = mainContent;
		JPopupMenu options = generatePopup();
		this.addActionListener(e -> {
			options.show(this, this.getWidth()/2, this.getHeight()/2);
		});
	}
	
	private JPopupMenu generatePopup() {
		JPopupMenu options = new JPopupMenu();
		
		JMenuItem weekItem = new JMenuItem("Go back to calendar week");
		weekItem.addActionListener(e -> {
			CalendarController.displayCalendar(mainContent, ViewContext.CALENDAR_WEEK);
		});
		
		JMenuItem monthItem = new JMenuItem("Go back to calendar month");
		monthItem.addActionListener(e -> {
			CalendarController.displayCalendar(mainContent, ViewContext.CALENDAR_MONTH);
		});
		
		options.add(weekItem);
		options.add(monthItem);
		
		return options;
	}

}
