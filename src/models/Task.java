package models;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Task{

	private String title;
	private String description;
	private LocalDate deadline;
	private PriorityLevel priorityLevel;
	private boolean isCompleted;
	private LocalDate completionDate;
	
	public Task(String title, String description, LocalDate deadline, PriorityLevel level) {
		this.setCompleted(false);
		this.setTitle(title);
		this.setDescription(description);
		this.setDeadline(deadline);
		this.setPriorityLevel(level);
	}
	
	/**
	 * Returns a string indicating the task's status:
	 * - "Completed!" if task is already completed
	 * - "Overdue!" if the deadline has passed
	 * - "Due today" if the deadline is today
	 * - "Due tomorrow" if deadline is the next day
	 * - "Due in X days" otherwise
	 *
	 * @return human-readable status of the task
	 */
	public String getStatus() {
		if (isCompleted) {
			return "Completed!";
		}
		LocalDate today = LocalDate.now();
		if(deadline.isBefore(today)) {
			return "Overdue!";
		}
		else if(deadline.isEqual(today)) {
			return "Due today";
		}
		long days = ChronoUnit.DAYS.between(today, deadline);
		if (days == 1) {
			return "Due tomorrow";
		}
		return "Due in " + days + " days";
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
	public LocalDate getDeadline() {
		return deadline;
	}

	public void setDeadline(LocalDate deadline) {
		this.deadline = deadline;
	}

	public PriorityLevel getPriorityLevel() {
		return priorityLevel;
	}

	public void setPriorityLevel(PriorityLevel priorityLevel) {
		this.priorityLevel = priorityLevel;
	}
	
	@Override
	public String toString() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM");
		return "Title: "+title + "\n " + 
				"Description: "+ description + "\n " + 
				"Priority Level: " + priorityLevel + 
				"\n Due on: " + deadline.format(formatter) +
				" at 11:59 pm ";
		
	}

	public boolean isCompleted() {
		return isCompleted;
	}

	public void setCompleted(boolean isCompleted) {
		this.isCompleted = isCompleted;
		if(isCompleted) {
			this.completionDate = LocalDate.now(); // mark when completed
		}
		else {
			this.completionDate = null;
		}
	}
	
	public LocalDate getCompletionDate() {
		return completionDate;
	}
}
