package tests;
import externalsort.Parser;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.junit.Assert.*;

/**
 * @author Cristian Diaz
 * @version 1.0
 */

public class ParserTest {
    private Parser parser;

    /**
     * Sets up parser with filename
     */
    @Before
    public void setUp() {
        try {
            parser = new Parser("bigRandom");
        }
        catch (FileNotFoundException e) {
            System.out.println("File did not exist.");
        }
    }

    /**
     * Used to test parser read records function
     */
    @Test
    public void test1() {
        ByteBuffer inputBuffer;
        inputBuffer = ByteBuffer.allocate(16 * 512);
        int index = 0;
        try {
            int val = parser.read(inputBuffer, 1, 16);
            assertEquals(-1, val);
        }
        catch (IOException e) {
            System.out.println("Invalid inputs");
        }
    }

    /**
     * Should catch an exception from an invalid index, and tests close
     */
    @Test
    public void test2() {
        ByteBuffer inputBuffer;
        inputBuffer = ByteBuffer.allocate(16 * 512);
        int index = 0;
        boolean checkMe = false;
        boolean checkMe2 = false;
        try {
            int val = parser.read(inputBuffer, -5, 16);
            assertEquals(16, val);
        }
        catch (IOException e) {
            System.out.println("Invalid inputs");
        }
        catch (IllegalArgumentException e) {
            checkMe = !checkMe;
            assertEquals(true, checkMe);
        }

        try {
            int val = parser.read(null, 0, 16);
            assertEquals(16, val);
        }
        catch (IOException e) {
            System.out.println("Invalid inputs");
        }
        catch (IllegalArgumentException e) {
            checkMe2 = !checkMe2;
            assertEquals(true, checkMe2);
        }

        try {
            parser.close();
        }
        catch (IOException e) {
            fail();
        }
    }
}
