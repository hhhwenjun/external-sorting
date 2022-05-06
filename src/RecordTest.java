
import java.nio.ByteBuffer;
import student.TestCase;

/**
 * Holds a single record
 * 
 * @author CS Staff
 * @version 2020-10-15
 */
public class RecordTest extends TestCase {

    private byte[] aBite;


    /**
     * The setup for the tests
     */
    public void setUp() {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES + Double.BYTES);
        buffer.putLong(7);
        buffer.putDouble(8, 1);
        aBite = buffer.array();
    }


    /**
     * Tests the first constructor
     */
    public void testConstruct1() {
        Record rec = new Record(aBite);
        assertEquals((double)1, rec.getKey(), 0.00);
        assertEquals((long)7, rec.getId(), 0);
        assertEquals(aBite, rec.getCompleteRecord());
        assertTrue(rec.toString().equals("7 1.0"));
    }
    
    /**
     * Tests the first constructor
     */
    public void testCompareTo() {
        Record rec = new Record(aBite);
        Record recToBeCompared = new Record(aBite);
        assertEquals(rec.compareTo(recToBeCompared), 0);
    }

}
