package tests;

import externalsort.Record;
import externalsort.Sort;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test main sorting class
 *
 * @author Muhammad Ali Qadri
 * @version 1
 */
public class SortTest {

    private Sort sorter;

    private static final String SORTED = "SORTED";
    private static final String RANDOM = "RANDOM";
    private static final String REVERSE = "REVERSE";

    /**
     * test huge random file with 4096 blocks, 512 records in each block,
     * heap = 8
     * */
    @Test
    public void testBigRandomFile() throws IOException {

        testSortConfig("bigRandom", 16, 512, 8, RANDOM);
    }

    /**
     * test huge sorted file with 4096 blocks, 512 records in each block,
     * heap = 8
     * */
    @Test
    public void testBigSortedFile() throws IOException {
        testSortConfig("bigSorted", 16, 512, 8, SORTED);
    }


    /**
     * test huge reverse sorted file with 4096 blocks, 512 records in each
     * block,
     * heap = 8
     * */
    @Test
    public void testBigReverseFile() throws IOException {
        testSortConfig("bigReverse", 16, 512, 8, REVERSE);
    }

    private void testSortConfig(String fileName, int blocks, int blockRecords,
                                int heapSize, String fileType)
            throws IOException{

        sorter = new Sort(16, blockRecords, 1, 1, heapSize);

        Files.deleteIfExists(new File(fileName).toPath());

        if(fileType.equals(SORTED)){
            GenFile.sorted(new String[]{fileName, String.valueOf(blocks)});
        }
        else if(fileType.equals(RANDOM)){
            GenFile.random(new String[]{fileName, String.valueOf(blocks)});
        }
        else {
            GenFile.reversed(new String[]{fileName, String.valueOf(blocks)});
        }

        sorter.sort(fileName);

        checkSorting(fileName, blocks, blockRecords);
    }

    private void checkSorting(String fileName, int blocks, int blockRecords)
            throws IOException {

        RandomAccessFile raf = new RandomAccessFile(fileName, "rws");

        List<Record> r = new ArrayList<>();
        for(int i = 0; i < blocks; i++){
            ByteBuffer b = ByteBuffer.allocate(blockRecords * 16);
            raf.read(b.array());

            while(b.hasRemaining()){
                byte[] t = new byte[16];
                b.get(t);
                r.add(new Record(t));
            }
        }

        raf.close();

        for(int i = 0; i < r.size(); i++){
            if(i + 1 != r.size() && r.get(i).getKey() > r.get(i + 1).getKey()){
                fail();
            }
        }
    }
}