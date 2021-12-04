package externalsort;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Main class to handle external sort functionality, buffers, replacement
 * selection and multiway merging.
 *
 * @author Muhammad Ali Qadri
 * @version 1
 */
public class Sort {

    private final static String RUN_FILE_NAME = "runFile";

    private String sortFileName;

    private Parser sortFile;
    private FileOutputStream outputFile;

    private List<Run> runs;

    private Heap<Record> heap;
    private ByteBuffer inputBuffer;
    private ByteBuffer outputBuffer;

    private final int recordSize;
    private final int recordsInBlock;
    private final int inputBufferBlocks;
    private final int outputBufferBlocks;
    private final int heapBlocks;

    private int runCounter;

    /**
     * Instantiate the sorting process based on defined environment parameters
     *
     * @param recSize     The size of the record in bytes in the file
     * @param recInBlk    The number of records considered in each block
     * @param inBuffSize  The size of the buffer in blocks that is read from
     *                    the file
     * @param outBuffSize The size of the output buffer in blocks, output
     *                    buffer is flushed to out file once filled
     * @param heapBlk     The size of the heap in blocks
     * @throws FileNotFoundException if provided file for sorting does not exist
     */
    public Sort(int recSize, int recInBlk, int inBuffSize,
                int outBuffSize,
                int heapBlk) throws FileNotFoundException {

        if (recSize < 1 || recInBlk < 1
            || inBuffSize < 1 || outBuffSize < 1) {
            throw new IllegalArgumentException();
        }

        recordSize = recSize;
        recordsInBlock = recInBlk;
        inputBufferBlocks = inBuffSize;
        outputBufferBlocks = outBuffSize;
        heapBlocks = heapBlk;
    }


    /**
     * Uses external sorting algorithm with replacement selection to sort the
     * file
     *
     * @param fileName The name of the file to be sorted
     * @throws IOException If the file throws an unexpected error
     */
    public void sort(String fileName) throws IOException {
        if (fileName.isEmpty()) {
            throw new IllegalArgumentException();
        }

        sortFileName = fileName;
        initializeMemory();

        //Position of pointer in the original file (for reading block into
        // input buffer)
        long seekPos = 0;
        Record lastPopped = null;

        //While not End of file
        while (sortFile.read(inputBuffer, seekPos,
                inputBuffer.capacity()) > 0) {

            //Read each record from block
            while (inputBuffer.hasRemaining()) {
                byte[] rec = new byte[recordSize];
                inputBuffer.get(rec);
                Record record = new Record(rec);

                //Heap is full and all elements are hidden
                if (heap.isFull() && heap.getSize() == 0) {
                    resetForRun(runs);
                    heap.recreate();
                    //clear last popped
                    lastPopped = null;

                }//If heap is full and there are visible elements

                if (heap.isFull() && heap.getSize() != 0) {
                    lastPopped = heap.pop();
                    putAndFlush(lastPopped);
                }

                //Heap is not full, compare with last element to output
                // buffer
                if (record.compareTo(lastPopped) > -1) {
                    //Just add to heap
                    heap.insert(record);
                } //Else Hide this element in the heap
                else {
                    heap.hideAtEnd(record);
                }
            }

            inputBuffer.clear();
            //Read next block
            seekPos += (long) recordSize * recordsInBlock;
        }

        if (heap.isEmpty() && (runs.isEmpty() || heap.hasHiddenElements())) {
            resetForRun(runs);
        }

        //When we reach EOF
        flushHeap();

        sortFile.close();
        outputFile.close();
        mergeRuns(heapBlocks);
    }

    private void initializeMemory() throws IOException {
        Files.deleteIfExists(new File(RUN_FILE_NAME).toPath());

        heap = new MinHeap<>(recordsInBlock * heapBlocks);
        inputBuffer =
                ByteBuffer.allocate(recordSize * recordsInBlock
                                    * inputBufferBlocks);
        outputBuffer =
                ByteBuffer.allocate(recordSize * recordsInBlock
                                    * outputBufferBlocks);
        runs = new ArrayList<>();

        sortFile = new Parser(sortFileName);

        outputFile = new FileOutputStream(RUN_FILE_NAME, true);
        runCounter = 0;
    }

    private void putAndFlush(Record out) throws IOException {
        outputBuffer.putLong(out.getKey());
        outputBuffer.putDouble(out.getValue());
        runCounter++;

        if (!outputBuffer.hasRemaining()) {
            outputFile.write(outputBuffer.array());
            outputFile.flush();

            outputBuffer.clear();
        }
    }

    /**
     * creates a new run, based on where the last one ended.
     */
    private void resetForRun(List<Run> run) {
        int start = run.isEmpty() ? 0 :
                run.get(run.size() - 1).getRecords() +
                run.get(run.size() - 1).getStart();

        run.add(new Run(start, runCounter));
        runCounter = 0;
    }

