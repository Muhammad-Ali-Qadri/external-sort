package tests;

import org.junit.Before;
import externalsort.Minheap;
import externalsort.Heap;

import static org.junit.Assert.*;
import org.junit.Test;


public class MinheapTest {
    Heap<Integer> minheap;

    /*
    Sets up the minheap with an entry.
     */
    @Before
    public void setUp() {
        minheap = new Minheap<>();
        minheap.insert(5);
    }

    /*
    Tests if after inserting, minheap str is 5
     */
    @Test
    public void simpleTest() {
        assertEquals(minheap.toString(), "5");
    }

    @Test
    public void reverseOrdered() {
        minheap.insert(3);
        minheap.insert(2);
        assertEquals(minheap.toString(), "2,5,3");
    }

    @Test
    public void smallPopTest() {
        minheap.insert(3);
        minheap.insert(2);
        Integer firstPop = minheap.pop();
        Integer secondPop = minheap.pop();
        Integer thirdpop = minheap.pop();
        assertEquals(firstPop, (Integer) 2);
        assertEquals(secondPop, (Integer) 3);
        assertEquals(thirdpop, (Integer) 5);
    }

    /*
    tests hideAndSwap
    Note... I think this is the correct behavior.
    heap was 2 5 3 (which is valid)
    after trying to hide and swap 12.. it became just 3 and 5 since, top val got
    poppped
     */
    @Test
    public void hideAndSwapTest() {
        minheap.insert(3);
        minheap.insert(2);
        minheap.hideAndSwap(12);
        String str = minheap.toString();
        assertEquals(minheap.toString(), "3,5");
    }

}
