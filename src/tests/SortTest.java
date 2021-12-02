package tests;

import externalsort.Sort;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
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

    /**
     * initialize sort class and files
     * */
    @Before
    public void setUp() throws FileNotFoundException {
        sorter = new Sort( 16, 2, 1, 1, 2);
    }


    /**
     * test small random file with
     * */
    @Test
    public void testSmallRandomFile() throws IOException {
        GenFile.random(new String[]{"smallRandom", "6"});
        sorter.sort("smallRandom");

        checkSorting("smallRandom");
    }

    private void checkSorting(String fileName) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(fileName, "rws");

        List<Long> ids = new ArrayList<>();

        for(int i = 0; i < 12; i++){
            ids.add(raf.readLong());
            raf.readDouble();
        }

        raf.close();

        for(int i = 0; i < ids.size(); i++){
            if(i + 1 != ids.size() && ids.get(i) > ids.get(i + 1)){
                fail();
            }
        }
    }
}