package externalsort;

import java.nio.ByteBuffer;

/**
 * This class represents each record in the big file. Can be compared to
 * another record
 *
 * @author Muhammad Ali Qadri
 * @version 1
 */
public class Record implements Comparable<Record> {

    private final ByteBuffer record;
    private int runFlag;

    /**
     * This constructor accepts byte array
     *
     * @param rec bytes representing this record
     */
    public Record(byte[] rec) {

        if (rec == null || rec.length == 0) {
            throw new IllegalArgumentException();
        }

        record = ByteBuffer.wrap(rec.clone());
    }


    /**
     * This constructor accepts byte array and run flag
     *
     * @param rec bytes representing this record
     * @param run represents which run does this record belong to
     */
    public Record(byte[] rec, int run) {
        if (rec == null || rec.length != 16) {
            throw new IllegalArgumentException();
        }

        record = ByteBuffer.wrap(rec.clone());
        runFlag = run;
    }


    /**
     * Returns the bytes in this record
     *
     * @return byte array
     */
    public byte[] getBytes() {
        return record.array().clone();
    }


    /**
     * Returns the Long key in the record
     *
     * @return Long key value
     */
    public long getKey() {
        return record.getLong(0);
    }


    /**
     * Returns the double value in the record
     *
     * @return Double value
     */
    public double getValue() {
        return record.getDouble(8);
    }


    /**
     * Returns the run flag
     *
     * @return Int value
     */
    public int getRunFlag() {
        return runFlag;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return (17 * runFlag) + (record.hashCode() * 13) + super.hashCode();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Record)) return false;

        Record record1 = (Record) o;
        return getRunFlag() == record1.getRunFlag() &&
               record.equals(record1.record);
    }


    /**
     * returns string in format "key : value"
     *
     * @return string representation of this record
     */
    @Override
    public String toString() {
        //TODO: Fix format
        return getKey() + " " + getValue();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(Record o) {
        if (o == null) {
            return 0;
        }

        return Long.compare(getKey(), o.getKey());
    }
}