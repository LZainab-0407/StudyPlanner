package controllers;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.toedter.calendar.JDateChooser;

import data.TaskManager;
import models.PriorityLevel;
import models.Task;

public class TaskController{

	public static void openAddNewTaskWindow(JFrame parentFrame) {
		
		JFrame inputFrame = new JFrame("Add New Task");
		inputFrame.setSize(500, 500);
		inputFrame.setLocationRelativeTo(parentFrame);
		inputFrame.setLayout(new BorderLayout());
		
		//creating components
		JTextField titleField = new JTextField();
		JTextArea descriptionArea = new JTextArea();
		JComboBox<PriorityLevel> priorityLevelBox = new JComboBox<>(PriorityLevel.values());
//		JTextField deadlineField = new JTextField("yyyy-MM-dd HH:mm");
		JDateChooser deadlinePicker = new JDateChooser();
		deadlinePicker.setDateFormatString("dd MMMMM yyyy");
		
		// panel for input
		JPanel inputPanel = new JPanel(new GridLayout(4, 2, 20, 20)); 
		inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		JLabel titleLabel = new JLabel("Title: ");
		inputPanel.add(titleLabel);
		inputPanel.add(titleField);
		
		JLabel descriptionLabel = new JLabel("Description: ");
		inputPanel.add(descriptionLabel);
		inputPanel.add(descriptionArea);
		
		JLabel priorityLevelLabel = new JLabel("Priority Level: ");
		inputPanel.add(priorityLevelLabel);
		inputPanel.add(priorityLevelBox);
		
		JLabel deadlineLabel = new JLabel("Deadline: ");
		inputPanel.add(deadlineLabel);
		inputPanel.add(deadlinePicker);
		
		inputFrame.add(inputPanel, BorderLayout.CENTER);
		inputFrame.setVisible(true);
		
		// create buttons to save or cancel in a new panel
		JPanel buttonPanel = new JPanel(new FlowLayout());
		JButton saveButton = new JButton("Save Task");
		saveButton.setFocusable(false);
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setFocusable(false);
		
		buttonPanel.add(saveButton);
		buttonPanel.add(cancelButton);
		
		inputFrame.add(buttonPanel, BorderLayout.SOUTH);
		
		// save button logic
		saveButton.addActionListener(e -> {
			String taskTitle = titleField.getText();
			String taskDescription = descriptionArea.getText();
			PriorityLevel taskPriorityLevel = (PriorityLevel) priorityLevelBox.getSelectedItem();
			
			Date selectedDate = deadlinePicker.getDate();
			if (selectedDate == null) {
				JOptionPane.showMessageDialog(inputFrame, "Please select a deadline date.");
				return; // returns from actionPerformed method and newTask is not created
			}
			// convert to LocalDate type referring to local zone
			LocalDate deadlineDate = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			// final deadline will be that day at 2359 hrs
			LocalDateTime taskDeadline = LocalDateTime.of(deadlineDate, LocalTime.of(23, 59));
			
			Task newTask = new Task(taskTitle, taskDescription, taskDeadline, taskPriorityLevel);
			TaskManager.addTask(newTask);
			
			JOptionPane.showMessageDialog(inputFrame, "New task added!");
			inputFrame.dispose();
		});
		
		// cancel button logic: close window
		cancelButton.addActionListener(e -> {inputFrame.dispose();});
		
	}
}
