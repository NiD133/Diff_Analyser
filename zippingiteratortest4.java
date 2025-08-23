package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections4.IteratorUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the ZippingIterator with iterators of uneven lengths to ensure it
 * correctly interleaves elements until all iterators are exhausted.
 */
public class ZippingIteratorUnevenLengthTest extends AbstractIteratorTest<Integer> {

    private List<Integer> evens;
    private List<Integer> odds;
    private List<Integer> fibonacci;

    @Override
    @SuppressWarnings("unchecked")
    public ZippingIterator<Integer> makeEmptyIterator() {
        return new ZippingIterator<>(IteratorUtils.<Integer>emptyIterator());
    }

    @Override
    public ZippingIterator<Integer> makeObject() {
        // The order here is different from the specific test case below,
        // which is intentional to cover different scenarios.
        return new ZippingIterator<>(evens.iterator(), odds.iterator(), fibonacci.iterator());
    }

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

        // The first 8 Fibonacci numbers (F_1 to F_8)
        fibonacci = List.of(1, 1, 2, 3, 5, 8, 13, 21);
    }

    /**
     * Tests that the ZippingIterator correctly interleaves elements from three
     * iterators of different lengths, continuing until all are exhausted.
     */
    @Test
    void testZippingWithThreeUnevenLengthIterators() {
        // Arrange
        // The ZippingIterator should interleave elements by cycling through the non-exhausted
        // iterators. The expected sequence is constructed by hand to verify this behavior.
        final List<Integer> expected = List.of(
            // Rounds 1-8: All three iterators (fibonacci, evens, odds) are active.
            // fib, even, odd
            1,   0,   1,
            1,   2,   3,
            2,   4,   5,
            3,   6,   7,
            5,   8,   9,
            8,  10,  11,
            13,  12,  13,
            21,  14,  15,

            // The 'fibonacci' iterator is now exhausted.
            // Rounds 9-10: The iterator cycles between the remaining 'evens' and 'odds'.
            // even, odd
            16, 17,
            18, 19
        );

        final ZippingIterator<Integer> zippingIterator =
            new ZippingIterator<>(fibonacci.iterator(), evens.iterator(), odds.iterator());

        // Act
        final List<Integer> actual = IteratorUtils.toList(zippingIterator);

        // Assert
        assertEquals(expected, actual, "The zipped list of elements should match the expected interleaved sequence.");
    }
}