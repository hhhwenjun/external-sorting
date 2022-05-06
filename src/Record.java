import java.nio.ByteBuffer;

/**
 * Holds a single record
 * 
 * @author CS Staff
 * @version 2020-10-15
 */
public class Record implements Comparable<Record> {

    private byte[] completeRecord;

    /**
     * The constructor for the Record class
     * 
     * @param record
     *            The byte for this object
     */
    public Record(byte[] record) {
        completeRecord = record;
    }


    /**
     * returns the complete record
     * 
     * @return complete record
     */
    public byte[] getCompleteRecord() {
        return completeRecord;
    }


    /**
     * Returns the object's key
     * 
     * @return the key
     */
    public double getKey() {
        ByteBuffer buff = ByteBuffer.wrap(completeRecord);
        return buff.getDouble(8);
    }


    /**
     * Returns the object's Id
     * 
     * @return the Id of the object
     */
    public Long getId() {
        ByteBuffer buff = ByteBuffer.wrap(completeRecord);
        return buff.getLong();
    }


    /**
     * Compare Two Records based on their keys
     * 
     * @param toBeCompared
     *            The Record to be compared.
     * @return A negative integer, zero, or a positive integer as this employee
     *         is less than, equal to, or greater than the supplied record
     *         object.
     */
    @Override
    public int compareTo(Record toBeCompared) {
        return Double.compare(this.getKey(), toBeCompared.getKey());
    }


    /**
     * Outputs the record as a String
     * 
     * @return a string of what the record contains
     */
    public String toString() {
        return "" + this.getId() + " " + this.getKey();
    }

}
