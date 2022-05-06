
import java.util.NoSuchElementException;
import student.TestCase;

/**
 * Test cases for LinkedList class, codes from my 1st project
 * 
 * @author Wenjun Han
 * @version 2022.1.28
 */
public class LinkedListTest extends TestCase {

    private LinkedList<String> emptyList;
    private LinkedList<String> names;
    private LinkedList<String> letters;

    /**
     * Set up lists
     */
    public void setUp() {
        emptyList = new LinkedList<>();
        names = new LinkedList<>();
        letters = new LinkedList<>();
        names.append("bob");
        letters.append("A");
        letters.append("B");
        letters.append("C");

    }


    /**
     * test clear()
     */
    public void testClear() {
        assertEquals(3, letters.length());
        letters.clear();
        assertEquals(0, letters.length());
    }


    /**
     * test insert()
     */
    public void testInsert() {
        letters.insert("D");
        assertEquals(4, letters.length());
        letters.moveToStart();
        letters.insert("A");
        assertEquals(5, letters.length());
        assertEquals(0, letters.currPos());
        letters.next();
        letters.next();
        assertEquals("B", letters.getValue());
        letters.clear();
        letters.insert("A");
        assertEquals(1, letters.length());

    }


    /**
     * test append()
     */
    public void testAppend() {
        names.append("julie");
        assertEquals(2, names.length());
        names.clear();
        names.append("julie");
        assertEquals(1, names.length());
        assertEquals("julie", names.getValue());

    }


    /**
     * test remove()
     */
    public void testRemove() {
        names.remove();
        assertEquals(0, names.length());
        names.clear();
        Exception thrown = null;
        try {
            names.remove();
        }
        catch (Exception e) {
            thrown = e;
        }
        assertTrue(thrown instanceof NoSuchElementException);

        names.append("Bob");
        names.append("Lily");
        names.moveToStart();
        names.remove();
        assertEquals("Lily", names.getValue());
        assertEquals(1, names.length());
        letters.remove();
        letters.prev();
        assertEquals("A", letters.getValue());
        letters.moveToEnd();
        letters.append("C");
        letters.moveToPos(1);
        letters.remove();
        assertEquals("C", letters.getValue());

    }


    /**
     * test moveToStart()
     */
    public void testMoveToStart() {
        assertEquals("C", letters.getValue());
        letters.moveToStart();
        assertEquals("A", letters.getValue());
        letters.moveToStart();
        assertEquals("A", letters.getValue());
    }


    /**
     * test moveToEnd()
     */
    public void testMoveToEnd() {
        letters.moveToStart();
        assertEquals("A", letters.getValue());
        letters.moveToEnd();
        assertEquals("C", letters.getValue());
        letters.moveToEnd();
        assertEquals("C", letters.getValue());
    }


    /**
     * test prev()
     */
    public void testPrev() {
        assertEquals("C", letters.getValue());
        letters.prev();
        assertEquals("B", letters.getValue());
        letters.moveToEnd();
        letters.prev();
        assertEquals("B", letters.getValue());
        letters.moveToStart();
        assertEquals("A", letters.getValue());
        letters.prev();
        assertEquals("A", letters.getValue());
    }


    /**
     * test next()
     */
    public void testNext() {
        assertEquals("C", letters.getValue());
        letters.moveToStart();
        assertEquals("A", letters.getValue());
        letters.moveToEnd();
        letters.next();
        assertEquals("C", letters.getValue());
    }


    /**
     * test length()
     */
    public void testLength() {
        assertEquals(0, emptyList.length());
    }


    /**
     * test currPos()
     */
    public void testCurrPos() {
        assertEquals(2, letters.currPos());
        letters.prev();
        assertEquals(1, letters.currPos());
        letters.prev();
        assertEquals(0, letters.currPos());
    }


    /**
     * test moveToPos()
     */
    public void testMoveToPos() {
        assertTrue(letters.moveToPos(1));
        assertEquals("B", letters.getValue());
        assertFalse(letters.moveToPos(-1));
        assertFalse(letters.moveToPos(10));

    }


    /**
     * test isAtEnd()
     */
    public void testIsAtEnd() {
        assertTrue(letters.isAtEnd());
        letters.prev();
        assertFalse(letters.isAtEnd());
    }


    /**
     * test isAtStart()
     */
    public void testIsAtStart() {
        letters.moveToStart();
        assertTrue(letters.isAtStart());
        letters.prev();
        assertTrue(letters.isAtStart());
        letters.next();
        assertFalse(letters.isAtStart());
    }


    /**
     * test getValue()
     */
    public void testGetValue() {
        assertEquals("C", letters.getValue());
        letters.append(null);
        assertNull(letters.getValue());
    }


    /**
     * test isEmpty()
     */
    public void testIsEmpty() {
        assertTrue(emptyList.isEmpty());
        assertFalse(letters.isEmpty());
    }

}
