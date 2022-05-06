import java.io.FileNotFoundException;
import java.io.IOException;
import student.TestCase;

/**
 * Test of external sort
 * 
 * @author Wenjun Han
 * @version 4.14.22
 */
public class ExternalsortTest extends TestCase {

    private FileReader reader;

    /**
     * set up for tests
     */
    public void setUp() {
        reader = new FileReader();
    }


    /**
     * Get code coverage of the class declaration.
     */
    public void testExternalsortInit() {
        Externalsort sorter = new Externalsort();
        assertNotNull(sorter);
    }


    /**
     * test short file <= 8 blocks
     * 
     * @throws IOException
     *             Throws if anything wrong
     */
    public void testShortFile() throws IOException {
        reader.readFile("sampleInput8.bin");
        assertTrue(contains(systemOut().getHistory(), "0.8732832848296571"));
    }


    /**
     * test longer file > 8 blocks, given test file
     * 
     * @throws IOException
     *             Throws if anything wrong
     */
    public void testLongerFile() throws IOException {
        reader.readFile("sampleInput16.bin");
        assertTrue(contains(systemOut().getHistory(), "7.25837957933813E-309"));
        assertTrue(contains(systemOut().getHistory(), "7.423511124391644E81"));
    }


    /**
     * test main of external sort
     */
    public void testMainInput() {
        try {
            Externalsort.main(new String[] { "sampleInput8.bin" });
            Externalsort.main(new String[] { "1", "2" });
            // non-existing file
            Externalsort.main(new String[] { "sample5.bin" });
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        assertTrue(contains(systemOut().getHistory(), "0.8732832848296571"));
    }

}
