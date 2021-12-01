package externalsort;

/**
 * This class represents a generic heap interface.
 *
 * @param <T> The generic type of values stored in this heap
 * @author Muhammad Ali Qadri
 * @version 1
 */

public interface Heap<T extends Comparable<?>> {

    /**
     * Insert value into the Heap, and maintain heap property
     *
     * @param value the value to be inserted
     */
    void insert(T value);


    /**
     * Removes the top value from heap and maintains heap property
     *
     * @return the value removed from this Heap
     */
    T pop();


    /**
     * Swaps the input value with the right most value in the heap. Then hides
     * the input value at the end of the heap by reducing size of heap
     * by 1. Hence, the added value cannot be accessed from the heap, and
     * then the heap needs to be restructured to maintain heap property.
     *
     * @param value the value to swap and hide
     */
    void hideAtEnd(T value);


    /**
     * Restructure the heap to comply with underlying heap property.
     */
    void heapify();


    /**
     * Take size of heap back to original initialized heap size, and heapify
     */
    void recreate();


    /**
     * Returns the size of current heap
     *
     * @return int value representing size
     */
    int getSize();


    /**
     * Returns If the heap is full
     *
     * @return True or False value indicating if heap is full or not
     */
    boolean isFull();


    /**
     * Returns If the heap is empty
     *
     * @return True or False value indicating if heap is empty or not
     */
    boolean isEmpty();

    /**
     * Functions only when visible elements are empty and only hidden elements
     * remain. Brings the hidden elements to the front of the heap and does
     * heapify
     */
    void heapifyHiddenElements();
}
