package externalsort;

/**
 * This class is used to parse the input file into blocks to be read into the
 * input buffer
 *
 * @author Muhammad Ali Qadri
 * @version 1
 */

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.Random;


public class Parser {
    private RandomAccessFile rafFile;
    private File file;

    public Parser(String filename) throws FileNotFoundException {
        try {
            file = new File(filename);
            rafFile = new RandomAccessFile(file, "rws");
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new FileNotFoundException();
        }
    }

    public void readRecords() throws IOException{
        //Read all records as a stream of bytes
        System.out.println("Length of file: " + rafFile.length() + " bytes.");
        for(int i = 0; i < rafFile.length(); i += 16)
            System.out.println(readRecord(rafFile, i, 16));
    }

    //Reading a Specific Record from the file
    public String readRecord(RandomAccessFile file, long pos, int recordSize)
            throws IOException{
        byte [] record = new byte [recordSize];
        file.seek(pos); //Adjust the filePointer
        int numBytes = file.read(record, 0, recordSize);
        System.out.println("Number of bytes read:" + numBytes);
        //Deserilize
        ByteBuffer bb = ByteBuffer.wrap(record);
        Long key = bb.getLong(); //Read 8 bytes
        Double data = bb.getDouble(); //read the next 8 bytes
        return key + "," + data;
    }

    //Reading a block of data from the file
    public byte [] readBlock(RandomAccessFile file, long pos, int blockSize)
            throws IOException{
        byte [] block = new byte [blockSize];
        file.seek(pos);
        int numBytes = file.read(block, 0, blockSize);
        System.out.println("Number of bytes read:" + numBytes);
        return block;

    }

    public void swapBlocks(RandomAccessFile file, long pos1, long pos2, int
            blockSize) throws IOException {
        file.seek(pos1);
        byte [] block1 = new byte [blockSize];
        file.read(block1, 0, blockSize);
        file.seek(pos2);
        byte[] block2 = new byte [blockSize];
        file.read(block2, 0, blockSize);

        //Do the Swap
        System.out.println("---------- Updated File ---------");
        file.seek(pos1);
        file.write(block2, 0, 16);
        file.seek(pos2);
        file.write(block1, 0, blockSize);
        readRecords();
    }
}
