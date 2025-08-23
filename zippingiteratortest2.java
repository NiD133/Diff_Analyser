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
 * This class was renamed from ZippingIteratorTestTest2 for clarity.
 */
public class ZippingIteratorTest extends AbstractIteratorTest<Integer> {

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
        evens = new ArrayList<>();
        odds = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0) {
                evens.add(i);
            } else {
                odds.add(i);
            }
        }

        fibonacci = Arrays.asList(1, 1, 2, 3, 5, 8, 13, 21);
    }

    /**
     * Tests that zipping an iterator with itself interleaves the elements correctly.
     * For an input iterator [a, b, c], the zipping iterator should produce [a, a, b, b, c, c].
     */
    @Test
    void shouldInterleaveElementsWhenZippingAnIteratorWithItself() {
        // Arrange: Create a zipping iterator from two iterators over the same list.
        final ZippingIterator<Integer> zippingIterator = new ZippingIterator<>(evens.iterator(), evens.iterator());

        // Act & Assert: Verify that each element from the original list appears twice in a row.
        for (final Integer expectedElement : evens) {
            assertTrue(zippingIterator.hasNext(), "Iterator should have a next element (first occurrence).");
            assertEquals(expectedElement, zippingIterator.next(), "First occurrence of the element should match.");

            assertTrue(zippingIterator.hasNext(), "Iterator should have a next element (second occurrence).");
            assertEquals(expectedElement, zippingIterator.next(), "Second occurrence of the element should match.");
        }

        // Assert: The iterator should be exhausted after iterating through all elements.
        assertFalse(zippingIterator.hasNext(), "Iterator should be exhausted at the end.");
    }
}