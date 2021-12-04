package tests;

import externalsort.Record;
import org.junit.Before;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;


/**
 * Test Record class
 *
 * @author Muhammad Ali Qadri
 * @version 1
 */
public class RecordTest {

    private Record record;     //Initialized with key: 1, value: 2
    private Record record_run;    //Initialized with key: 1, value: 2, run = 2

    //Represents key: 1, value: 2
    private final byte[] byteArray = "\0\0\0\0\0\0\0\01@\0\0\0\0\0\0\0".
            getBytes(StandardCharsets.US_ASCII);

    /**
     * Setup testing variables
     */
    @Before
    public void setUp() {
        double d = 2;
        ByteBuffer byteBuffer = ByteBuffer.allocate(Double.BYTES);
        byteBuffer.putDouble(d);
        byte[] ad = byteBuffer.array();
        record = new Record(byteArray);
        record_run = new Record(byteArray, 2);
    }


    /**
     * Check if key equal to 1
     */
    @Test
    public void testGetKey() {
        assertEquals(1, record.getKey());
    }


    /**
     * Check if key equal to 1
     */
    @Test
    public void testGetValue() {
        assertEquals(2.0, record.getValue(), 0.02);
    }


    /**
     * Check if run flag is 2
     */
    @Test
    public void testGetFlag() {
        assertEquals(2, record_run.getRunFlag());
    }


    /**
     * Check if byte array is equal to what was input to it
     */
    @Test
    public void testGetBytes() {
        assertTrue( Arrays.equals(byteArray, record_run.getBytes() ) );
    }


    /**
     * Check if string representation returns correct key and value
     */
    @Test
    public void testToString() {
        assertEquals("1 2.0", record_run.toString());
    }


    /**
     * Check if equal to another object
     */
    @Test
    public void testNotEqualAnotherObject() {
        assertNotEquals("1\t2.0", record_run);
    }


    /**
     * Check if equals same
     */
    @Test
    public void testEqualsSame() {
        assertEquals(record_run, record_run);
    }


    /**
     * Check if equals another object different key
     */
    @Test
    public void testNotEqualDifferentKey() {
        byte[] arr = "\0\0\0\0\0\0\0\02@\0\0\0\0\0\0\0\0".
                getBytes(StandardCharsets.US_ASCII);
        assertNotEquals(record_run, new Record(arr));
    }


    /**
     * Check if equals another object different value
     */
    @Test
    public void testNotEqualDifferentValue() {
        byte[] arr = "\0\0\0\0\0\0\0\01@\0\0\0\0\0\0\0\0".
                getBytes(StandardCharsets.US_ASCII);
        assertNotEquals(record_run, new Record(arr));
    }


    /**
     * Check if equals another object same value
     */
    @Test
    public void testEqualDiffObjectSameValue() {
        assertEquals(record, new Record(byteArray));
    }


    /**
     * Check if another Record compares to this
     */
    @Test
    public void testCompareTo() {
        byte[] arr = "\0\0\0\0\0\0\0\02@\0\0\0\0\0\0\0\0".
                getBytes(StandardCharsets.US_ASCII);
        Record big = new Record(arr);

        assertEquals(0, record.compareTo(big));
        assertEquals(0, big.compareTo(record));
        assertEquals(0, record_run.compareTo(record));
    }

    /**
     * Checks greater key, less, and equal
     */
    @Test
    public void testCompareTo2() {
        byte[] arr = "\0\0\0\0\0\0\0\5@\10\0\0\0\0\0\0\25".
                getBytes(StandardCharsets.US_ASCII);
        Record big = new Record(arr);

        assertEquals(-1, record.compareTo(big));
        assertEquals(1, big.compareTo(record));
        assertEquals(0, record_run.compareTo(record));
    }
}