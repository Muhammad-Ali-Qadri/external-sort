// On my honor:
//
// - I have not used source code obtained from another student,
// or any other unauthorized source, either modified or
// unmodified.
//
// - All source code and documentation used in my program is
// either my original work, or was derived by me from the
// source code published in the textbook for this course.
//
// - I have not discussed coding details about this project with
// anyone other than my partner (in the case of a joint
// submission), instructor, ACM/UPE tutors or the TAs assigned
// to this course. I understand that I may discuss the concepts
// of this program with other students, and that another student
// may help me debug my program so long as neither of us writes
// anything during the discussion or modifies any computer file
// during the discussion. I have violated neither the spirit nor
// letter of this restriction.

import externalsort.Record;
import externalsort.Sort;
import tests.GenFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.fail;

/**
 * This class is the main entry point for the external sort functionality.
 *
 * @author Muhammad Ali Qadri
 * @version 1
 */
public class ExternalSort {
    public static void main(String[] args) {
        if (args[0] == null) {
            System.out.println("File expected");
            return;
        }

        int blocks = 16;

        try {
            GenFile.random(new String[]{args[0], String.valueOf(blocks)});
            Sort sorter = new Sort(16, 512, 1, 1, 8);
            sorter.sort(args[0]);

            RandomAccessFile raf = new RandomAccessFile(args[0], "rws");

            List<Record> r = new ArrayList<>();
            for(int i = 0; i < blocks; i++){
                raf.seek(16 * blocks * i);
                ByteBuffer b = ByteBuffer.allocate(16);

                raf.read(b.array());

                while(b.hasRemaining()){
                    byte[] t = new byte[16];
                    b.get(t);
                    r.add(new Record(t));
                }
            }

            raf.close();

            for (Record record : r) {
                System.out.println(record);
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("Invalid file: " + args[0]);
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }
}
