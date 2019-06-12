import java.util.Date;

/**
 * Represents a Task object with a name and Date.
 * 
 * @author Jonathan Sohrabi 2018
 */
public class Task implements Comparable< Task > {
	/**
	 * The name of the task.
	 */
	private String taskName;
	/**
	 * The due date of the task.
	 */
	private Date dueDate;
	
	/**
	 * Constructor for creating a new Task.
	 * 
	 * @param tName The name of the Task.
	 * @param dDate	The due date of the Task.
	 */
	public Task( String tName, Date dDate ) {
		this.taskName = tName;
		this.dueDate = dDate;
	}
	
	/**
	 * Return the name of the Task.
	 * 
	 * @return The Task's name.
	 */
	public String getName() {
		return this.taskName;
	}
	
	/**
	 * Return the due date of the Task.
	 * 
	 * @return The Task's due date as a Date object.
	 */
	public Date getDate() {
		return this.dueDate;
	}

	/**
	 * Converts the Task to a String.
	 * String format is Name,MM/DD/YYYY HH:MM
	 */
	@Override
	public String toString() {
		String returnedString =  getName() + "," + ( getDate().getMonth() + 1 ) + "/" + getDate().getDate() + "/" +
				( getDate().getYear() + 1900 ) + " " + getDate().getHours() + ":";
		// Formatting for the minutes of the Task.
		if ( getDate().getMinutes() < 10 ) {
			returnedString += "0" + getDate().getMinutes();
		} else {
			returnedString += getDate().getMinutes();
		}
		
		return returnedString;
	}
	
	/**
	 * Compares two Task objects.
	 * Objects are first compared by Date. If both have the same Date, then they are compared by name.
	 * 
	 * @return <0 if this is before the parameter, 0 if the two are equal, >0 if this is after the parameter.
	 */
	@Override
	public int compareTo( Task t ) {
		int compared = this.getDate().compareTo( t.getDate() );	// Compare by Date.
		// If due dates are not the same, return the comparison.
		if ( compared != 0 ) {
			return compared; 	// Return less than 0 if this.dueDate is before t.dueDate. Return greater than 0 if this.dueDate is after t.dueDate.
		// If the due dates are the same, return the comparison by name.
		} else {
			return this.getName().compareTo( t.getName() );
		}
	}
}