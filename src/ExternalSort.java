
/**
 * This class is the main entry point for the external sort functionality.
 *
 * @author Muhammad Ali Qadri
 * @version 1
 */
import externalsort.Parser;

import java.io.FileNotFoundException;

public class ExternalSort {
    public static void main(String[] args) {
        if (args[0] == null) {
            System.out.println("File expected");
            return;
        }

        try {
            Parser parser = new Parser(args[0]);
        }

        catch (FileNotFoundException e) {
            System.out.println("Invalid file: " + args[0]);
        }
    }
}
