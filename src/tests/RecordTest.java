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
    private Record recordRun;    //Initialized with key: 1, value: 2, run = 2

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
        recordRun = new Record(byteArray, 2);
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
        assertEquals(2, recordRun.getRunFlag());
    }


    /**
     * Check if byte array is equal to what was input to it
     */
    @Test
    public void testGetBytes() {
        assertTrue( Arrays.equals(byteArray, recordRun.getBytes() ) );
    }


    /**
     * Check if string representation returns correct key and value
     */
    @Test
    public void testToString() {
        assertEquals("1 2.0", recordRun.toString());
    }


    /**
     * Check if equal to another object
     */
    @Test
    public void testNotEqualAnotherObject() {
        assertNotEquals("1\t2.0", recordRun);
    }


    /**
     * Check if equals same
     */
    @Test
    public void testEqualsSame() {
        assertEquals(recordRun, recordRun);
    }


    /**
     * Check if equals another object different key
     */
    @Test
    public void testNotEqualDifferentKey() {
        byte[] arr = "\0\0\0\0\0\0\0\02@\0\0\0\0\0\0\0\0".
                getBytes(StandardCharsets.US_ASCII);
        assertNotEquals(recordRun, new Record(arr));
    }


    /**
     * Check if equals another object different value
     */
    @Test
    public void testNotEqualDifferentValue() {
        byte[] arr = "\0\0\0\0\0\0\0\01@\0\0\0\0\0\0\0\0".
                getBytes(StandardCharsets.US_ASCII);
        assertNotEquals(recordRun, new Record(arr));
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
        assertEquals(0, recordRun.compareTo(record));
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
        assertEquals(0, recordRun.compareTo(record));
    }

    /**
     * Tests hashcode of record class
     */
    @Test
    public void testHashCode() {
        int hashcode = record.hashCode();
        int hashcode2 = recordRun.hashCode();

        assertNotEquals(hashcode, hashcode2);
    }

    /**
     * Tests illegal record with run, and without run, and compareTo with null
     */
    @Test
    public void illegalRecords() {
        byte[] bytes = new byte[0];
        boolean test1 = false;
        boolean test2 = false;
        boolean test3 = false;
        boolean test4 = false;
        boolean test5 = false;
        try {
            Record rec = new Record(null);
        }
        catch (IllegalArgumentException e) {
            test1 = !test1;
            assertTrue(test1);
        }

        try {
            Record rec2 = new Record(bytes);
        }
        catch (IllegalArgumentException e) {
            test2 = !test2;
            assertTrue(test2);
        }

        try  {
            Record rec = new Record(null, 0);
        }
        catch (IllegalArgumentException e) {
            test3 = !test3;
            assertTrue(test3);
        }

        try {
            Record rec = new Record(bytes, 0);
        }
        catch (IllegalArgumentException e) {
            test4 = !test4;
            assertTrue(test4);
        }

        assertEquals(record.compareTo(null) , 0);
    }
}