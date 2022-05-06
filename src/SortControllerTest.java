import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import student.TestCase;

/**
 * Test of sort controller
 * 
 * @author Wenjun Han
 * @version 4.14.22
 */
public class SortControllerTest extends TestCase {

    private RandomAccessFile file;
    private SortController controller;

    /**
     * set up for tests
     * 
     * @throws IOException
     *             Throws if anything going wrong with I/O.
     */
    public void setUp() throws IOException {
        ByteBuffer inputBuffer = ByteBuffer.wrap(
            new byte[FileReader.BLOCK_SIZE]);
        ByteBuffer outputBuffer = ByteBuffer.wrap(
            new byte[FileReader.BLOCK_SIZE]);
        file = new RandomAccessFile("sampleInput16.bin", "rw");
        Record[] records = FileReader.buildRecords(0, file, FileReader.HEAP_SIZE
            / FileReader.RECORD_SIZE);
        controller = new SortController(records, inputBuffer, outputBuffer,
            file);
    }


    /**
     * test replacement selection & multi-way merge method
     * 
     * @throws IOException
     *             Throws if anything going wrong with I/O.
     */
    public void testReplacementSelectionAndMerge() throws IOException {
        RandomAccessFile runFile = controller.replacementSelection();
        assertEquals(runFile.length(), file.length());
        assertNotNull(runFile);
        controller.multiwayMerge(runFile);
        assertNotNull(file);
        assertTrue(file.getFilePointer() > 0);
    }
}
