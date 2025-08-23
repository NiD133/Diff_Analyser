package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections4.IteratorUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ZippingIterator}, extending the base iterator tests
 * with specific scenarios.
 */
public class ZippingIteratorTest extends AbstractIteratorTest<Integer> {

    // Test data initialized in setUp()
    private ArrayList<Integer> evens;
    private ArrayList<Integer> odds;
    private ArrayList<Integer> fib;

    @Override
    public ZippingIterator<Integer> makeEmptyIterator() {
        return new ZippingIterator<>(IteratorUtils.emptyIterator());
    }

    @Override
    public ZippingIterator<Integer> makeObject() {
        return new ZippingIterator<>(evens.iterator(), odds.iterator(), fib.iterator());
    }

    /**
     * Sets up the test data collections before each test.
     * <ul>
     *   <li>evens: [0, 2, 4, 6, 8, 10, 12, 14, 16, 18]</li>
     *   <li>odds:  [1, 3, 5, 7, 9, 11, 13, 15, 17, 19]</li>
     *   <li>fib:   [1, 1, 2, 3, 5, 8, 13, 21]</li>
     * </ul>
     */
    @BeforeEach
    public void setUp() {
        evens = new ArrayList<>();
        odds = new ArrayList<>();
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
     * Tests that calling remove() on a ZippingIterator with a single underlying
     * iterator correctly modifies the source collection.
     */
    @Test
    void testRemoveOnSingleIterator() {
        // Arrange
        // The 'evens' list is [0, 2, 4, ..., 18]. We will remove all multiples of 4.
        final List<Integer> expectedRemainingElements = List.of(2, 6, 10, 14, 18);
        final ZippingIterator<Integer> zippingIterator = new ZippingIterator<>(evens.iterator());

        // Act
        // Iterate and remove elements that are multiples of 4.
        while (zippingIterator.hasNext()) {
            final Integer currentNumber = zippingIterator.next();
            if (currentNumber % 4 == 0) {
                zippingIterator.remove();
            }
        }

        // Assert
        // Verify that the underlying 'evens' collection was modified as expected.
        assertEquals(expectedRemainingElements, evens,
            "The underlying collection should only contain elements not removed by the iterator.");
    }
}