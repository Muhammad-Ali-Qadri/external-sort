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

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is the main entry point for the external sort functionality.
 *
 * @author Muhammad Ali Qadri
 * @version 1
 */
public class Externalsort {

    /**
     * @param args contains file to be sorted
     * @throws IOException in the even the file couldn't be closed.
     */
    public static void main(String[] args) throws IOException {
        if (args[0] == null) {
            System.out.println("File expected");
            return;
        }

        int blocks = 512;
        int recordSize = 16;

        Sort sorter = new Sort(recordSize, blocks, 1, 1, 8);
        sorter.sort(args[0]);

        RandomAccessFile raf = new RandomAccessFile(args[0], "rws");

        ByteBuffer b = ByteBuffer.allocate(recordSize);
        List<Record> r = new ArrayList<>();
        int seekPos = 0;
        raf.seek(seekPos);
        while (raf.read(b.array(), 0, 16) > 0)   {
            while (b.hasRemaining()) {
                byte[] t = new byte[16];
                b.get(t);
                Record temp = new Record(t);
                r.add(temp);
            }
            b.clear();
            seekPos += (long) recordSize * blocks;
            raf.seek(seekPos);
        }

        raf.close();
        for (int i = 0 ; i < r.size(); i++) {
            if (i % 5 == 0 && i != 0) {
                System.out.println("");
            }
            String printMe = r.get(i).toString() + "\t\t";
            System.out.print(printMe);
        }

        raf.close();


    }
}
