/**
 * MinHeap structure, source code from OpenDSA - heap sort
 * 
 * @author Wenjun Han(hwenjun)
 * @version 3.23.22
 * @param <T>
 *            The data type
 */
public class MinHeap<T extends Comparable<? super T>> {

    private T[] heap; // Pointer to the heap array
    private int capacity; // Maximum size of the heap
    private int n; // Number of things now in heap

    /**
     * Constructor supporting preloading of heap contents
     * 
     * @param h
     *            Heap data array
     * @param num
     *            Number of elements inside the heap
     * @param max
     *            Capacity of heap
     */
    public MinHeap(T[] h, int num, int max) {
        heap = h;
        n = num;
        capacity = max;
        buildheap();
    }


    /**
     * Return current size of the heap
     * 
     * @return size of heap
     */
    public int heapsize() {
        return n;
    }


    /**
     * Set the size of heap
     * 
     * @param newSize
     *            The new size of heap
     */
    public void setHeapsize(int newSize) {
        n = newSize;
    }


    /**
     * Return true if position is a leaf position, false otherwise
     * 
     * @param pos
     *            The position of a node
     * @return True if this is a leaf, otherwise return false
     */
    public boolean isLeaf(int pos) {
        return (pos >= n / 2) && (pos < n);
    }


    /**
     * Return the position for left child of pos
     * 
     * @param pos
     *            The position we want to have the left child
     * @return The left child position, -1 means no left child
     */
    public int leftchild(int pos) {
        if (pos >= n / 2) {
            return -1;
        }
        return 2 * pos + 1;
    }


    /**
     * Return position for right child of pos
     * 
     * @param pos
     *            The position we want to have the right child
     * @return The right child position, -1 means no right child
     */
    public int rightchild(int pos) {
        if (pos >= (n - 1) / 2) {
            return -1;
        }
        return 2 * pos + 2;
    }


    /**
     * Return position for parent
     * 
     * @param pos
     *            The position we want to have its parent
     * @return The parent position, -1 means it is the root
     */
    public int parent(int pos) {
        if (pos <= 0) {
            return -1;
        }
        return (pos - 1) / 2;
    }


    /**
     * Insert value into heap
     * 
     * @param key
     *            The key value to insert to heap
     */
    public void insert(T key) {
        if (n >= capacity) {
            return;
        }
        int curr = n++;
        heap[curr] = key; // Start at end of heap
        // Now sift up until curr's parent's key < curr's key
        while ((curr != 0) && (heap[curr].compareTo(heap[parent(curr)]) < 0)) {
            swap(curr, parent(curr));
            curr = parent(curr);
        }
    }


    /**
     * Swap the value of two location in the heap
     * 
     * @param loc1
     *            The first location to swap
     * @param loc2
     *            The second location to swap
     */
    private void swap(int loc1, int loc2) {
        T temp = heap[loc1];
        heap[loc1] = heap[loc2];
        heap[loc2] = temp;
    }


    /**
     * Heapify contents of Heap
     */
    public void buildheap() {
        for (int i = n / 2 - 1; i >= 0; i--) {
            siftdown(i);
        }
    }


    /**
     * Put element in its correct place
     * 
     * @param pos
     *            The position of the element
     */
    public void siftdown(int pos) {
        if ((pos < 0) || (pos >= n)) {
            return;
        } // Illegal position
        while (!isLeaf(pos)) {
            int j = leftchild(pos);
            if ((j < (n - 1)) && (heap[j].compareTo(heap[j + 1]) > 0)) {
                j++; // j is now index of child with greater value
            }
            if (heap[pos].compareTo(heap[j]) <= 0) {
                return;
            }
            swap(pos, j);
            pos = j; // Move down
        }
    }


    /**
     * Remove and return minimum value, top element
     * 
     * @return The minimum value
     */
    public T removemin() {
        if (n == 0) {
            return null;
        } // Removing from empty heap
        swap(0, --n); // Swap minimum with last value
        siftdown(0); // Put new heap root val in correct place
        return heap[n];
    }


    /**
     * Remove and return element at specified position
     * 
     * @param pos
     *            The location of element to be removed
     * @return The element
     */
    public T remove(int pos) {
        if ((pos < 0) || (pos >= n)) {
            return null;
        } // Illegal heap position
        if (pos == (n - 1)) {
            n--;
        } // Last element, no work to be done
        else {
            swap(pos, --n); // Swap with last value
            update(pos);
        }
        return heap[n];
    }


    /**
     * The value at pos has been changed, restore the heap property
     * 
     * @param pos
     *            Update the element at this position
     */
    public void update(int pos) {
        // If it is a big value, push it up
        while ((pos > 0) && (heap[pos].compareTo(heap[parent(pos)]) < 0)) {
            swap(pos, parent(pos));
            pos = parent(pos);
        }
        siftdown(pos); // push down
    }


    /**
     * Get the heap array
     * 
     * @return The data array
     */
    public T[] getHeap() {
        return heap;
    }


    /**
     * Check the top element value
     * 
     * @return The element value
     */
    public T peek() {
        return heap[0];
    }

}
