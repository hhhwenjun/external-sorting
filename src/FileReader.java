import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

/**
 * Reader for the bin file
 * 
 * @author Wenjun Han(hwenjun)
 * @version 3.24.22
 *
 */
public class FileReader {

    /** Block size **/
    public static final int BLOCK_SIZE = 8192;
    /** Record size **/
    public static final int RECORD_SIZE = 16;
    /** Heap size **/
    public static final int HEAP_SIZE = BLOCK_SIZE * 8;
    /** Number of records per heap **/
    public static final int HEAP_RECORD_NUM = HEAP_SIZE / RECORD_SIZE;

    /**
     * The file reader of the bin file
     * 
     * @param fileName
     *            Name of the file
     * @throws IOException
     *             Throw exception if anything wrong with I/O stream
     */
    public void readFile(String fileName) throws IOException {

        ByteBuffer inputBuffer = ByteBuffer.wrap(
            new byte[FileReader.BLOCK_SIZE]);
        ByteBuffer outputBuffer = ByteBuffer.wrap(
            new byte[FileReader.BLOCK_SIZE]);
        RandomAccessFile file = new RandomAccessFile(fileName, "rw");
        int numRecord = 0;
        numRecord = Math.min((int)file.length() / RECORD_SIZE, HEAP_SIZE
            / RECORD_SIZE);

        // build up records
        Record[] records = buildRecords(0, file, numRecord);
        if (file.length() <= HEAP_SIZE) {
            MinHeap<Record> minHeap = new MinHeap<>(records, numRecord,
                numRecord);
            file.seek(0);
            byte[] resultOutput = new byte[BLOCK_SIZE];
            while (minHeap.heapsize() > 0) {
                Record minRec = minHeap.removemin();
                outputBuffer.putLong(minRec.getId());
                outputBuffer.putDouble(minRec.getKey());
                if (!outputBuffer.hasRemaining()) {
                    outputBuffer.flip();
                    outputBuffer.get(resultOutput);
                    file.write(resultOutput);
                    outputBuffer.rewind();
                    outputBuffer.clear();
                }
            }
        }
        else {
            SortController controller = new SortController(records, inputBuffer,
                outputBuffer, file);
            RandomAccessFile sortedFile = controller.replacementSelection();
            controller.multiwayMerge(sortedFile);
        }
        SortController.printSortedRecord(file);
        file.close();
    }


    /**
     * Build up records in the Records array
     * 
     * @param pointer
     *            The pointer location
     * 
     * @param file
     *            The random access file
     * @param numRecord
     *            The number of records to build Records
     * @return A record array
     * @throws IOException
     *             Throws if anything wrong with I/O
     */
    public static Record[] buildRecords(
        int pointer,
        RandomAccessFile file,
        int numRecord)
        throws IOException {
        byte[] byteRecord = new byte[RECORD_SIZE];
        Record[] records = new Record[numRecord];
        for (int i = 0; i < numRecord; i++) {
            file.seek(pointer);
            byteRecord = new byte[RECORD_SIZE];
            // read the file to the byte record
            file.read(byteRecord, 0, RECORD_SIZE);
            pointer += RECORD_SIZE;
            records[i] = new Record(byteRecord);
        }
        return records;
    }

}
