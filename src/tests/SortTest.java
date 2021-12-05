package tests;

import externalsort.Record;
import externalsort.Sort;
import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test main sorting class
 *
 * @author Muhammad Ali Qadri
 * @version 1
 */
public class SortTest {

    private static final String SORTED = "SORTED";
    private static final String RANDOM = "RANDOM";
    private static final String REVERSE = "REVERSE";

    /**
     * test huge random file with 4096 blocks, 512 records in each block,
     * heap = 8
     * */
    @Test
    public void testBigRandomFile() throws IOException {
        boolean val = testSortConfig("bigRandom", 8, 512, 8, RANDOM);
        assertTrue(val);
    }

    /**
     * test huge sorted file with 4096 blocks, 512 records in each block,
     * heap = 8
     * */
    @Test
    public void testBigSortedFile() throws IOException {
        boolean val = testSortConfig("bigSorted", 16, 512, 8, SORTED);
        assertTrue(val);
    }


    /**
     * test huge reverse sorted file with 4096 blocks, 512 records in each
     * block,
     * heap = 8
     * */
    @Test
    public void testBigReverseFile() throws IOException {
        boolean val = testSortConfig("bigReverse", 16, 512, 8, REVERSE);
        assertTrue(val);
    }

    private boolean testSortConfig(String fileName, int blocks, int
            blockRecords, int heapSize, String fileType)
            throws IOException {

        Sort sorter;

        sorter = new Sort(16, blockRecords, 1, 1, heapSize);

        Files.deleteIfExists(new File(fileName).toPath());


        if (fileType.equals(SORTED)) {
            GenFile.sorted(new String[]{fileName, String.valueOf(blocks)});
        }
        else if (fileType.equals(RANDOM)) {
            GenFile.random(new String[]{fileName, String.valueOf(blocks)});
        }
        else {
            GenFile.reversed(new String[]{fileName, String.valueOf(blocks)});
        }


        Files.deleteIfExists(new File(fileName + "copy").toPath());
        copyContent(fileName, fileName + "copy", blocks);
        sorter.sort(fileName);

        return checkSorting(fileName, blocks, blockRecords);
    }

    private boolean checkSorting(String fileName, int blocks, int blockRecords)
            throws IOException {

        RandomAccessFile raf = new RandomAccessFile(fileName, "rws");

        List<Record> r = new ArrayList<>();
        List<Record> org = new ArrayList<>();
        for (int i = 0; i < blocks; i++) {
            ByteBuffer b = ByteBuffer.allocate(blockRecords * 16);
            raf.read(b.array());

            while (b.hasRemaining()) {
                byte[] t = new byte[16];
                b.get(t);
                r.add(new Record(t));
            }
        }

        raf.close();

        for (int i = 0; i < r.size(); i++) {
            if (i + 1 != r.size() &&
                    r.get(i).getValue() > r.get(i + 1).getValue()) {
                fail();
            }
        }
        return true;
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

    /**
     * Illegal sort creation arguments tests....
     */
    @Test
    public void testIllegalArgs() {
        Sort sort;
        Sort sort2;
        Sort sort3;
        Sort sort4;

        boolean test1 = false;
        boolean test2 = false;
        boolean test3 = false;
        boolean test4 = false;

        try {
            sort = new Sort(0, 512, 1, 1, 2);
        }
        catch (FileNotFoundException e) {
            fail();
        }
        catch (IllegalArgumentException e) {
            test1 = !test1;
            assertTrue(test1);
        }

        try {
            sort2 = new Sort(16, 0, 1, 1, 2);
        }
        catch (FileNotFoundException e) {
            fail();
        }
        catch (IllegalArgumentException e) {
            test2 = !test2;
            assertTrue(test2);
        }

        try {
            sort3 = new Sort(16, 512, 0, 1, 2);
        }
        catch (FileNotFoundException e) {
            fail();
        }
        catch (IllegalArgumentException e) {
            test3 = !test3;
            assertTrue(test3);
        }

        try {
            sort4 = new Sort(16, 512, 1, 0, 2);
        }
        catch (FileNotFoundException e) {
            fail();
        }
        catch (IllegalArgumentException e) {
            test4 = !test4;
            assertTrue(test4);
        }

        boolean test5 = false;
        Sort sorter;
        try {
            sorter = new Sort(16, 512, 1, 1, 2);
            sorter.sort("");
        }
        catch (IOException e) {
            fail();
        }
        catch (IllegalArgumentException e) {
            test5 = !test5;
            assertTrue(test5);
        }

    }

}