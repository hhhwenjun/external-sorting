import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * The external sort controller for the project input and output
 * 
 * @author Wenjun Han(hwenjun)
 * @version 4.13.22
 *
 */
public class SortController {
    private Record[] records;
    private MinHeap<Record> minHeap;
    private ByteBuffer inputBuffer;
    private ByteBuffer outputBuffer;
    private RandomAccessFile file;
    private LinkedList<RunTracker> run;

    /**
     * The constructor of the sort controller
     * 
     * @param records
     *            The input records
     * @param inputBuffer
     *            The input buffer
     * @param outputBuffer
     *            The output buffer
     * @param file
     *            The file
     */
    public SortController(
        Record[] records,
        ByteBuffer inputBuffer,
        ByteBuffer outputBuffer,
        RandomAccessFile file) {
        this.records = records;
        minHeap = new MinHeap<>(records, records.length, records.length);
        this.inputBuffer = inputBuffer;
        this.outputBuffer = outputBuffer;
        this.file = file;
        run = new LinkedList<>();
    }


    /**
     * The replacement selection
     * 
     * @return runFile
     *         The runFile that temporarily store sort data
     * @throws IOException
     *             Throws if any wrong with I/O
     */
    public RandomAccessFile replacementSelection() throws IOException {
        Files.deleteIfExists(Paths.get("runFile.bin"));
        // initialization for run file and parameters
        RandomAccessFile runFile = new RandomAccessFile("runFile.bin", "rw");
        long fileLength = file.length();
        byte[] output = new byte[FileReader.BLOCK_SIZE];
        byte[] input = new byte[FileReader.BLOCK_SIZE];
        byte[] tempInput = new byte[FileReader.RECORD_SIZE];
        byte[] tempOutput = new byte[FileReader.RECORD_SIZE];
        // prepare the first buffer
        file.seek(FileReader.HEAP_SIZE);
        file.read(input);
        inputBuffer = ByteBuffer.wrap(input);
        input = new byte[FileReader.BLOCK_SIZE];

        RunTracker runtracker = new RunTracker(0, 0);
        int endHeapRecord = 0;
        long runStart = 0;
        long runLength = 0;
        while (minHeap.heapsize() > 0) {
            // for each run
            while (minHeap.heapsize() > 0) {
                // check if output buffer full
                if (!outputBuffer.hasRemaining()) {
                    // write the content to the file
                    outputBuffer.flip();
                    outputBuffer.get(output);
                    runFile.write(output);
                    // clear the buffer and set position to 0
                    output = new byte[FileReader.BLOCK_SIZE];
                    outputBuffer.rewind();
                    outputBuffer.clear();
                }
                // check if input buffer empty, read new input buffer
                if (!inputBuffer.hasRemaining() && file
                    .getFilePointer() < fileLength) {
                    file.read(input);
                    inputBuffer = ByteBuffer.wrap(input);
                }
                if (inputBuffer.hasRemaining()) {
                    // read the data into the heap
                    runLength += FileReader.RECORD_SIZE;

                    inputBuffer.get(tempInput);
                    Record newInput = new Record(tempInput);
                    tempInput = new byte[FileReader.RECORD_SIZE];
                    Record newOutput = minHeap.getHeap()[0];
                    outputBuffer.putLong(newOutput.getId());
                    outputBuffer.putDouble(newOutput.getKey());

                    if (newInput.compareTo(newOutput) > 0) {
                        // put the data into heap
                        minHeap.getHeap()[0] = newInput;
                        minHeap.siftdown(0);
                    }
                    else {
                        // put the data into back of the heap
                        if (minHeap.heapsize() > 0) {
                            int currentHeapSize = minHeap.heapsize();
                            minHeap.setHeapsize(--currentHeapSize);
                            Record temp = minHeap.getHeap()[currentHeapSize];
                            minHeap.getHeap()[currentHeapSize] = minHeap
                                .getHeap()[0];
                            minHeap.getHeap()[0] = temp;
                            minHeap.getHeap()[currentHeapSize] = newInput;
                            minHeap.siftdown(0);
                        }
                        endHeapRecord++;
                    }
                }
                else {
                    runLength += FileReader.RECORD_SIZE;
                    Record singleOutput = minHeap.removemin();
                    outputBuffer.putLong(singleOutput.getId());
                    outputBuffer.putDouble(singleOutput.getKey());
                }

            }
            // re-construct the min heap
            if (endHeapRecord > 0) {
                int heapLength = minHeap.getHeap().length;
                for (int i = heapLength - endHeapRecord, j =
                    0; i < heapLength; i++, j++) {
                    minHeap.getHeap()[j] = minHeap.getHeap()[i];
                }
                minHeap.setHeapsize(endHeapRecord);
                minHeap.buildheap();
            }
            // record a new run
            runtracker = new RunTracker(runStart, runLength);

            int outputIdx = outputBuffer.position();
            outputBuffer.flip();
            for (int i = 0; i < outputIdx; i += 16) {
                outputBuffer.position(i);
                outputBuffer.get(tempOutput);
                runFile.write(tempOutput);
            }
            outputBuffer.rewind();
            outputBuffer.clear();

            run.append(runtracker);
            runStart += runLength;
            runLength = 0;
            endHeapRecord = 0;
        }
        return runFile;
    }


