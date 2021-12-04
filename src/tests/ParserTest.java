package tests;
import externalsort.Parser;
import externalsort.Record;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.junit.Assert.*;


public class ParserTest {
    Parser parser;

    //TODO: Complete parser test cases with full commenting
    /*
        Sets up parser with filename
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

    /*
    Used to test parser read records function
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
}
