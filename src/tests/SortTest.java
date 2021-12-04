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
        testSortConfig("bigRandom", 8, 512, 8, RANDOM);
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


        Files.deleteIfExists(new File(fileName + "copy").toPath());
        copyContent(fileName, fileName + "copy", blocks);
        sorter.sort(fileName);

        checkSorting(fileName, blocks, blockRecords);
    }

    private void checkSorting(String fileName, int blocks, int blockRecords)
            throws IOException {

        RandomAccessFile raf = new RandomAccessFile(fileName, "rws");

        List<Record> r = new ArrayList<>();
        List<Record> org = new ArrayList<>();
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
            if(i + 1 != r.size() && r.get(i).getValue() > r.get(i + 1).getValue()){
                fail();
            }
        }

        /*
        RandomAccessFile raf2 = new RandomAccessFile(fileName + "copy", "rws");

        for(int i = 0; i < blocks; i++){
            ByteBuffer b = ByteBuffer.allocate(blockRecords * 16);
            raf2.read(b.array());

            while(b.hasRemaining()){
                byte[] t = new byte[16];
                b.get(t);
                org.add(new Record(t));
            }
        }

        raf2.close();

        int i = 0;
        for (Record entry : org) {
            if ( !r.contains(entry) ) {
                System.out.println(entry);
                i++;
            }
               // fail();
        }
        System.out.println("number of entries wrong... " + i);
        */
    }


    private void copyContent(String source, String destination, int blocks)
            throws IOException {

        FileInputStream fis = new FileInputStream(source);
        FileOutputStream fos = new FileOutputStream(destination);
        ByteBuffer inputBuffer = ByteBuffer.allocate(blocks * 16);

        inputBuffer.clear();
        while (fis.read(inputBuffer.array()) > -1) {
            fos.write(inputBuffer.array());
        }

        fis.close();
        fos.close();
        inputBuffer.clear();
    }

}