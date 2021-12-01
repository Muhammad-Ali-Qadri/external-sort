package tests;

import externalsort.Heap;
import externalsort.MinHeap;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


/**
 * Test min heap
 *
 * @author Muhammad Ali Qadri
 * @version 1
 * */
public class MinHeapTest {
    private Heap<Integer> minheap;
    private final int maxHeapSize = 7; //Convenient while testing

    /**
     * Sets up the minheap with an entry.
     */
    @Before
    public void setUp() {
        minheap = new MinHeap<>(maxHeapSize);
        minheap.insert(5);
    }

    /**
     * Tests if after inserting, minheap str is 5
     */
    @Test
    public void simpleTest() {
        assertEquals("5", minheap.toString());
    }

    /**
     * Tests exceed size limit
     */
    @Test
    public void overflowTest() {
        minheap.insert(7);
        minheap.insert(6);
        minheap.insert(4);
        minheap.insert(3);
        minheap.insert(2);
        minheap.insert(1);
        minheap.insert(0);
        assertEquals("1, 4, 2, 7, 5, 6, 3", minheap.toString());
    }

    /**
     * Test insertion of elements in reverse order
     * */
    @Test
    public void reverseOrdered() {
        minheap.insert(3);
        minheap.insert(2);
        assertEquals("2, 5, 3", minheap.toString());
    }

    /**
     * Test pop of minimum element
     * */
    @Test
    public void smallPopTest() {
        minheap.insert(3);
        minheap.insert(2);
        Integer firstPop = minheap.pop();
        Integer secondPop = minheap.pop();
        Integer thirdpop = minheap.pop();
        assertEquals((Integer) 2, firstPop);
        assertEquals((Integer) 3, secondPop);
        assertEquals((Integer) 5, thirdpop);
    }

    /**
     * tests hide at end. Check if the value added is appended to the end of
     * the heap, and its size is reduced.
     */
    @Test
    public void hideAtEndTest() {
        overflowTest(); //fill out the minHeap

        assertEquals((Integer) 1, minheap.pop());

        minheap.hideAtEnd(8);
        assertTrue(minheap.isFull());
        assertEquals(6, minheap.getSize());

        String str = minheap.toString();
        assertEquals("2, 4, 3, 7, 5, 6\nHidden Elements: 8",
                minheap.toString());
    }

    /**
     * tests hide at end. Check if multiple values added are appended to the
     * end of the heap, and its size is reduced.
     */
    @Test
    public void extensiveHideAtEndTest() {
        overflowTest(); //fill out the minHeap

        popAndHide(1, 8, 6);
        popAndHide(2, 2, 5);
        popAndHide(3, 1, 4);

        String str = minheap.toString();
        assertEquals("4, 5, 6, 7\nHidden Elements: 1, 2, 8",
                minheap.toString());
    }


    /**
     * Test if heap can restructure itself to comply with heap property
     * */
    @Test
    public void testHeapify(){
        overflowTest();
        minheap.heapify();
        assertEquals("1, 4, 2, 7, 5, 6, 3", minheap.toString());
    }


    /**
     * Test if heap can bring its size back to default and re-add hidden
     * elements
     * */
    @Test
    public void testCreate(){
        extensiveHideAtEndTest();
        minheap.recreate();
        assertEquals("1, 4, 2, 7, 5, 6, 8", minheap.toString());
        assertEquals(maxHeapSize, minheap.getSize());
    }


    /**
     * test heapify hidden elements. When heap is empty and only hidden
     * elements remain, they are put back into start of heap and heapify is done
     * */
    @Test
    public void testHeapifyHiddenElements(){
        minheap.hideAtEnd(8);
        minheap.hideAtEnd(9);

        minheap.heapifyHiddenElements();
        assertEquals(1, minheap.getSize());

        assertEquals((Integer) 5, minheap.pop());

        assertTrue(minheap.isEmpty());

        minheap.heapifyHiddenElements();
        assertEquals("8, 9", minheap.toString());
        assertEquals(2, minheap.getSize());
    }

    /**
     * test heapify hidden elements for entire heap size. When heap is empty
     * and only hidden elements remain, they are put back into start of heap
     * and heapify is done
     * */
    @Test
    public void testComplexHeapifyHiddenElements(){
        overflowTest();

        popAndHide(1, 3, 6);
        popAndHide(2, 4, 5);
        popAndHide(3, 5, 4);
        popAndHide(4, 6, 3);
        popAndHide(5, 7, 2);
        popAndHide(6, 8, 1);
        popAndHide(7, 9, 0);

        assertEquals("\nHidden Elements: 9, 8, 7, 6, 5, 4, 3",
                minheap.toString());
        assertTrue(minheap.isEmpty());

        minheap.heapifyHiddenElements();
        assertEquals("3, 5, 4, 6, 8, 9, 7", minheap.toString());
        assertEquals(maxHeapSize, minheap.getSize());
        assertTrue(minheap.isFull());

    }


    private void popAndHide(int pop, int hide, int expectedSize){
        assertEquals((Integer) pop, minheap.pop());

        minheap.hideAtEnd(hide);
        assertTrue(minheap.isFull());
        assertEquals(expectedSize, minheap.getSize());
    }
}
