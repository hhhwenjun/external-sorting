import student.TestCase;

/**
 * test case of run tracker
 * 
 * @author Wenjun Han
 * @version 4.14.22s
 *
 */
public class RunTrackerTest extends TestCase {

    private RunTracker runtracker;

    /**
     * set up
     */
    public void setUp() {
        runtracker = new RunTracker(10, 20);
    }


    /**
     * test getters in run tracker
     */
    public void testGetter() {
        assertEquals(runtracker.getStart(), 10);
        assertEquals(runtracker.getLength(), 20);
        assertEquals(runtracker.getEnd(), 30);
    }


    /**
     * test setters in run tracker
     */
    public void testSetter() {
        runtracker.setLength(50);
        runtracker.setStart(0);
        assertEquals(runtracker.getStart(), 0);
        assertEquals(runtracker.getLength(), 50);
    }
}
