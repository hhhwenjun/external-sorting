/**
 * The tracker of the run start and end
 * 
 * @author Wenjun Han
 * @version 3.25.22
 *
 */
public class RunTracker {
    private long start;
    private long length;

    /**
     * Constructor of the run tracker
     * 
     * @param start
     *            The start point of file pointer
     * @param length
     *            The length of the run
     */
    public RunTracker(long start, long length) {
        this.start = start;
        this.length = length;
    }


    /**
     * Get the start of the file pointer
     * 
     * @return The start of run
     */
    public long getStart() {
        return start;
    }


    /**
     * Get the end of the file pointer
     * 
     * @return The end of run
     */
    public long getEnd() {
        return start + length;
    }


    /**
     * Get the length of the run
     * 
     * @return The length of run
     */
    public long getLength() {
        return length;
    }


    /**
     * Set the start of the run
     * 
     * @param newStart
     *            New start of the run
     */
    public void setStart(long newStart) {
        this.start = newStart;
    }


    /**
     * Set the new length of the run
     * 
     * @param newLength
     *            New length of the run
     */
    public void setLength(long newLength) {
        this.length = newLength;
    }
}
