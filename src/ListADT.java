import java.util.NoSuchElementException;

/**
 * An interface for the ADT list. Generalize by using "Object" for the element
 * type.
 * 
 * The reference codes originally from class notes CS5040.
 * 
 * @author Wenjun Han
 * @version 2022.1.27
 * @param <E>
 */
public interface ListADT<E> {

    /**
     * remove all contents from the list, so it can be empty again.
     */
    public void clear();


    /**
     * Insert item at the current location
     * 
     * @param item
     *            The element we want to insert
     * @return true if insert successfully, otherwise false
     */
    public boolean insert(E item);


    /**
     * Append item at the end of the list, The client must ensure that the
     * list's capacity is not exceeded
     * 
     * @param item
     *            The element to want to append to end of list
     * @return true if append successfully, otherwise false
     */
    public boolean append(E item);


    /**
     * Remove and return the current element
     * 
     * @return the removed item
     * @throws NoSuchElementException
     */
    public Object remove() throws NoSuchElementException;


    /**
     * Set the current position to the start of the list
     */
    public void moveToStart();


    /**
     * Set the current position to the end of the list
     */
    public void moveToEnd();


    /**
     * Move the current position one step left, no change if already at
     * beginning
     */
    public void prev();


    /**
     * Move the current position one step right, no change if already at end
     */
    public void next();


    /**
     * Return the number of elements in the list
     * 
     * @return the length of the current list
     */
    public int length();


    /**
     * Return the position of the current element
     * 
     * @return the position of the item
     */
    public int currPos();


    /**
     * Set the current position to "position"
     * 
     * @param position
     *            The destination of the pointer
     * @return true if successful, otherwise false
     */
    public boolean moveToPos(int position);


    /**
     * Return true if current position is at end of the list
     * 
     * @return true if position at the end, otherwise false
     */
    public boolean isAtEnd();


    /**
     * Return the current element
     * 
     * @return list item
     * @throws NoSuchElementException
     */
    public Object getValue() throws NoSuchElementException;


    /**
     * Return whether or not the List is empty
     * 
     * @return true if empty, otherwise false
     */
    public boolean isEmpty();
}
