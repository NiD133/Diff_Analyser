package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections4.IteratorUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for ZippingIterator.
 * This class name has been improved from ZippingIteratorTestTest5 for clarity.
 */
public class ZippingIteratorTest extends AbstractIteratorTest<Integer> {

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
        // This iterator is used by the tests in the parent AbstractIteratorTest class.
        return new ZippingIterator<>(evens.iterator(), odds.iterator(), fib.iterator());
    }

    @BeforeEach
    public void setUp() {
        evens = new ArrayList<>();
        odds = new ArrayList<>();
        // evens will be [0, 2, 4, ..., 18]
        // odds will be  [1, 3, 5, ..., 19]
        for (int i = 0; i < 20; i++) {
            if (i % 2 == 0) {
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

    /**
     * Tests that the iterator correctly interleaves elements from two iterators of the same length.
     */
    @Test
    void testZippingWithTwoIteratorsOfSameLength() {
        // Arrange
        // The ZippingIterator should alternate between the odds and evens iterators, starting with odds.
        final ZippingIterator<Integer> zippingIterator = new ZippingIterator<>(odds.iterator(), evens.iterator());

        // The expected sequence is an explicit interleaving of the 'odds' and 'evens' lists.
        final List<Integer> expectedSequence = Arrays.asList(
            1, 0, 3, 2, 5, 4, 7, 6, 9, 8,
            11, 10, 13, 12, 15, 14, 17, 16, 19, 18
        );

        // Act & Assert
        // Iterate through the expected sequence and verify each element from the zipping iterator.
        for (final Integer expectedValue : expectedSequence) {
            assertTrue(zippingIterator.hasNext(), "Iterator should have more elements");
            assertEquals(expectedValue, zippingIterator.next(), "Iterator should return the next value in the zipped sequence");
        }

        // After consuming all elements, the iterator should be exhausted.
        assertFalse(zippingIterator.hasNext(), "Iterator should be exhausted after the loop");
    }
}