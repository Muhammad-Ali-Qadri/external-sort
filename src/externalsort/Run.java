package externalsort;


/**
 * Stores the specifications for each run
 *
 * @author Muhammad Ali Qadri
 * @version 1
 */
public class Run {
    private final int start;
    private final int records;

    /**
     * Initializes a run
     *
     * @param len length of run
     * @param st the starting byte of the run
     * */
    public Run(int st, int len) {
        if (len == 0) {
            throw new IllegalArgumentException();
        }

        start = st;
        records = len;
    }

    /**
     *
     * @return the starting index in the file
     */
    public int getStart() {
        return start;
    }

    /**
     * @return the number of records belonging to this run.
     */
    public int getRecords() {
        return records;
    }
}