    /**
     * Multi-way merge sorted file
     * 
     * @param runFile
     *            The run file with blocks of sorted data
     * @throws IOException
     */
    public void multiwayMerge(RandomAccessFile runFile) throws IOException {
        // delete if the merge file exists
        Files.deleteIfExists(Paths.get("mergeTempFile.bin"));
        RandomAccessFile mergeTempFile = new RandomAccessFile(
            "mergeTempFile.bin", "rw");
        boolean finalRunComplete = false;
        LinkedList<RunTracker> mergeRun = run;
        if (mergeRun.length() == 1) {
            mergeRun = mergeHelper(runFile, mergeTempFile, mergeRun);
        }
        // merge to file
        while (!finalRunComplete) {
            if (mergeRun.length() == 1) {
                // cover the file as result
                transferDataToFile(mergeTempFile, file);
                mergeTempFile.close();
                runFile.close();
                finalRunComplete = true;
                Files.deleteIfExists(Paths.get("runFile.bin"));
                Files.deleteIfExists(Paths.get("mergeTempFile.bin"));

            }
            else {
                // merge data from file
                mergeRun = mergeHelper(runFile, mergeTempFile, mergeRun);
                transferDataToFile(mergeTempFile, runFile);
                // repeat the process of merging
                mergeTempFile.seek(0);
                runFile.seek(0);
            }
        }
    }


    /**
     * Transfer data from one file to another file
     * 
     * @param sourceFile
     *            The data source file
     * @param targetFile
     *            The data target destination
     * @throws IOException
     *             Throws when anything wrong with I/O
     */
    private void transferDataToFile(
        RandomAccessFile sourceFile,
        RandomAccessFile targetFile)
        throws IOException {
        sourceFile.seek(0);
        targetFile.seek(0);
        byte[] dataBlock = new byte[FileReader.BLOCK_SIZE];
        while (sourceFile.getFilePointer() < sourceFile.length()) {
            sourceFile.read(dataBlock);
            targetFile.write(dataBlock);
        }
    }


    /**
     * Helper method of merging multi-runs data
     * 
     * @param runFile
     *            The run file
     * @param mergeTempFile
     *            A temp file to store merging data
     * @return A list of merging information
     * @throws IOException
     *             Throws when anything wrong with I/O
     */
    private LinkedList<RunTracker> mergeHelper(
        RandomAccessFile runFile,
        RandomAccessFile mergeTempFile,
        LinkedList<RunTracker> mergeRun)
        throws IOException {

        LinkedList<RunTracker> mergeInfo = new LinkedList<>();
        // record the block index
        int[] recordEndIndex = new int[8];
        int blockRecordNum = FileReader.BLOCK_SIZE / FileReader.RECORD_SIZE;
        ByteBuffer mergeOutputBuffer = ByteBuffer.wrap(
            new byte[FileReader.BLOCK_SIZE]);
        mergeOutputBuffer.rewind();
        mergeOutputBuffer.clear();
        byte[] output = new byte[FileReader.BLOCK_SIZE];
        byte[] tempOutput = new byte[FileReader.RECORD_SIZE];
        long runStart = 0;
        long runLength = 0;

        while (mergeRun.length() > 0) {
            int runNum = readBlocks(runFile, recordEndIndex, mergeRun);
            int runFinishNum = 0;
            int minRecordRunNum = 0;
            // finish reading blocks
            boolean[] runReadFinish = new boolean[runNum];
            int[] runRecord = new int[runNum];
            for (int i = 0; i < runNum; i++) {
                runRecord[i] = blockRecordNum * i;
            }
            // while not all the runs are completed
            while (runFinishNum < runNum) {

                // output to file if buffer is full
                if (!mergeOutputBuffer.hasRemaining()) {
                    mergeOutputBuffer.flip();
                    mergeOutputBuffer.get(output);
                    mergeTempFile.write(output);
                    // clear the buffer and set position to 0
                    output = new byte[FileReader.BLOCK_SIZE];
                    mergeOutputBuffer.rewind();
                    mergeOutputBuffer.clear();
                }
                // compare all block record find minimum record
                // create a largest record for comparison
                runLength += FileReader.RECORD_SIZE;
                ByteBuffer minBuffer = ByteBuffer.wrap(
                    new byte[FileReader.RECORD_SIZE]);
                minBuffer.putLong(0, 100);
                minBuffer.putDouble(FileReader.RECORD_SIZE / 2,
                    Double.POSITIVE_INFINITY);
                minBuffer.rewind();
                Record minRecord = new Record(minBuffer.array());
                // loop through the run's block to compare
                for (int i = 0; i < runRecord.length; i++) {
                    if (runReadFinish[i]) {
                        continue;
                    }
                    Record currRecord = records[runRecord[i]];
                    if (currRecord.compareTo(minRecord) < 0) {
                        minRecord = currRecord;
                        minRecordRunNum = i;
                    }
                }
                // output the min record to buffer
                mergeOutputBuffer.putLong(minRecord.getId());
                mergeOutputBuffer.putDouble(minRecord.getKey());

                // when finish the comparison of the block
                if (runRecord[minRecordRunNum] == 
                    recordEndIndex[minRecordRunNum]) {
                    mergeRun.moveToPos(minRecordRunNum);
                    RunTracker minRun = mergeRun.getValue();
                    // read next block if data still available
                    if (minRun.getLength() > 0) {
                        readSingleBlock(runFile, recordEndIndex,
                            minRecordRunNum, mergeRun);
                        runRecord[minRecordRunNum] = minRecordRunNum
                            * blockRecordNum;
                    }
                    else {
                        runReadFinish[minRecordRunNum] = true;
                        runFinishNum++;
                    }
                }
                else {
                    runRecord[minRecordRunNum]++;
                }
            }
            // output rest of the data from buffer to file
            int outputIdx = mergeOutputBuffer.position();
            mergeOutputBuffer.flip();
            for (int i = 0; i < outputIdx; i += 16) {
                mergeOutputBuffer.position(i);
                mergeOutputBuffer.get(tempOutput);
                mergeTempFile.write(tempOutput);
                tempOutput = new byte[FileReader.RECORD_SIZE];
            }
            mergeOutputBuffer.rewind();
            mergeOutputBuffer.clear();
            // remove completed run information from run list
            int countRemoved = 0;
            mergeRun.moveToStart();
            while (countRemoved < runNum) {
                mergeRun.remove();
                countRemoved++;
            }

            // update the merged run information for future merge
            mergeInfo.append(new RunTracker(runStart, runLength));
            runStart += runLength;
            runLength = 0;
        }
        return mergeInfo;
    }


