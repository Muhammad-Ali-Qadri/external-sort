package externalsort;

public class Minheap<T extends Comparable<? super T>>
        implements Heap<T> {
    private Comparable[] Heap; // Pointer to the heap array
    private int size;
    private int n;
    private final int maxheapsize = 4096;

    public Minheap() {
        Heap = new Comparable[maxheapsize];
        n = 0; // index its on
        size = maxheapsize; // max entries
    }
    public Minheap(T[] h, int num , int max) {
        Heap = h;
        n = num;
        size = max;
        heapify();
    }

    @Override
    public void insert(T value) {
        if (n >= size) {
            //System.out.println("Heap is full");
            return;
        }
        int curr = n++;
        Heap[curr] = value;

        while ((curr != 0)  &&
                (Heap[curr].compareTo(Heap[parent(curr)]) < 0)) {
            swap(curr, parent(curr));
            curr = parent(curr);
        }
    }

    @Override
    public T pop() {
        assert n > 0 : "Removing from empty heap";
        swap(0, --n); // Swap minimum with last value
        if (n != 0) {    // Not on last element
            siftdown(0);   // Put new heap root val in correct place
        }
        return (T) Heap[n];
    }

    @Override
    public void hideAndSwap(T value) {
        Comparable temp = Heap[--n];

        Heap[0] = value;
        Heap[n] = Heap[0];
        Heap[0] = temp;
        if (n != 0) {
            siftdown(0);   // Put new heap root val in correct place
        }
    }

    @Override
    public void heapify() {
        for (int i=n/2-1; i>=0; i--) {
            siftdown(i);
        }
    }

    @Override
    public void recreate() {
        n = maxheapsize;
        heapify();
    }

    @Override
    public int getSize() {
        return n;
    }

    //not sure what this one does
    @Override
    public void reduceSize(int newSize) {
    }

    // returns true if its a leaf slot
    private boolean isLeaf(int pos) {
        return (pos >= n/2) && (pos < n) ;
    }

    private int leftChild(int pos) {
        if (pos >= n/2) {
            return -1;
        }
        return 2*pos + 1;
    }

    private int rightChild(int pos) {
        if (pos >= (n-1)/2)
        {
            return -1;
        }
        return 2*pos + 2;
    }

    private int parent(int pos) {
        if (pos <= 0)
        {
            return -1;
        }
        return (pos-1)/2;
    }

    public void swap(int p1, int p2) {
        Comparable temp = Heap[p1];
        Heap[p1] = Heap[p2];
        Heap[p2] = temp;
    }

    private void siftdown(int pos) {
        assert (pos >= 0) && (pos < n) : "Illegal heap position";
        while (!isLeaf(pos)) {
            int j = leftChild(pos);
            if ((j < (n - 1)) && (Heap[j].compareTo(Heap[j + 1]) > 0)) {
                j++; // j is now index of child with greater value
            }
            if (Heap[pos].compareTo(Heap[j]) <= 0) {
                return;
            }
            swap(pos, j);
            pos = j;  // Move down
        }
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < n; i++) {
            Comparable item = Heap[i];
            s.append(item.toString());
            s.append(",");
        }
        if(s.length() > 0)
            return s.substring(0, s.length() - 1); // remove trailing comma
        return s.toString();
    }

}
