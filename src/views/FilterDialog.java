package views;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;

import models.PriorityLevel;

public class FilterDialog extends JDialog{
	private JComboBox<PriorityLevel> priorityBox;
	private JComboBox<String> statusBox;
	
	public FilterDialog(JFrame parent) {
		super(parent, "Filter tasks", true);
		
		
	}
	
	public PriorityLevel getPriorityLevel() {
		return (PriorityLevel) priorityBox.getSelectedItem();
	}
	
	public String getStatus() {
		return (String) statusBox.getSelectedItem();
	}
}
