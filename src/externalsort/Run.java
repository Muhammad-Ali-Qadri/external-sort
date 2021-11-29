package externalsort;


/**
 * Stores the specifications for each run
 *
 * @author Muhammad Ali Qadri
 * @version 1
 */
public class Run {
    private final int start;
    private final int length;

    /**
     * Initializes a run
     *
     * @param len length of run
     * @param st the starting byte of the run
     * */
    public Run(int st, int len){
        if(len == 0){
            throw new IllegalArgumentException();
        }

        start = st;
        length = len;
    }

    public int getStart() {
        return start;
    }

    public int getLength() {
        return length;
    }
}
