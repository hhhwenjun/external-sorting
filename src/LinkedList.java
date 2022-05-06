import java.util.NoSuchElementException;

/**
 * A doubly LinkedList that implement list interface
 * 
 * @author Wenjun Han
 * @version 2022.1.28
 * @param <T>
 *            The data type
 */
public class LinkedList<T> implements ListADT<T> {

    private Node firstNode;
    private Node curr;
    private int numberOfNodes;

    /**
     * constructor that initialize the Doubly LinkedList
     */
    public LinkedList() {
        firstNode = null;
        curr = firstNode;
        numberOfNodes = 0;
    }


    /**
     * remove all contents from the list, so it can be empty again.
     */
    @Override
    public void clear() {
        firstNode = null;
        curr = firstNode;
        numberOfNodes = 0;
    }


    /**
     * Insert item at the current location
     * 
     * @param item
     * @return true if insert successfully, otherwise false
     */
    @Override
    public boolean insert(T item) {

        Node node = new Node(item);
        if (isEmpty()) {
            append(item);
            return true;
        }
        if (curr == firstNode) {
            node.setNext(firstNode);
            firstNode.setPrev(node);
            firstNode = node;
            curr = node;
        }
        else {
            Node prevNode = curr.getPrev();
            prevNode.setNext(node);
            node.setNext(curr);
            curr.setPrev(node);
            node.setPrev(prevNode);
            curr = node;
        }
        numberOfNodes++;
        return true;
    }


    /**
     * Append item at the end of the list
     * 
     * @param item
     * @return true if append successfully, otherwise false
     */
    @Override
    public boolean append(T item) {
        Node node = new Node(item);
        if (isEmpty()) {
            firstNode = node;
            curr = node;
        }
        else {
            moveToEnd();
            curr.setNext(node);
            node.setPrev(curr);
            curr = node;
        }
        numberOfNodes++;
        return true;
    }


    /**
     * Remove and return the current element
     * 
     * @return the removed item
     * @throws NoSuchElementException
     */
    @Override
    public T remove() throws NoSuchElementException {

        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        T result = curr.getData();
        Node nextNode = curr.getNext();
        Node prevNode = curr.getPrev();
        if (numberOfNodes == 1) {
            clear();
            return result;
        }
        else if (prevNode == null) {
            firstNode = nextNode;
            firstNode.setPrev(null);
            curr = nextNode;
        }
        else if (nextNode == null) {
            prevNode.setNext(null);
            curr.setPrev(null);
            curr = prevNode;
        }
        else {
            prevNode.setNext(nextNode);
            nextNode.setPrev(prevNode);
            curr = nextNode;
        }
        numberOfNodes--;
        return result;
    }


    /**
     * Set the current position to the start of the list
     */
    @Override
    public void moveToStart() {
        curr = firstNode;
    }


    /**
     * Set the current position to the end of the list
     */
    @Override
    public void moveToEnd() {
        Node temp = firstNode;
        while (temp.getNext() != null) {
            temp = temp.getNext();
        }
        curr = temp;
    }


    /**
     * Move the current position one step left, no change if already at
     * beginning
     */
    @Override
    public void prev() {
        if (curr.getPrev() != null) {
            Node prevNode = curr.getPrev();
            curr = prevNode;
        }
    }


    /**
     * Move the current position one step right, no change if already at end
     */
    @Override
    public void next() {
        if (curr.getNext() != null) {
            Node nextNode = curr.getNext();
            curr = nextNode;
        }
    }


    /**
     * Return the number of elements in the list
     * 
     * @return the length of the current list
     */
    @Override
    public int length() {

        return numberOfNodes;
    }


    /**
     * Return the position of the current element
     * 
     * @return the position of the item
     */
    @Override
    public int currPos() {
        int pos = 0;
        Node temp = firstNode;
        while (temp != curr) {
            temp = temp.getNext();
            pos++;
        }
        return pos;
    }


    /**
     * Set the current position to "position"
     * 
     * @param position
     * @return true if successful, otherwise false
     */
    @Override
    public boolean moveToPos(int position) {
        if (position < 0 || position >= numberOfNodes) {
            return false;
        }
        Node temp = firstNode;
        for (int i = 0; i < position; i++) {
            temp = temp.getNext();
        }
        curr = temp;
        return true;
    }


    /**
     * Return true if current position is at end of the list
     * 
     * @return true if position at the end, otherwise false
     */
    @Override
    public boolean isAtEnd() {

        return curr.getNext() == null;
    }


    /**
     * Return true if current position is at front of the list
     * 
     * @return true if position at the begin, otherwise false
     */
    public boolean isAtStart() {

        return curr.getPrev() == null;
    }


    /**
     * Return the current element
     * 
     * @return list item
     * @throws NoSuchElementException
     */
    @Override
    public T getValue() throws NoSuchElementException {

        return curr.getData();
    }


    /**
     * Return whether or not the List is empty
     * 
     * @return true if empty, otherwise false
     */
    @Override
    public boolean isEmpty() {

        return numberOfNodes == 0;
    }

    /**
     * Create a node class for the linkedlist. Reference code of CS2114
     * 
     * @author Wenjun Han
     * @param <T>
     *            The content type of node
     */
    private class Node {
        private T data;
        private Node next;
        private Node prev;

        public Node(T data) {
            this(data, null, null);
        }


        /**
         * Constructor for node
         * 
         * @param data
         *            the stored data
         * @param next
         *            point to the next node
         * @param prev
         *            point to the previous node
         */
        public Node(T data, Node next, Node prev) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }


        /**
         * get the next node
         * 
         * @return next node
         */
        public Node getNext() {
            return next;
        }


        /**
         * get the previous node
         * 
         * @return previous node
         */
        public Node getPrev() {
            return prev;
        }


        /**
         * set the previous node
         * 
         * @param prev
         *            node
         */
        public void setPrev(Node prev) {
            this.prev = prev;
        }


        /**
         * set the the next node
         * 
         * @param next
         *            point to the next node
         */
        public void setNext(Node next) {
            this.next = next;
        }


        /**
         * get the data of the node
         * 
         * @return data
         */
        public T getData() {
            if (data != null) {
                return data;
            }
            return null;
        }
    }

}
