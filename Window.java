import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import java.util.Vector;

/**
 * Used as the user interface for the program.
 * 
 * @author Jonathan Sohrabi 2018
 */
public class Window extends JFrame {
	/**
	 * Holds all objects that are in the heap, in order. Used for updating the JList.
	 */
	private Vector <String > tasks;
	/**
	 * Holds all Task objects in a minheap.
	 */
	private Heap < Task > h;
	/**
	 * Label for the task list.
	 */
	private JLabel taskListLabel;
	/**
	 * Button that closes the program and saves the heap to a file.
	 */
	private JButton quitButton;
	/**
	 * Displays all currently open tasks.
	 */
	private JList < String > taskList;
	/**
	 * Allows for scrolling through the JList.
	 */
	private JScrollPane listScrollPane;
	/**
	 * Label for the name of the current task.
	 */
	private JLabel curTaskLabel;
	/**
	 * Shows the name of the current task.
	 */
	private JTextField curTaskTxt;
	/**
	 * Label for the "complete by" field.
	 */
	private JLabel compByLabel;
	/**
	 * Shows the date and time of when the task should be completed.
	 */
	private JTextField compByTxt;
	/**
	 * Button that marsk the current task as complete.
	 */
	private JButton completeButton;
	/**
	 * Button that toggles between the postpone and add task forms.
	 */
	private JButton postponeAddButton;
	/**
	 * Label that tells whether the user is using the postpone form or add task form.
	 */
	private JLabel postponeAddLabel;
	/**
	 * Label for the date area of the add/postpone form.
	 */
	private JLabel dateLabel;
	/**
	 * User input area for the month in the add/postpone task form.
	 */
	private JTextField monthTxt;
	/**
	 * User input area for the date in the add/postpone task form.
	 */
	private JTextField dayTxt;
	/**
	 * User input area for the year in the add/postpone task form.
	 */
	private JTextField yearTxt;
	/**
	 * Label for the time area of the add/postpone form.
	 */
	private JLabel timeLabel;
	/**
	 * User input area for the hours in the add/postpone task form.
	 */
	private JTextField timeHrTxt;
	/**
	 * User input area for the minutes in the add/postpone task form.
	 */
	private JTextField timeMinTxt;
	/**
	 * Label for the task name area of the add task form.
	 */
	private JLabel nameLabel;
	/**
	 * User input for the task name in the add task form
	 */
	private JTextField nameTxt;
	/**
	 * Button to submit the addition of postponing of a task.
	 */
	private JButton submitButton;
	/**
	 * Used to verify the inputs of the form fields.
	 */
	private TextInputVerifier vrf;
	
