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
 * <p>
 * This class includes tests for specific zipping scenarios and provides
 * iterator instances for the generic tests in {@link AbstractIteratorTest}.
 * </p>
 */
public class ZippingIteratorTest extends AbstractIteratorTest<Integer> {

    // Constants to define the size of test collections
    private static final int DATA_SIZE = 10;

    // Test data collections
    private List<Integer> evenNumbers;
    private List<Integer> oddNumbers;
    private List<Integer> fibonacciNumbers;

    @BeforeEach
    public void setUp() {
        // Initialize lists with predictable data
        evenNumbers = new ArrayList<>();
        oddNumbers = new ArrayList<>();
        // Total elements will be DATA_SIZE * 2
        for (int i = 0; i < DATA_SIZE * 2; i++) {
            if (i % 2 == 0) {
                evenNumbers.add(i);
            } else {
                oddNumbers.add(i);
            }
        }

        fibonacciNumbers = Arrays.asList(1, 1, 2, 3, 5, 8, 13, 21);
    }

    /**
     * Provides a ZippingIterator instance for the abstract test suite.
     * This iterator interleaves three distinct sequences.
     */
    @Override
    public ZippingIterator<Integer> makeObject() {
        return new ZippingIterator<>(evenNumbers.iterator(), oddNumbers.iterator(), fibonacciNumbers.iterator());
    }

    /**
     * Provides an empty ZippingIterator for the abstract test suite.
     */
    @Override
    @SuppressWarnings("unchecked")
    public ZippingIterator<Integer> makeEmptyIterator() {
        return new ZippingIterator<>(IteratorUtils.<Integer>emptyIterator());
    }

    /**
     * Tests that zipping an iterator of even numbers and an iterator of odd numbers
     * results in a single, sequential iterator of all numbers.
     */
    @Test
    void testZippingEvensAndOddsProducesSequentialNumbers() {
        // Arrange: Create an iterator by zipping two iterators of equal length.
        final ZippingIterator<Integer> zippingIterator =
                new ZippingIterator<>(evenNumbers.iterator(), oddNumbers.iterator());
        final int totalElements = evenNumbers.size() + oddNumbers.size();

        // Act & Assert: Verify that the iterator produces a complete sequence.
        for (int i = 0; i < totalElements; i++) {
            assertTrue(zippingIterator.hasNext(), "Iterator should have a next element at index " + i);
            final Integer expected = i;
            final Integer actual = zippingIterator.next();
            assertEquals(expected, actual, "Element at index " + i + " should match the expected sequence");
        }

        // Assert: Verify the iterator is exhausted after the loop.
        assertFalse(zippingIterator.hasNext(), "Iterator should be exhausted after iterating through all elements");
    }
}