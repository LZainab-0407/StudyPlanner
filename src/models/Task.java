package models;

import java.time.LocalDateTime;

public class Task {
	private String title;
	private String description;
	private LocalDateTime deadline;
	private PriorityLevel priorityLevel;
	
	public Task(String title, String description, LocalDateTime deadline, PriorityLevel level) {
		
		this.setTitle(title);
		this.setDescription(description);
		this.setDeadline(deadline);
		this.setPriorityLevel(level);
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

	public LocalDateTime getDeadline() {
		return deadline;
	}

	public void setDeadline(LocalDateTime deadline) {
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
		return title + ": " + description + "\n" + priorityLevel + "\nDeadline: " + deadline;
		
	}
}
