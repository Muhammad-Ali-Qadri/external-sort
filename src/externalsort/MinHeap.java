package externalsort;

import java.lang.reflect.Array;


/**
 * Generic min heap, implements heap structure.
 *
 * @author Muhammad Ali Qadri
 * @version 1
 */
public class MinHeap<T extends Comparable<? super T>>
        implements Heap<T> {

    private T[] heap; // Pointer to the heap array
    private int currentSize;
    private int index;
    private final int maxHeapSize;

    /**
     * Create the minheap according to the input maximum size
     *
     * @param maxSize The max default size of this heap
     */
    @SuppressWarnings("unchecked")
    public MinHeap(int maxSize) {
        maxHeapSize = maxSize;

        heap = (T[]) Array.newInstance(Comparable.class, maxHeapSize);
        index = 0; // index its on

        currentSize = maxHeapSize; // max entries
    }


    public MinHeap(T[] h, int num, int max) {
        maxHeapSize = max;
        heap = h;
        index = num;
        currentSize = max;
        heapify();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void insert(T value) {
        if (index >= currentSize) {
            return;
        }

        int curr = index++;
        heap[curr] = value;

        while ((curr != 0) && (heap[curr].compareTo(heap[parent(curr)]) < 0)) {
            swap(curr, parent(curr));
            curr = parent(curr);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public T pop() {
        assert index > 0 : "Removing from empty heap";
        swap(0, --index); // Swap minimum with last value
        if (index != 0) {    // Not on last element
            siftDown(0);   // Put new heap root val in correct place
        }

        return heap[index];
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void hideAtEnd(T value) {
        heap[--currentSize] = value;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void heapify() {
        for (int i = index / 2 - 1; i >= 0; i--) {
            siftDown(i);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void recreate() {
        if(hasHiddenElements()){
            index = maxHeapSize;
            currentSize = maxHeapSize;
            heapify();
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFull() {
        return index == currentSize;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return index == 0;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasHiddenElements() {
        return currentSize < maxHeapSize;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int getSize() {
        return index;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void heapifyHiddenElements() {
        //Only when visible elements are empty and only hidden elements remain
        //Bring the hidden elements to the front of the heap
        if(index == 0 && currentSize < maxHeapSize){
            for(int i = 0; currentSize + i < maxHeapSize; i++){
                heap[i] = heap[i + currentSize];
            }

            index = maxHeapSize - currentSize;
            currentSize = maxHeapSize;

            heapify();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < index; i++) {
            s.append(heap[i].toString());

            if (i + 1 < index) {
                s.append(", ");
            }
        }

        //Print hidden elements at end of heap
        if (currentSize < maxHeapSize) {
            s.append("\nHidden Elements: ");
            for (int i = currentSize; i < maxHeapSize; i++) {
                s.append(heap[i].toString());

                if (i + 1 < maxHeapSize) {
                    s.append(", ");
                }
            }
        }

        return s.toString();
    }

    private void swap(int p1, int p2) {
        T temp = heap[p1];
        heap[p1] = heap[p2];
        heap[p2] = temp;
    }

    // returns true if its a leaf slot
    private boolean isLeaf(int pos) {
        return (pos >= index / 2) && (pos < index);
    }

    private int leftChild(int pos) {
        if (pos >= index / 2) {
            return -1;
        }

        return 2 * pos + 1;
    }

    private int rightChild(int pos) {
        if (pos >= (index - 1) / 2) {
            return -1;
        }

        return 2 * pos + 2;
    }

    private int parent(int pos) {
        if (pos <= 0) {
            return -1;
        }

        return (pos - 1) / 2;
    }

    private void siftDown(int pos) {
        assert (pos >= 0) && (pos < index) : "Illegal heap position";

        while (!isLeaf(pos)) {
            int j = leftChild(pos);

            if ((j < (index - 1)) && (heap[j].compareTo(heap[j + 1]) > 0)) {
                j++; // j is now index of child with greater value
            }

            if (heap[pos].compareTo(heap[j]) <= 0) {
                return;
            }

            swap(pos, j);
            pos = j;  // Move down
        }
    }

}
