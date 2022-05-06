import student.TestCase;

/**
 * test case of min heap
 * 
 * @author Wenjun Han
 * @version 3.24.22
 *
 */
public class MinHeapTest extends TestCase {

    private MinHeap<Integer> minHeap;

    /**
     * set up the test case and test buildHeap()
     */
    public void setUp() {
        minHeap = new MinHeap<>(new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 },
            10, 10);
    }


    /**
     * test heapsize()
     */
    public void testHeapsize() {
        assertEquals(10, minHeap.heapsize());
    }


    /**
     * test isLeaf()
     */
    public void testIsLeaf() {
        assertTrue(minHeap.isLeaf(8));
        assertTrue(minHeap.isLeaf(9));
        assertFalse(minHeap.isLeaf(0));
        assertFalse(minHeap.isLeaf(-6));
        assertFalse(minHeap.isLeaf(16));
    }


    /**
     * test leftchild()
     */
    public void testLeftchild() {
        assertEquals(1, minHeap.leftchild(0));
        assertEquals(7, minHeap.leftchild(3));
        assertEquals(-1, minHeap.leftchild(9));
    }


    /**
     * test rightchild()
     */
    public void testRightchild() {
        assertEquals(2, minHeap.rightchild(0));
        assertEquals(6, minHeap.rightchild(2));
        assertEquals(-1, minHeap.rightchild(9));
    }


    /**
     * test parent()
     */
    public void testParent() {
        assertEquals(0, minHeap.parent(1));
        assertEquals(-1, minHeap.parent(0));
    }


    /**
     * test insert() and peek()
     */
    public void testInsertPeek() {
        minHeap.remove(9);
        minHeap.remove(8);
        minHeap.insert(0);
        assertEquals(0, (int)minHeap.peek());
        minHeap.insert(-5);
        assertEquals(-5, (int)minHeap.peek());
        assertEquals(minHeap.heapsize(), 10);
        MinHeap<Integer> another = new MinHeap<>(new Integer[] { 1, 2, 3, 0,
            0 }, 5, 5);
        another.insert(-1);
        assertEquals(0, (int)another.peek());
    }


    /**
     * test siftdown() edge cases
     */
    public void testSiftdown() {
        minHeap.siftdown(-5);
        minHeap.siftdown(15);
        assertEquals(1, (int)minHeap.peek());
        assertEquals(10, (int)minHeap.getHeap()[9]);
        minHeap.getHeap()[minHeap.heapsize() - 1] = -5;
        minHeap.update(minHeap.heapsize() - 1);
        minHeap.update(-5);
        assertEquals(-5, (int)minHeap.peek());

    }


    /**
     * test removemin()
     */
    public void testRemovemin() {
        minHeap.removemin();
        assertEquals(2, (int)minHeap.peek());
        MinHeap<Integer> another = new MinHeap<>(new Integer[5], 0, 10);
        another.removemin();
        assertNull(another.peek());
    }


    /**
     * test remove()
     */
    public void testRemove() {
        minHeap.remove(-5);
        minHeap.remove(23);
        minHeap.remove(0);
        assertEquals(2, (int)minHeap.peek());
    }


    /**
     * test getHeap()
     */
    public void testGetHeap() {
        assertEquals(1, (int)minHeap.getHeap()[0]);
        assertEquals(10, (int)minHeap.getHeap()[9]);
    }


    /**
     * test setHeapSize()
     */
    public void testSetHeapSize() {
        minHeap.setHeapsize(3);
        assertEquals(3, minHeap.heapsize());
    }
}
