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
	TASK_LIST,
	
	
	TASK_LIST_ON_DATE,
	
	/** The task is being displayed from the calendar view (e.g. as a popup). */
	CALENDAR
}
