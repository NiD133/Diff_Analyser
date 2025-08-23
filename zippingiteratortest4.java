package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.ArrayList;
import org.apache.commons.collections4.IteratorUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ZippingIteratorTestTest4 extends AbstractIteratorTest<Integer> {

    private ArrayList<Integer> evens;

    private ArrayList<Integer> odds;

    private ArrayList<Integer> fib;

    @Override
    @SuppressWarnings("unchecked")
    public ZippingIterator<Integer> makeEmptyIterator() {
        return new ZippingIterator<>(IteratorUtils.<Integer>emptyIterator());
    }

    @Override
    public ZippingIterator<Integer> makeObject() {
        return new ZippingIterator<>(evens.iterator(), odds.iterator(), fib.iterator());
    }

    @BeforeEach
    public void setUp() throws Exception {
        evens = new ArrayList<>();
        odds = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            if (0 == i % 2) {
                evens.add(i);
            } else {
                odds.add(i);
            }
        }
        fib = new ArrayList<>();
        fib.add(1);
        fib.add(1);
        fib.add(2);
        fib.add(3);
        fib.add(5);
        fib.add(8);
        fib.add(13);
        fib.add(21);
    }

    @Test
    void testIterateFibEvenOdd() {
        final ZippingIterator<Integer> iter = new ZippingIterator<>(fib.iterator(), evens.iterator(), odds.iterator());
        // fib    1
        assertEquals(Integer.valueOf(1), iter.next());
        // even   0
        assertEquals(Integer.valueOf(0), iter.next());
        // odd    1
        assertEquals(Integer.valueOf(1), iter.next());
        // fib    1
        assertEquals(Integer.valueOf(1), iter.next());
        // even   2
        assertEquals(Integer.valueOf(2), iter.next());
        // odd    3
        assertEquals(Integer.valueOf(3), iter.next());
        // fib    2
        assertEquals(Integer.valueOf(2), iter.next());
        // even   4
        assertEquals(Integer.valueOf(4), iter.next());
        // odd    5
        assertEquals(Integer.valueOf(5), iter.next());
        // fib    3
        assertEquals(Integer.valueOf(3), iter.next());
        // even   6
        assertEquals(Integer.valueOf(6), iter.next());
        // odd    7
        assertEquals(Integer.valueOf(7), iter.next());
        // fib    5
        assertEquals(Integer.valueOf(5), iter.next());
        // even   8
        assertEquals(Integer.valueOf(8), iter.next());
        // odd    9
        assertEquals(Integer.valueOf(9), iter.next());
        // fib    8
        assertEquals(Integer.valueOf(8), iter.next());
        // even  10
        assertEquals(Integer.valueOf(10), iter.next());
        // odd   11
        assertEquals(Integer.valueOf(11), iter.next());
        // fib   13
        assertEquals(Integer.valueOf(13), iter.next());
        // even  12
        assertEquals(Integer.valueOf(12), iter.next());
        // odd   13
        assertEquals(Integer.valueOf(13), iter.next());
        // fib   21
        assertEquals(Integer.valueOf(21), iter.next());
        // even  14
        assertEquals(Integer.valueOf(14), iter.next());
        // odd   15
        assertEquals(Integer.valueOf(15), iter.next());
        // even  16
        assertEquals(Integer.valueOf(16), iter.next());
        // odd   17
        assertEquals(Integer.valueOf(17), iter.next());
        // even  18
        assertEquals(Integer.valueOf(18), iter.next());
        // odd   19
        assertEquals(Integer.valueOf(19), iter.next());
        assertFalse(iter.hasNext());
    }
}