	/**
	 * Constructor for the Window class.
	 * Populates tasks vector with elements of the heap.
	 * Creates a new JFrame object and populates it with JPanels.
	 * 
	 * @param h The heap used to populate the tasks vector.
	 */
	public Window( Heap < Task > h ) {
		setTitle( "Task List" );			// Sets the title of the JFrame to "Task List".
		
		this.h = h;
		tasks = new Vector< String >();		// Create new String vector.
		vrf = new TextInputVerifier();		// Create new TextInputVerifier object.

		// Adds all element names of the heap to the tasks vector.
		for ( int i = 0; i < h.getSize(); i++ ) {
			tasks.addElement( h.toString().split( "=" )[ i ].split( "," )[ 0 ] );
		}

		UIManager.put( "Label.disabledForeground", Color.BLACK );	// Set the disabled Component text color to black instead of gray.
		
		TaskListPanel tlPanel = new TaskListPanel();
		CurTaskPanel ctPanel = new CurTaskPanel();
		PostponeAddPanel paPanel = new PostponeAddPanel();
		
		// Add JPanels to the Window.
		this.add( tlPanel );
		this.add( ctPanel );
		this.add( paPanel );
		
		this.setSize( 600, 400 );								// Set the window size.
		this.setLocationRelativeTo( null );						// Set window location to center of the screen.
		this.setLayout( null );									// Set window layout as null
		this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );	// Close window when exit button clicked.
		this.setVisible( true );								// Set window as visible.
	}
	
	/**
	 * JPanel for showing the current task list.
	 * Houses the the JList and quit button.
	 * 
	 * @author Jonathan Sohrabi 2018
	 */
	private class TaskListPanel extends JPanel implements ActionListener {
		/**
		 * Constructor of the TaskListPanel.
		 * Sets Components on this JPanel.
		 */
		TaskListPanel() {
			this.setLayout( null );						// Set layout as null.
			this.setBounds( 0, 0, 200, 400 );			// Set (x,y) position to (0,0) on the parent, size to 200x400.
			
			taskListLabel = new JLabel( "Tasks:" );
			taskListLabel.setBounds( 10, 5, 50, 30 );	// Set (x,y) position to (10,5) on the JPanel, size to 50x30.
			add( taskListLabel );						// Add this component to the JPanel.
			
			taskList = new JList < String > ( tasks );	// Create a new JList and populate it with the values in tasks vector.
			taskList.setEnabled( false );				// Set JList an non-interactable.
			
			listScrollPane = new JScrollPane( taskList );	// Create a JScrollPane to allow for scrolling through the list.
			listScrollPane.setBounds( 20, 35, 175, 280 );	// Set (x,y) position to (20,35) on the JPanel, size to 175x280.
			add( listScrollPane );							// Add this component to the JPanel
			
			quitButton = new JButton( "Quit" );
			quitButton.setBounds( 70, 325, 80, 25 );	// Set (x,y) position to (70,325) on the JPanel, size to 80,25.
			quitButton.addActionListener( this );		// Add an ActionListener to this Component.
			add( quitButton );							// Add this component to the JPanel.
		}

		/**
		 * When an ActionEvent occurs in this JPanel, saves the contents of the heap to a textfile and quits the program.
		 */
		@Override
		public void actionPerformed( ActionEvent e ) {
			if ( e.getSource() == quitButton ) {
				h.saveHeap();
				System.exit( 0 );
			}
		}
	}
	
	/**
	 * JPanel for information on the current task.
	 * Contains components related to displaying information about the current task.
	 * 
	 * @author Jonathan Sohrabi 2018
	 */
	private class CurTaskPanel extends JPanel implements ActionListener {
		/**
		 * Constructor of the CurTaskPanel.
		 * Sets components on this JPanel.
		 */
		CurTaskPanel() {
			this.setLayout( null );							// Set the layout of this JFrame as null.
			this.setBounds( 245, 15, 350, 180 );			// Set this JPanel position on the parent JFrame at (245, 15) with a width and height of 350x180.
			
			curTaskLabel = new JLabel( "Current Task:" );
			curTaskLabel.setBounds( 0, 0, 90, 30 );			// Set (x,y) position to (0,0) on the JPanel, size to 90x30.
			this.add( curTaskLabel );						// Add this component to the JPanel.
			
			curTaskTxt = new JTextField();
			// If heap size > 0
			if ( h.getSize() > 0 ) {
				curTaskTxt.setText( h.getCurrent().toString().split( "," )[ 0 ] );	// Set the contents of this text field to the name of the current task.
			// If heap size = 0
			} else {
				curTaskTxt.setText( "" );											// Set the contents of this text field to the name of the current task.
			}
			curTaskTxt.setEnabled( false );										// Set this component as disabled.
			curTaskTxt.setDisabledTextColor( Color.BLACK );						// Set this component disabled text color as black.
			curTaskTxt.setBounds( 5, 25, 300, 25 );								// Set (x,y) position to (5,25) on the JPanel, size to 300x25.
			this.add( curTaskTxt );												// Add this component to the JPanel.
			
			compByLabel = new JLabel( "Complete By:" );
			compByLabel.setBounds( 0, 45, 90, 30 );		// Set (x,y) position to (0,45) on the JPanel, size to 90x30.
			this.add( compByLabel );					// Add this component to the JPanel.
			
			compByTxt = new JTextField();
			// If heap size > 0
			if ( h.getSize() > 0 ) {
				compByTxt.setText( h.getCurrent().toString().split( "," )[ 1 ] );	// Set the contents of this text field to the date of the current task.
			// If heap size = 0
			} else {
				compByTxt.setText( "" );											// Set the contents of this text field to an empty string.
			}
			compByTxt.setEnabled( false );										// Set this component as disabled.
			compByTxt.setDisabledTextColor( Color.BLACK );						// Set this component disabled text color as black.
			compByTxt.setBounds( 5, 70, 300, 25 );								// Set (x,y) position to (5,70) on the JPanel, size to 300x25.
			this.add( compByTxt );												// Add this component to the JPanel.
			
			completeButton = new JButton( "Complete Task" );
			completeButton.setBounds( 25, 120, 125, 25 );		// Set (x,y) position to (25,120) on the JPanel, size to 125x25.
			completeButton.addActionListener( this );			// Add an ActionListener to this component.
			this.add( completeButton );							// Add this component to the JPanel.
			
			postponeAddButton = new JButton( "Postpone" );
			postponeAddButton.setBounds( 160, 120, 125, 25 );	// Set (x,y) position to (160, 120) on the JPanel, size to 125x25.
			postponeAddButton.addActionListener( this );		// Add an ActionListener to this component.
			this.add( postponeAddButton );						// Add this component to the JPanel.
		}

		/**
		 * Performs a set of actions depending on what component triggered an ActionEvent.
		 */
		@Override
		public void actionPerformed( ActionEvent e ) {
			// If the complete task button triggered the ActionEvent:
			if ( e.getSource() == completeButton ) {
				// If the heap is nonempty:
				if ( h.getSize() != 0 ) {
					String removedItem = ( ( Task ) h.removeItem() ).getName();				// Remove the root of the heap and get its name.
					tasks.removeElement( removedItem );										// Remove the same item from the tasks vector.
					// If the heap is still nonempty:
					if ( h.getSize() != 0 ) {
						curTaskTxt.setText( h.getCurrent().toString().split( "," )[ 0 ] );	// Fill the current task textbox with the name of the current task.
						compByTxt.setText( h.getCurrent().toString().split( "," )[ 1 ] );	// Fill the complete by textbox with the date of the current task.
						// If the postpone/add button form is toggled to postponing:
						if ( postponeAddButton.getText().equals( "Add Task" ) ) {
							monthTxt.setText( Integer.toString( h.getCurrent().getDate().getMonth() + 1 ) );	// Fill the postpone month textbox with the month of the current task. 
							dayTxt.setText( Integer.toString( h.getCurrent().getDate().getDate() ) );			// Fill the postpone date textbox with the date of the current task.
							yearTxt.setText( Integer.toString( h.getCurrent().getDate().getYear() + 1900 ) );	// Fill the postpone year textbox with the year of the current task.
							timeHrTxt.setText( Integer.toString( h.getCurrent().getDate().getHours() ) );		// Fill the postpone hours textbox with the hour of the current task.
							// Fill the postpone minutes textbox with the minutes of the current task. If-statements are for formatting purposes.
							if ( h.getCurrent().getDate().getMinutes() < 10 ) {
								timeMinTxt.setText( "0" + h.getCurrent().getDate().getMinutes() );
							} else {
								timeMinTxt.setText( Integer.toString( h.getCurrent().getDate().getMinutes() ) );
							}
						}
					}
					taskList.setListData( tasks );		// Fill the JList with the new vector values.
				// If the heap is empty, fill the current task and complete by textboxes with empty strings. 
				} else {
					curTaskTxt.setText( "" );
					compByTxt.setText( "" );
				}
			// If the postpone/add button triggered the ActionEvent:
			} else if ( e.getSource() == postponeAddButton ) {
				// If the postpone/add panel is set to adding a task:
				if ( postponeAddLabel.getText().equals( "Add Task:" ) ) {
					Task curTask = ( Task )h.getCurrent();					// Get the current Task, store it in a variable.
					postponeAddButton.setText( "Add Task" );				// Set the text of the postpone/add toggle button to "Add Task".
					postponeAddLabel.setText( "Postpone:" );				// Set the postpone/add panel label text to "Postpone".
					// Get the information about the current task and put the information into the date data fields.
					monthTxt.setText( Integer.toString( curTask.getDate().getMonth() + 1 ) );
					dayTxt.setText( Integer.toString( curTask.getDate().getDate() ) );
					yearTxt.setText( Integer.toString( curTask.getDate().getYear() + 1900 ) );
					timeHrTxt.setText( Integer.toString( curTask.getDate().getHours() ) );
					// If-statement for formatting the minutes.
					if ( curTask.getDate().getMinutes() < 10 ) {
						timeMinTxt.setText( "0" + Integer.toString( curTask.getDate().getMinutes() ) );
					} else {
						timeMinTxt.setText( Integer.toString( curTask.getDate().getMinutes() ) );
					}
					// Hide the name label and text box.
					nameLabel.setVisible( false );
					nameTxt.setVisible( false );
				// If the postpone/add panel is toggled to postponing a task:
				} else if ( postponeAddLabel.getText().equals( "Postpone:" ) ) {
					postponeAddButton.setText( "Postpone" );		// Set postpone/add toggle button text.
					postponeAddLabel.setText( "Add Task:" );		// Set postpone/add label text.
					monthTxt.setText( "MM" );						// Set month textbox text.
					dayTxt.setText( "DD" );							// Set date textbox text.
					yearTxt.setText( "YYYY" );						// Set year textbox text.
					timeHrTxt.setText( "HH" );						// Set hours textbox text.
					timeMinTxt.setText( "MM" );						// Set minutes textbox text.
					nameLabel.setVisible( true );					// Set new task name level as visible.
					nameTxt.setVisible( true );						// Set new task name textbox as visible.
				}
			}
		}
	}
	
	/**
	 * JPanel for adding or postponing a task.
	 * Contains components related to adding or postponing a task.
	 * 
	 * @author Jonathan Sohrabi 2018
	 */
	private class PostponeAddPanel extends JPanel implements ActionListener {
		/**
		 * Constructor of the PostponeAddPanel.
		 * Sets components on this JPanel. 
		 */
		PostponeAddPanel() {
			this.setLayout( null );					// Sets null layout.
			this.setBounds( 245, 200, 320, 400 );	// Set (x,y) position to (245,200) on the JPanel, size to 320x400.
			
			postponeAddLabel = new JLabel( "Add Task:" );
			postponeAddLabel.setBounds( 0, 0, 90, 30 );		// Set (x,y) position to (0,0) on the JPanel, size to 90x30.
			this.add( postponeAddLabel );					// Adds this component to the JPanel.
			
			dateLabel = new JLabel( "Date:" );
			dateLabel.setBounds( 10, 25, 30, 30 );			// Set (x,y) position to (10,25) on the JPanel, size to 30x30.
			this.add( dateLabel );							// Adds this component to the JPanel.
			
			monthTxt = new JTextField( "MM" );
			monthTxt.setBounds( 50, 30, 40, 25 );			// Set (x,y) position to (50,30) on the JPanel, size to 40x25.
			this.add( monthTxt );							// Add this component to the JPanel.
			
			dayTxt = new JTextField( "DD" );
			dayTxt.setBounds( 95, 30, 40, 25 );				// Set (x,y) position to (95,30) on the JPanel, size to 40x25.
			this.add( dayTxt );								// Add this component to the JPanel.
			
			yearTxt = new JTextField( "YYYY" );
			yearTxt.setBounds( 145, 30, 40, 25 );			// Set (x,y) position to (145,30) on the JPanel, size to 40x25.
			this.add( yearTxt );							// Add this component to the JPanel.
			
			timeLabel = new JLabel( "Time:" );
			timeLabel.setBounds( 10, 55, 35, 30 );			// Set (x,y) position to (10,55) on the JPanel, size to 35,30.
			this.add( timeLabel );							// Add this component to the JPanel.
			
			timeHrTxt = new JTextField( "HH" );
			timeHrTxt.setBounds( 50, 60, 40, 25 );			// Set (x,y) position to (50,60) on the JPanel, size to 40,25.
			this.add( timeHrTxt );							// Add this component to the JPanel.
			
			timeMinTxt = new JTextField( "MM" );
			timeMinTxt.setBounds( 95, 60, 40, 25 );			// Set (x,y) position to (95,60) on the JPanel, size to 40x25.
			this.add( timeMinTxt );							// Add this component to the JPanel.
			
			nameLabel = new JLabel( "Task:" );
			nameLabel.setBounds( 10, 86, 40, 25 );			// Set (x,y) position to (10,86) on the JPanel, size to 40x25.
			this.add( nameLabel );							// Add this component to the JPanel.
			
			nameTxt = new JTextField( "New Task" );
			nameTxt.setBounds( 50, 90, 200, 25 );			// Set (x,y) position to (50,90) on the JPanel, size to 200x25.
			this.add( nameTxt );							// Add this component to the JPanel.
			
			submitButton = new JButton( "Submit" );
			submitButton.setBounds( 90, 125, 100, 25 );		// Set (x,y) position to (90,125) on the JPanel, size to 100x25.
			submitButton.addActionListener( this );			// Add an ActionListener to this component.
			this.add( submitButton );						// Add this component to the JPanel.
		}

		/**
		 * Performs a set of actions depending on the component that triggered an ActionEvent.
		 */
		@Override
		public void actionPerformed( ActionEvent e ) {
			Task curTask;										// Used to hold the current task.
			int newYear, newMonth, newDate, newHours, newMins;	// Used to hold the new date information.
			String newName;										// Used to hold the new task name.
			// If the submit button fired the ActionEvent:
			if ( e.getSource() == submitButton ) {
				// If the postpone/add panel is toggled to postpone:
				if ( postponeAddLabel.getText().equals( "Postpone:" ) ) {
					// If the date is valid.
					if ( vrf.verifyMonth() && vrf.verifyDate() && vrf.verifyYear()  && vrf.verifyHours() && vrf.verifyMinutes() ) {
						curTask = h.removeItem();								// Get the top element of the heap by removing it.
						newYear = Integer.parseInt( yearTxt.getText() ) - 1900;	// Get the new year.
						newMonth = Integer.parseInt( monthTxt.getText() ) - 1;	// Get the new month.
						newDate = Integer.parseInt( dayTxt.getText() );			// Get the new date.
						newHours = Integer.parseInt( timeHrTxt.getText() );		// Get the new hours.
						newMins = Integer.parseInt( timeMinTxt.getText() );		// Get the new minutes.
						// Add the task back to the heap, but with a different date.
						h.addItem( new Task( curTask.getName(), new Date( newYear, newMonth, newDate, newHours, newMins ) ) );
						tasks.removeAllElements();								// Remove all elements from the tasks vector.
						// Add everything from the heap back to the vector, in sorted order.
						for ( int i = 0; i < h.getSize(); i++ ) {
							tasks.addElement( h.toString().split( "=" )[ i ].split( "," )[ 0 ] );
						}
						taskList.setListData( tasks );				// Set the JList to the values in the vector to update it.
						
						// Reset and refresh the contents of the fields.
						curTaskTxt.setText( h.getCurrent().toString().split( "," )[ 0 ] );
						compByTxt.setText( h.getCurrent().toString().split( "," )[ 1 ] );
						monthTxt.setText( Integer.toString( h.getCurrent().getDate().getMonth() + 1) );
						dayTxt.setText( Integer.toString( h.getCurrent().getDate().getDate() ) );
						yearTxt.setText( Integer.toString( h.getCurrent().getDate().getYear() + 1900 ) );
						timeHrTxt.setText( Integer.toString( h.getCurrent().getDate().getHours() ) );
						if ( h.getCurrent().getDate().getMinutes() < 10 ) {
							timeMinTxt.setText( "0" + h.getCurrent().getDate().getMinutes() );
						} else {
							timeMinTxt.setText( Integer.toString( h.getCurrent().getDate().getMinutes() ) );
						}
					}
				// If the add/postpone panel is toggled to add a task:
				} else if ( postponeAddLabel.getText().equals( "Add Task:" ) ) {
					// If the date is valid:
					if ( vrf.verifyMonth() && vrf.verifyDate() && vrf.verifyYear()  && vrf.verifyHours() && vrf.verifyMinutes() ) {
						// Get the new date and name information.
						newYear = Integer.parseInt( yearTxt.getText() ) - 1900;
						newMonth = Integer.parseInt( monthTxt.getText() ) - 1;
						newDate = Integer.parseInt( dayTxt.getText() );
						newHours = Integer.parseInt( timeHrTxt.getText() );
						newMins = Integer.parseInt( timeMinTxt.getText() );
						newName = vrf.verifyName( nameTxt.getText() );
						// Add the new task to the heap.
						h.addItem( new Task( newName, new Date( newYear, newMonth, newDate, newHours, newMins ) ) );
						tasks.removeAllElements();			// Remove all elements from the tasks vector.
						// Add everything from the heap back to the vector, in sorted order.
						for ( int i = 0; i < h.getSize(); i++ ) {
							tasks.addElement( h.toString().split( "=" )[ i ].split( "," )[ 0 ] );
						}
						taskList.setListData( tasks );		// Set the JList to the values in the tasks vector to refresh it.
						
						// Reset and refresh the contents of fields.
						curTaskTxt.setText( h.getCurrent().toString().split( "," )[ 0 ] );
						compByTxt.setText( h.getCurrent().toString().split( "," )[ 1 ] );
						monthTxt.setText( Integer.toString( h.getCurrent().getDate().getMonth() + 1) );
						dayTxt.setText( Integer.toString( h.getCurrent().getDate().getDate() ) );
						yearTxt.setText( Integer.toString( h.getCurrent().getDate().getYear() + 1900 ) );
						timeHrTxt.setText( Integer.toString( h.getCurrent().getDate().getHours() ) );
						if ( h.getCurrent().getDate().getMinutes() < 10 ) {
							timeMinTxt.setText( "0" + h.getCurrent().getDate().getMinutes() );
						} else {
							timeMinTxt.setText( Integer.toString( h.getCurrent().getDate().getMinutes() ) );
						}
						nameTxt.setText( "New Task" );
					}
				}
			}
		}
	}
	
	/**
	 * Contains methods for validating the contents of the form fields.
	 * 
	 * @author Jonathan Sohrabi 2018
	 */
	private class TextInputVerifier {
		/**
		 * Checks the validity of the month text field.
		 * Displays an error if invalid.
		 * 
		 * @return True if the contents are valid, false otherwise.
		 */
		private boolean verifyMonth() {
			String text = monthTxt.getText();			// Get the value of the month text field.
			try {
				int value = Integer.parseInt( text );	// Attempt to parse it as an integer.
				// If the month is out of range, show an error to the user and return false.
				if ( value < 1 || value > 12 ) {
					JOptionPane.showMessageDialog( Window.this, "Invalid Month.", "Input Error", JOptionPane.ERROR_MESSAGE );
					return false;
				// If the input was fine, return true.
				} else {
					return true;
				}
			// If the month could not be parsed as an integer, show an error to the user and return false.
			} catch ( NumberFormatException e ) {
				JOptionPane.showMessageDialog( Window.this, "Invalid Month.", "Input Error", JOptionPane.ERROR_MESSAGE );
				return false;
			}
		}

		/**
		 * Checks the validity of the date text field.
		 * Displays an error if invalid.
		 * 
		 * @return True if the contents are valid, false otherwise.
		 */
		private boolean verifyDate() {
			String text = dayTxt.getText();				// Get the value of the date text field.
			try {
				int value = Integer.parseInt( text );	// Attempt to parse it as an integer.
				// If the date is out of range, show an error to the user and return false.
				if ( value < 1 || value > 31 ) {
					JOptionPane.showMessageDialog( Window.this, "Invalid Date.", "Input Error", JOptionPane.ERROR_MESSAGE );
					return false;
				// If the input was fine, return true.
				} else {
					return true;
				}
			// If the date could not be parsed as an integer, show an error to the user and return false.
			} catch ( NumberFormatException e ) {
				JOptionPane.showMessageDialog( Window.this, "Invalid Date.", "Input Error", JOptionPane.ERROR_MESSAGE );
				return false;
			}
		}

		/**
		 * Checks the validity of the year text field.
		 * Displays an error if invalid.
		 * 
		 * @return True if the contents are valid, false otherwise.
		 */
		private boolean verifyYear() {
			String text = yearTxt.getText();			// Get the value of the year text field.
			try {
				int value = Integer.parseInt( text );	// Attempt to parse it as an integer.
				// If the year was invalid, show an error to the user and return false.
				if ( value < 0 ) {
					JOptionPane.showMessageDialog( Window.this, "Invalid Year.", "Input Error", JOptionPane.ERROR_MESSAGE );
					return false;
				// If the input was fine, return true.
				} else {
					return true;
				}
			// If the year could not be parsed as an integer, show an error to the user and return false.
			} catch ( NumberFormatException e ) {
				JOptionPane.showMessageDialog( Window.this, "Invalid Year.", "Input Error", JOptionPane.ERROR_MESSAGE );
				return false;
			}
		}

		/**
		 * Checks the validity of the hours text field.
		 * Displays an error if invalid.
		 * 
		 * @return True if the contents are valid, false otherwise.
		 */
		private boolean verifyHours() {
			String text = timeHrTxt.getText();			// Get the value of the hours text field.
			try {
				int value = Integer.parseInt( text );	// Attempt to parse it as an integer.
				// If the hour is invalid, show an error to the user and return false.
				if ( value < 0 || value > 23 ) {
					JOptionPane.showMessageDialog( Window.this, "Invalid Hours.", "Input Error", JOptionPane.ERROR_MESSAGE );
					return false;
				// If the input was fine, return true.
				} else {
					return true;
				}
			// If the hours could not be parsed as an integer, show an error to the user and return false.
			} catch ( NumberFormatException e ) {
				JOptionPane.showMessageDialog( Window.this, "Invalid Hours.", "Input Error", JOptionPane.ERROR_MESSAGE );
				return false;
			}
		}

		/**
		 * Checks the validity of the minutes text field.
		 * Displays an error if invalid.
		 * 
		 * @return True if the contents are valid, false otherwise.
		 */
		private boolean verifyMinutes() {
			String text = timeMinTxt.getText();			// Get the value of the minutes text field.
			try {
				int value = Integer.parseInt( text );	// Try to parse it as an integer.
				// If the mintues are invalid, show an error to the user and return false.
				if ( value < 0 || value > 60 ) {
					JOptionPane.showMessageDialog( Window.this, "Invalid Minutes.", "Input Error", JOptionPane.ERROR_MESSAGE );
					return false;
				// If the input was fine, return true.
				} else {
					return true;
				}
			// If the minutes could not be parsed as an integer, show an error to the user and return false.
			} catch ( NumberFormatException e ) {
				JOptionPane.showMessageDialog( Window.this, "Invalid Minutes.", "Input Error", JOptionPane.ERROR_MESSAGE );
				return false;
			}
		}

		/**
		 * Returns the name parameter without equal signs or commas to ensure safe data entry.
		 * 
		 * @return The task name without equal signs or commas.
		 */
		private String verifyName( String name ) {
			return name.replaceAll( ",", " " ).replaceAll( "=", " ");		// Replace all equal signs and commas with a space.
		}
	}
}