    /**
     * The current run won`t be cancelled when we reach EOF, all the
     * remaining visible elements in the heap will be added to output
     * buffer as a part of that run.
     * <p>
     * Once finished and we have hidden elements as well, we will start a
     * new run and add them to that run accordingly
     */
    private void flushHeap() throws IOException {
        boolean addedVisibleElements = false;
        boolean addedHiddenElements = false;

        while (!heap.isEmpty() || heap.hasHiddenElements()) {
            Record lastPopped;

            if (!heap.isEmpty()) {
                lastPopped = heap.pop();
                putAndFlush(lastPopped);
                addedVisibleElements = true;
            } //Heap is full and all elements are hidden
            else if (heap.hasHiddenElements()) {
                if (addedVisibleElements) {
                    resetForRun(runs);
                    addedVisibleElements = false;
                }
                heap.heapifyHiddenElements();
                addedHiddenElements = true;
            }
        }

        if (addedVisibleElements || addedHiddenElements) {
            resetForRun(runs);
        }
    }


    /**
     * Performs the merge on the sorted runs, over... and over again.
     *
     * @param blocksToMerge describes how many runs are being referenced.
     */
    private void mergeRuns(int blocksToMerge) throws IOException {

        boolean hasMerged = false;
        //Repeat until our file is a single run (indicates sorted file)
        while (runs.size() > 1) {
            hasMerged = true;
            sortFile = new Parser(RUN_FILE_NAME);
            runCounter = 0;

            //Remove contents of file to write in first
            clearFile(sortFileName);

            //Write back to original file
            outputFile = new FileOutputStream(sortFileName, true);

            //For each set of blocksToMerge number of runs
            int totalGroups =
                    (int)Math.ceil((double)runs.size() / blocksToMerge);

            //Complete runs on file
            runs = completeMergeRun(blocksToMerge, totalGroups);

            sortFile.close();
            outputFile.close();

            if (runs.size() > 1) {
                copyContent(sortFileName, RUN_FILE_NAME);
            }
        }

        //File already sorted - no need for merging - just paste result to
        // original file
        if (!hasMerged) {
            copyContent(RUN_FILE_NAME, sortFileName);
        }
    }

    private List<Run> completeMergeRun(int blocksToMerge, int totalGroups)
            throws IOException {

        List<Run> mergeRuns = new ArrayList<>();
        int isOddRuns = runs.size() % blocksToMerge;

        for (int runGroup = 0; runGroup < totalGroups; runGroup++) {
            int groupBlocksToInclude = blocksToMerge;
            //Done in case of runs that are not multiple of blocks to merge
            if(runGroup + 1 == totalGroups && isOddRuns > 0){
                groupBlocksToInclude = isOddRuns;
            }

            runCounter = 0;
            int[] totalGroupRecords =
                    getRunRecordCounts(runGroup, blocksToMerge,
                            groupBlocksToInclude);
            int[] completedRecords = new int[groupBlocksToInclude];

            //Load each run`s first blocks into the heap
            for (int run = 0; run < groupBlocksToInclude; run++) {
                int runIndex = (runGroup * blocksToMerge) + run;
                long start = (long) runs.get(runIndex).getStart() * recordSize;
                int length = runs.get(runIndex).getRecords() * recordSize;

                loadBlockToHeap(start,
                        Integer.min(length, inputBuffer.capacity()), run);
            }

            //Put values from heap into output buffer
            while (!heap.isEmpty()) {
                Record rec = heap.pop();
                int run = rec.getRunFlag();
                putAndFlush(rec);

                completedRecords[run]++;

                //Load next block of depleted run (if exist)
                if (completedRecords[run] % recordsInBlock == 0
                    && completedRecords[run] < totalGroupRecords[run]) {
                    int runIndex = (runGroup * blocksToMerge) + run;

                    int recordsLeft =
                            Integer.min((runs.get(runIndex).getRecords()
                                        - completedRecords[run]) * recordSize,
                                    inputBuffer.capacity());

                    long start = (long) (runs.get(runIndex).getStart()
                                            + completedRecords[run])
                                 * recordSize;

                    loadBlockToHeap(start, recordsLeft, run);
                }

                if (completedRecords[run] == totalGroupRecords[run]) {
                    inputBuffer.clear();
                }
            }

            //One run completed
            resetForRun(mergeRuns);
        }

        return mergeRuns;
    }


    private void clearFile(String fileName) throws IOException {
        new FileOutputStream(fileName).close();
    }


    private int[] getRunRecordCounts(int runGroup, int blocksToMerge,
                                     int groupBlocksToInclude) {
        int[] runBlockMap = new int[blocksToMerge];
        for (int i = 0; i < groupBlocksToInclude; i++) {
            int runPos = (runGroup * blocksToMerge) + i;
            runBlockMap[i] = runs.get(runPos).getRecords();
        }

        return runBlockMap;
    }

    private void loadBlockToHeap(long position, int length, int runFlag)
            throws IOException {
        inputBuffer.clear();
        sortFile.read(inputBuffer, position, length);
        byte[] rec = new byte[recordSize];

        while (length != 0) {
            inputBuffer.get(rec);
            heap.insert(new Record(rec, runFlag));
            length -= recordSize;
        }
    }


    private void copyContent(String source, String destination)
            throws IOException {

        FileInputStream fis = new FileInputStream(source);
        FileOutputStream fos = new FileOutputStream(destination);

        inputBuffer.clear();
        while (fis.read(inputBuffer.array()) > -1) {
            fos.write(inputBuffer.array());
        }

        fis.close();
        fos.close();
        inputBuffer.clear();
    }
}
