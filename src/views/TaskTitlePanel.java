package views;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import data.TaskManager;
import models.Task;

/**
 * Represents a clickable panel for a task, used in the week view.
 * Shows the task title and description, and opens a popup on click.
 * 
 * @author Labibah Zainab
 */
public class TaskTitlePanel extends JPanel{
	private Task task;
	JTextArea taskArea;
	JPanel mainContent;
	
	/**
     * Constructs a visual representation of a task's title and description.
     *
     * @param task the task to display
     * @param color the color based on priority
     * @param mainContent the main content panel for opening task popup
     */
	public TaskTitlePanel(Task task, Color color,JPanel mainContent) {
		this.task = task;
		this.mainContent = mainContent;
		
		this.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		String info = task.getTitle() + "\n" + task.getDescription();
		this.taskArea = new JTextArea(info);
		
		taskArea.setLineWrap(true);
		taskArea.setWrapStyleWord(true);
		taskArea.setFont(new Font("Arial", Font.PLAIN, 10));
		taskArea.setEditable(false);
		taskArea.setOpaque(true);
		taskArea.setBackground(TaskManager.getColorForTask(task));
		
		taskArea.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new TaskPopUpFrame(task, mainContent, ViewContext.CALENDAR_WEEK);
			}
		});
		
		JScrollPane taskTitlePane = new JScrollPane(taskArea);

		taskTitlePane.setFocusable(false);
		
		this.add(taskTitlePane);
	}
}
