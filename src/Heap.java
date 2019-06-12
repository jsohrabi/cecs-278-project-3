import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Scanner;
import java.util.Vector;

/**
 * Data structure used to store objects in a min heap.
 * 
 * @author Jonathan Sohrabi 2018
 *
 * @param <T> The type of object to use this with.
 */
public class Heap < T extends Comparable< T > > {
	/**
	 * Holds the stored objects.
	 */
	private Vector< T > taskList;
	
	/**
	 * File object to populate this from.
	 */
	private File taskFile;
	
	/**
	 * Default constructor. Initialized variables and populates from a saved file.
	 */
	public Heap() {
		taskList = new Vector< T >();
		taskFile = new File( "./taskList.txt" );
		loadHeap();
	}
	
	/**
	 * Get the size of the taskList vector.
	 * 
	 * @return The size of the taskList vector.
	 */
	public int getSize() {
		return taskList.size();
	}
	
	/**
	 * Checks if the taskList vector has no values.
	 * 
	 * @return True if the taskList vector has no values, false otherwise.
	 */
	public boolean isEmpty() {
		return taskList.isEmpty();
	}
	
	/**
	 * Gets the parent node of the parameterized index.
	 * 
	 * @param i The index to get the parent of.
	 * @return The parent of the parameterized index.
	 */
	private int getPLoc( int i ) {
		return ( i - 1 ) / 2;
	}
	
	/**
	 * Gets the left child node of the parameterized index.
	 * 
	 * @param i The index to get the left child of.
	 * @return The left child of the parameterized index.
	 */
	private int getLCLoc( int i ) {
		return ( 2 * i ) + 1;
	}
	
	/**
	 * Gets the right child node of the parameterized index.
	 * 
	 * @param i The index to get the right child of.
	 * @return The right child of the parameterized index.
	 */
	private int getRCLoc( int i ) {
		return ( 2 * i ) + 2;
	}
	
	/**
	 * Gets the object at the specified index.
	 * 
	 * @param i The index of the object.
	 * @return The object at the index.
	 */
	private T getItem( int i ) {
		return this.taskList.get( i );
	}
	
	/**
	 * Adds an item to the minheap and adjusts accordingly.
	 * 
	 * @param i The item to add
	 */
	public void addItem( T i ) {
		this.taskList.add( i );					// Add the item to the vector.
		int index = this.taskList.size() - 1;	// Get the index of the item that was just added.
		// While the current index is not the root and the parent of the current index is greater than the item that was just added:
		while( index > 0 && getItem( getPLoc( index ) ).compareTo( i ) > 0 ) {
			this.taskList.set( index, getItem( getPLoc( index ) ) );	// Swap the item at the current index and its parent.
			index = getPLoc( index );			// Get the parent of the current index.
		}
		this.taskList.set( index, i );			// Swap the item at the current index and the item that was added.
	}
	
	/**
	 * Returns this heap as a string.
	 */
	@Override
	public String toString() {
		String s = "";
		T removedItem;							// Holds an item that was removed from the heap.
		Vector < T > v = new Vector < T > ();	// Used to store the elements of the heap as they are removed.
		// While the taskList vector is not empty:
		while ( getSize() > 0 ) {
			removedItem = removeItem();			// Set the removed item to the heap's root by removing it.
			s += removedItem;					// Appends the item to a string.
			// Add an equal sign between items, for delimiting purposes.
			if ( getSize() != 0 ) {
				s += "=";
			}
			v.addElement( removedItem );		// Add the removed item to the vector v.
		}
		// Add every item in the vector v back to the heap.
		for ( T ele : v ) {
			this.addItem( ele );
		}
		return s;
	}
	
	/**
	 * Get the item at the root of the heap.
	 * @return The item at index 0 of the taskList vector.
	 */
	public T getCurrent() {
		return this.taskList.get( 0 );
	}
	
	/**
	 * Removes the root of the heap and adjusts the heap accordingly.
	 * 
	 * @return The removed item.
	 */
	public T removeItem() {
		T min = this.taskList.get( 0 );				// Get the min value from the heap, i.e. the root.
		int index = this.taskList.size() - 1;		// Get the last index of the taskList vector.
		T last = this.taskList.remove( index );		// Remove the last element from the taskList vector.
		// If the last index is not the root element:
		if ( index > 0 ) {
			this.taskList.set( 0, last );			// Swap the root and the last element of the heap.
			T root = this.taskList.get( 0 );		// Get the new root of the heap.
			int end = this.taskList.size() - 1;		// Get the new end of the heap.
			index = 0;								// Set the current index to the root.
			boolean done = false;
			while ( !done ) {
				// If the left child of the current index is smaller than the end element:
				if ( getLCLoc( index ) <= end ) {
					T child = getItem( getLCLoc( index ) );	// Get the left child of the current index.
					int childLoc = getLCLoc( index );		// Get the index of the left child.
					// If the right child of the current index is smaller than the end element:
					if ( getRCLoc( index ) <= end ) {
						// If the right child is smaller than the left child:
						if ( getItem( getRCLoc( index ) ).compareTo( child ) < 0 ) {
							child = getItem( getRCLoc( index ) );	// Set the current working child to the right child.
							childLoc = getRCLoc( index );			// Set the current working child location to the right child's location.
						}
					}
					// If the current working child is smaller than the root:
					if ( child.compareTo( root ) < 0 ) {
						this.taskList.set( index, child );	// Swap the element at the current index with the child.
						index = childLoc;					// Set the current index to the child's index.
					// If the current working child is greater than the root, stop swapping.
					} else {
						done = true;
					}
				// If the left child of the current index is greater than the end element, stop swapping.
				} else {
					done = true;
				}
			}
			this.taskList.set( index, root );		// Swap the elements at the current index and the root.
		}
		return min;			// Return the old root element.
	}
	
	/**
	 * Reads from a text file to populate the heap with values.
	 */
	public void loadHeap() {
		Scanner scan = null;
		String readLines[];
		
		// If the file exists:
		if ( taskFile.exists() ) {
			try {
				scan = new Scanner( taskFile );
				while ( scan.hasNextLine() ) {
					readLines = scan.nextLine().split( "," );									// Split the input line at commas.
					addItem( ( T ) new Task( readLines[ 0 ], new Date( readLines[ 1 ] ) ) );	// Add the item to the heap.
				}
				scan.close();
			} catch ( FileNotFoundException e ) {
				System.out.println( "File not found" );
				System.exit( 0 );
			}
		}
	}
	
	/**
	 * Save the current contents of the heap to a text file.
	 */
	public void saveHeap() {
		String[] heapContents = this.toString().split( "=" );		// Split the heap at every equal sign.
		try {
			PrintWriter pw = new PrintWriter( taskFile );
			// For every element in the heapContents array, write that element to the text file.
			for ( int i = 0; i < heapContents.length; i++ ) {
				pw.println( heapContents[ i ] );
			}
			pw.close();
		} catch ( FileNotFoundException e ) {
			System.out.println( "Error writing to text file" );
		}
	}
}