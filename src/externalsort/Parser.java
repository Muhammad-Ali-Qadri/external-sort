package externalsort;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

/**
 * This class is used to parse the input file into blocks to be read into the
 * input buffer
 *
 * @author Muhammad Ali Qadri
 * @version 1
 */
public class Parser {
    private final RandomAccessFile rafFile;

    /**
     * Create parser on the input file
     *
     * @param filename the file from which data is read
     * @throws FileNotFoundException If the mentioned file does not exist
     */
    public Parser(String filename) throws FileNotFoundException {
        rafFile = new RandomAccessFile(new File(filename), "rws");
    }


    /**
     * Read data into a buffer.
     *
     * @param buffer the buffer to which bytes are written
     * @param pos    the starting position to get data from within file
     * @param length The length of bytes to write to buffer
     * @throws IOException if failed to read from file, or unexpected
     *                     parameters are provided
     */
    public int read(ByteBuffer buffer, long pos, int length)
            throws IOException {

        if (buffer == null || pos < 0) {
            throw new IllegalArgumentException();
        }

        rafFile.seek(pos);
        return rafFile.read(buffer.array(), 0, length);
    }


    /**
     * Closes the file
     *
     * @throws IOException if failed to read from file, or unexpected
     *                     parameters are provided
     */
    public void close() throws IOException {
        rafFile.close();
    }
}
