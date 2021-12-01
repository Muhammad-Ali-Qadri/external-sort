package tests;
import externalsort.Parser;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;


public class ParserTests {
    Parser parser;

    /*
        Sets up parser with filename
     */
    @Before
    public void setUp() {
        try {
            parser = new Parser("./Data/sampleInput16.bin");
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
        try {
            parser.readRecords();
        }
        catch(IOException e) {
            System.out.println("IOException during reading records.");
        }
    }
}