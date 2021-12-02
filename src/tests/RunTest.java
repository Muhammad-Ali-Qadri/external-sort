package tests;

import externalsort.Run;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test Run class
 *
 * @author Muhammad Ali Qadri
 * @version 1
 */
public class RunTest {

    private Run run;

    /**
     * Initialize run class
     *
     * */
    @Before
    public void setUp() {
        run = new Run(0, 32);
    }

    /**
     * test get start position
     *
     * */
    @Test
    public void testGetStart(){
        assertEquals(0, run.getStart());
    }

    /**
     * test run get length
     *
     * */
    @Test
    public void testGetLength(){
        assertEquals(32, run.getRecords());
    }
}