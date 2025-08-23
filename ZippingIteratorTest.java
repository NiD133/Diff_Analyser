package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.IteratorUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit test suite for {@link ZippingIterator}.
 */
@SuppressWarnings("boxing")
class ZippingIteratorTest extends AbstractIteratorTest<Integer> {

    private List<Integer> evens;
    private List<Integer> odds;
    private List<Integer> fibonacci;

    @Override
    public ZippingIterator<Integer> makeEmptyIterator() {
        return new ZippingIterator<>(IteratorUtils.<Integer>emptyIterator());
    }

    @Override
    public ZippingIterator<Integer> makeObject() {
        return new ZippingIterator<>(evens.iterator(), odds.iterator(), fibonacci.iterator());
    }

    @BeforeEach
    public void setUp() {
        evens = createEvenNumbersList(20);
        odds = createOddNumbersList(20);
        fibonacci = createFibonacciList();
    }

    private List<Integer> createEvenNumbersList(int limit) {
        List<Integer> evenNumbers = new ArrayList<>();
        for (int i = 0; i < limit; i += 2) {
            evenNumbers.add(i);
        }
        return evenNumbers;
    }

    private List<Integer> createOddNumbersList(int limit) {
        List<Integer> oddNumbers = new ArrayList<>();
        for (int i = 1; i < limit; i += 2) {
            oddNumbers.add(i);
        }
        return oddNumbers;
    }

    private List<Integer> createFibonacciList() {
        List<Integer> fibonacciNumbers = new ArrayList<>();
        fibonacciNumbers.add(1);
        fibonacciNumbers.add(1);
        fibonacciNumbers.add(2);
        fibonacciNumbers.add(3);
        fibonacciNumbers.add(5);
        fibonacciNumbers.add(8);
        fibonacciNumbers.add(13);
        fibonacciNumbers.add(21);
        return fibonacciNumbers;
    }

    @Test
    void testIterateEvenNumbers() {
        ZippingIterator<Integer> iterator = new ZippingIterator<>(evens.iterator());
        for (Integer even : evens) {
            assertTrue(iterator.hasNext());
            assertEquals(even, iterator.next());
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    void testIterateDuplicateEvenNumbers() {
        ZippingIterator<Integer> iterator = new ZippingIterator<>(evens.iterator(), evens.iterator());
        for (Integer even : evens) {
            assertTrue(iterator.hasNext());
            assertEquals(even, iterator.next());
            assertTrue(iterator.hasNext());
            assertEquals(even, iterator.next());
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    void testIterateEvenAndOddNumbers() {
        ZippingIterator<Integer> iterator = new ZippingIterator<>(evens.iterator(), odds.iterator());
        for (int i = 0; i < 20; i++) {
            assertTrue(iterator.hasNext());
            assertEquals(Integer.valueOf(i), iterator.next());
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    void testIterateFibonacciEvenOddNumbers() {
        ZippingIterator<Integer> iterator = new ZippingIterator<>(fibonacci.iterator(), evens.iterator(), odds.iterator());

        assertNextValues(iterator, 1, 0, 1, 1, 2, 3, 2, 4, 5, 3, 6, 7, 5, 8, 9, 8, 10, 11, 13, 12, 13, 21, 14, 15, 16, 17, 18, 19);
        assertFalse(iterator.hasNext());
    }

    private void assertNextValues(ZippingIterator<Integer> iterator, Integer... expectedValues) {
        for (Integer expectedValue : expectedValues) {
            assertTrue(iterator.hasNext());
            assertEquals(expectedValue, iterator.next());
        }
    }

    @Test
    void testIterateOddAndEvenNumbers() {
        ZippingIterator<Integer> iterator = new ZippingIterator<>(odds.iterator(), evens.iterator());
        for (int i = 0, j = 0; i < 20; i++) {
            assertTrue(iterator.hasNext());
            int value = iterator.next();
            if (i % 2 == 0) {
                assertEquals(odds.get(j).intValue(), value);
            } else {
                assertEquals(evens.get(j).intValue(), value);
                j++;
            }
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    void testRemoveFromDoubleIterator() {
        ZippingIterator<Integer> iterator = new ZippingIterator<>(evens.iterator(), odds.iterator());
        int expectedSize = evens.size() + odds.size();
        while (iterator.hasNext()) {
            Integer value = iterator.next();
            if (value % 4 == 0 || value % 3 == 0) {
                expectedSize--;
                iterator.remove();
            }
        }
        assertEquals(expectedSize, evens.size() + odds.size());
    }

    @Test
    void testRemoveFromSingleIterator() {
        ZippingIterator<Integer> iterator = new ZippingIterator<>(evens.iterator());
        int expectedSize = evens.size();
        while (iterator.hasNext()) {
            Integer value = iterator.next();
            if (value % 4 == 0) {
                expectedSize--;
                iterator.remove();
            }
        }
        assertEquals(expectedSize, evens.size());
    }
}