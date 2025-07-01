package models;

/**
 * Defines the priority level of a task.
 * <p>
 * Priority levels are used to visually distinguish tasks based on importance,
 * affect their background color in the UI, and determine their ordering
 * when sorted by priority.
 */
public enum PriorityLevel {
	/** High priority — tasks that are urgent or important. */
	HIGH,
	
	/** Medium priority — tasks of moderate importance. */
	MEDIUM,
	
	/** Low priority — tasks that are least urgent. */
	LOW
}
