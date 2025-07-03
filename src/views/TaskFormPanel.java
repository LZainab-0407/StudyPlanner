package views;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import com.toedter.calendar.JDateChooser;

import models.PriorityLevel;

public class TaskFormPanel extends JPanel {
	
	private JTextField titleField;
	
	private JTextArea descriptionArea;
	
	private JComboBox<PriorityLevel> priorityLevelBox;
	
	private JDateChooser deadlinePicker;
	
	public TaskFormPanel(){
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		// first row, first column
		JLabel titleLabel = new JLabel("Title: ");
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(5,5,5,5);
		this.add(titleLabel, gbc);
		
		// first row, second column
		titleField = new JTextField();
		gbc.gridx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		this.add(titleField, gbc);
		
		// second row, first column
		JLabel descriptionLabel =  new JLabel("Description: ");
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		this.add(descriptionLabel, gbc);
		
		// second row, second column
		descriptionArea = new JTextArea(4, 20);
		descriptionArea.setLineWrap(true);
		descriptionArea.setWrapStyleWord(true);
		JScrollPane scrollPane = new JScrollPane(descriptionArea);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		gbc.gridx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		this.add(scrollPane, gbc);
		
		// third row, first column
		JLabel priorityLevelLabel = new JLabel("Priority Level: ");
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.fill = GridBagConstraints.NONE;
		this.add(priorityLevelLabel, gbc);
		
		// third row, second column
		priorityLevelBox = new JComboBox<>(PriorityLevel.values());
		gbc.gridx = 1;
		gbc.fill = GridBagConstraints.NONE;
		this.add(priorityLevelBox, gbc);
		
		// fourth row, first column
		JLabel deadlineLabel =  new JLabel("Deadline: ");
		gbc.gridx = 0;
		gbc.gridy = 3;
		this.add(deadlineLabel, gbc);
		
		// fourth row, second column
		deadlinePicker = new JDateChooser();
		deadlinePicker.setDateFormatString("dd MMMMM yyyy");
		deadlinePicker.setMinSelectableDate(new Date()); // can't pick dates that have already passed
		gbc.gridx = 1;
		this.add(deadlinePicker, gbc);
	}
	
	/**
	 * returns text from the title field
	 * @return
	 */
	public String getTitleText() {
		return titleField.getText();
	}
	/**
	 * sets initial text in the title field
	 * @param title
	 */
	public void setTitleFieldText(String title) {
		titleField.setText(title);
	}
	
	/**
	 * gets text from description area
	 * @return
	 */
	public String getDescriptionText() {
		return descriptionArea.getText();
	}
	
	/**
	 * sets initial text in description area
	 * @param descriptionArea
	 */
	public void setDescriptionText(String description) {
		descriptionArea.setText(description);;
	}
	
	/**
	 * returns priority level selected for the task
	 * @return
	 */
	public PriorityLevel getPriorityLevelFromBox() {
		return (PriorityLevel) priorityLevelBox.getSelectedItem();
	}
	
	/**
	 * Sets the initial priority level option for the box
	 * @param priorityLevelBox
	 */
	public void setPriorityLevelBoxOption(PriorityLevel priorityLevel) {
		priorityLevelBox.setSelectedItem(priorityLevel);;
	}
	
	/**
	 * returns deadline selected from deadline picker
	 * @return
	 */
	public LocalDate getDeadlineFromPicker() {
		Date selectedDate = deadlinePicker.getDate();
		if (selectedDate == null) {
			return null;
		}
		LocalDate date = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		return date;
	}
	
	/**
	 * Sets initial date on deadline picker 
	 * @param deadline  (LocalDate) taskDeadline
	 */
	public void setDeadlinePickerdate(LocalDate deadline) {
		if(deadline != null) {
			Date date = Date.from(deadline.atStartOfDay(ZoneId.systemDefault()).toInstant());
	        deadlinePicker.setDate(date);
		}
	}
	
	/**
	 * Sets initial date on deadline picker 
	 * @param deadline  (Date) initial date
	 */
	public void  setDeadlinePickerdate(Date date) {
		if (date != null) {
			deadlinePicker.setDate(date);
		}
	}
}
