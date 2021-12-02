package externalsort;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
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

    private final static String RUN_FILE_NAME = "runFile.bin";

    private final String sortFileName;

    private Parser sortFile;
    private FileOutputStream outputFile;

    private final List<Run> runs;

    private final MinHeap<Record> heap;
    private final ByteBuffer inputBuffer;
    private final ByteBuffer outputBuffer;

    private final int recordSize;
    private final int recordsInBlock;
    private final int inputBufferBlocks;
    private final int outputBufferBlocks;
    private final int heapBlocks;
    private int runCounter;
    /**
     * Instantiate the sorting process based on defined environment parameters
     *
     * @param fileName    The name of the file to be sorted
     * @param recSize     The size of the record in bytes in the file
     * @param recInBlk    The number of records considered in each block
     * @param inBuffSize  The size of the buffer in blocks that is read from
     *                    the file
     * @param outBuffSize The size of the output buffer in blocks, output
     *                    buffer is flushed to out file once filled
     * @param heapBlk     The size of the heap in blocks
     * @throws FileNotFoundException if provided file for sorting does not exist
     */
    public Sort(String fileName, int recSize, int recInBlk, int inBuffSize,
                int outBuffSize,
                int heapBlk) throws FileNotFoundException {

        if (fileName.isEmpty() || recSize < 1 || recInBlk < 1
            || inBuffSize < 1 || outBuffSize < 1) {
            throw new IllegalArgumentException();
        }

        sortFileName = fileName;
        recordSize = recSize;
        recordsInBlock = recInBlk;
        inputBufferBlocks = inBuffSize;
        outputBufferBlocks = outBuffSize;
        heapBlocks = heapBlk;

        heap = new MinHeap<>(recordSize * recordsInBlock * heapBlocks);
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


    /**
     * Uses external sorting algorithm with replacement selection to sort the
     * file
     *
     * @throws IOException If the file throws an unexpected error
     * */
    public void sort() throws IOException {
        long seekPos = 0;

        //While not End of file
        while(sortFile.read(inputBuffer, seekPos) > 0){
            byte[] rec = new byte[recordSize];
            Record lastPopped = null;
            //int recPos = 0; I dont think we need this, .get method of input buffer offsets the array, not the buffer.

            //Read each record from block
            while(inputBuffer.hasRemaining() ){
                inputBuffer.get(rec, 0, recordSize);
                Record record = new Record(rec);

                //If heap is full and there are visible elements
                if(heap.isFull() && heap.getSize() != 0){
                    lastPopped = heap.pop();
                    putAndFlush(lastPopped);
                } //Heap is full and all elements are hidden
                else if(heap.isFull() && heap.getSize() == 0){
                    //TODO: End run and reset
                    resetForRun();
                }

                //Heap is not full, compare with last element to output
                // buffer
                if(record.compareTo(lastPopped) > -1){
                    //Just add to heap
                    heap.insert(record);
                } //Hide this element in the heap
                else{
                    heap.hideAtEnd(record);
                }

                //recPos += recordSize;
            }

            //Read next block
            seekPos += (long) recordSize * recordsInBlock;
        }

        //TODO: When reached end of file (EOF)
        /* should move hidden elements, if they exist to front.
        * Then will pop the rest of the heap into the output buffer.
        * AT 1 hr 13 of the video... wouldn't it get stuck here?
        * 6 less than 9, but would go in the next slot so the output buffer wouldn't be sorted?
        * */
        heap.heapifyHiddenElements();
        while (!heap.isEmpty() ) {
            Record lastPopped = null;
            if(heap.isFull() && heap.getSize() != 0){
                lastPopped = heap.pop();
                putAndFlush(lastPopped);
            } //Heap is full and all elements are hidden
            else if(heap.isFull() && heap.getSize() == 0){
                resetForRun();
            }
        }

        multiwayMerge(8);
    }

    private void putAndFlush(Record out) throws IOException{
        outputBuffer.putLong(out.getKey());
        outputBuffer.putDouble(out.getKey());
        runCounter++;

        if(!outputBuffer.hasRemaining()){
            outputFile.write(outputBuffer.array());
            outputFile.flush();

            outputBuffer.clear();
            inputBuffer.clear();
        }
    }

    /**
     * creates a new run, based on where the last one ended.
     */
    private void resetForRun() {
        int start = runs.size() == 0 ? 0 :
                runs.get(runs.size() - 1).getLength() + 1 +
                        runs.get(runs.size() - 1).getStart() ;
        Run run = new Run(start, runCounter);
        runs.add(run);
        runCounter = 0;
        heap.recreate();
    }

    /**
     * Performs the merge on the sorted runs, over... and over again.
     * @param number describes how many runs are being referenced.
     */
    public void multiwayMerge(int number) {

        for(int i = 0; i < runs.size()/number; i++) {
            return;
        }
    }

}