    /**
     * Read blocks of records from runFile for merging data
     * 
     * @param runFile
     *            The created run file
     * @param recordEndIndex
     *            The end index of the 8 blocks
     * @return The run index
     * @throws IOException
     *             Throws when anything wrong with I/O
     */
    private int readBlocks(
        RandomAccessFile runFile,
        int[] recordEndIndex,
        LinkedList<RunTracker> runList)
        throws IOException {
        int index;
        int runSize = Math.min(runList.length(), 8);
        // Maximally we can put 8 blocks of data into the heap
        for (index = 0; index < runSize; index++) {
            readSingleBlock(runFile, recordEndIndex, index, runList);
        }
        return index;
    }


    /**
     * A helper method for readBlocks
     * 
     * @param runFile
     *            The run file
     * @param recordEndIndex
     *            The end index of the 8 blocks
     * @param runIdx
     *            The run index
     * @throws IOException
     *             Throws when anything wrong with I/O
     */
    private void readSingleBlock(
        RandomAccessFile runFile,
        int[] recordEndIndex,
        int runIdx,
        LinkedList<RunTracker> runList)
        throws IOException {
        runList.moveToPos(runIdx);
        RunTracker currentRun = runList.getValue();
        int blockRecordNum = FileReader.BLOCK_SIZE / FileReader.RECORD_SIZE;
        // read in by block
        for (int j = runIdx * blockRecordNum; j < (runIdx + 1)
            * blockRecordNum; j++) {
            // load the records to Record memory
            if (currentRun.getLength() == 0) {
                break;
            }
            long runFilePos = currentRun.getStart();
            byte[] runSingleRecord = new byte[FileReader.RECORD_SIZE];
            runFile.seek(runFilePos);
            runFile.read(runSingleRecord);
            Record newRec = new Record(runSingleRecord);
            records[j] = newRec;
            recordEndIndex[runIdx] = j;
            // modify run information
            currentRun.setStart(currentRun.getStart() + FileReader.RECORD_SIZE);
            currentRun.setLength(currentRun.getLength()
                - FileReader.RECORD_SIZE);
        }
    }


    /**
     * Print out some of the records to console to check sort results
     * 
     * @param finalFile
     *            The sorted final file
     * @throws IOException
     */
    public static void printSortedRecord(RandomAccessFile finalFile)
        throws IOException {
        finalFile.seek(0);
        int lineCount = 0;
        for (long pointer = 0; pointer < finalFile.length(); pointer +=
            FileReader.BLOCK_SIZE) {
            if (lineCount % 5 == 0 && lineCount != 0) {
                System.out.println();
            }
            else if (pointer > 0) {
                System.out.print(" ");
            }
            lineCount++;
            byte[] record = new byte[FileReader.RECORD_SIZE];
            finalFile.seek(pointer);
            finalFile.read(record, 0, FileReader.RECORD_SIZE);
            Record data = new Record(record);
            System.out.print(data.toString());
        }
    }

}
