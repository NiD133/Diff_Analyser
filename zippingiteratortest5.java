package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.ArrayList;
import org.apache.commons.collections4.IteratorUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ZippingIteratorTestTest5 extends AbstractIteratorTest<Integer> {

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
    void testIterateOddEven() {
        final ZippingIterator<Integer> iter = new ZippingIterator<>(odds.iterator(), evens.iterator());
        for (int i = 0, j = 0; i < 20; i++) {
            assertTrue(iter.hasNext());
            final int val = iter.next();
            if (i % 2 == 0) {
                assertEquals(odds.get(j).intValue(), val);
            } else {
                assertEquals(evens.get(j).intValue(), val);
                j++;
            }
        }
        assertFalse(iter.hasNext());
    }
}
