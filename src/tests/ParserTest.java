package tests;
import externalsort.Parser;
import externalsort.Record;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;


public class ParserTest {
    Parser parser;

    //TODO: Complete parser test cases with full commenting
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
        ByteBuffer inputBuffer;
        inputBuffer = ByteBuffer.allocate(16 * 512);
        int index = 0;
        try {
            while (parser.read(inputBuffer, index, inputBuffer.limit()) > 0) {
                byte[] rec = new byte[16];
                inputBuffer.get(rec, 0, 16);
                Record record = new Record(rec);
                System.out.println("Record key is.. " + record.getKey() );
                System.out.println("Record value is... " + record.getValue() );
                index +=16;
                System.out.println("Index is: "+ index);

                if (!inputBuffer.hasRemaining() ) {
                    inputBuffer.clear();
                }
            }
        }
        catch (IOException e) {
            System.out.println("IO error");
        }
    }
}
