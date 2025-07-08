package views;

/**
 * Represents the context in which a task is being displayed or managed.
 * <p>
 * This enum helps distinguish between different parts of the application UI,
 * such as the main task list or the calendar view, and allows components like
 * {@code TaskPanel} or controllers to adjust their behavior accordingly.
 */
public enum ViewContext {
	/** The task is being displayed in the main task list view. */
	TASK_LIST_ALL,
	
	TASK_LIST_COMPLETED,
	
	TASK_LIST_PENDING,
	
	TASK_LIST_OVERDUE,
	
	/**
	 * Task is being being displayed in a task list view. The list contains tasks
	 * that are due on a specific date.
	 */
	TASK_LIST_ON_DATE,
	
	/**
	 * The statistics are being shown
	 */
	STATS,
	
	/** The task is being displayed from the calendar view (e.g. as a popup). */
	CALENDAR,
	
	/**
	 * The tasks are being displayed as the result of a search.
	 */
	SEARCH
}
