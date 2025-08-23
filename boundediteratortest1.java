package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for BoundedIterator.
 *
 * @param <E> the type of element in the iterator
 */
public class BoundedIteratorTest<E> extends AbstractIteratorTest<E> {

    /** The full list of elements used for testing. */
    private final String[] testArray = { "a", "b", "c", "d", "e", "f", "g" };

    private List<E> fullList;

    @SuppressWarnings("unchecked")
    @BeforeEach
    public void setUp() {
        fullList = Arrays.asList((E[]) testArray);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<E> makeEmptyIterator() {
        return new BoundedIterator<>(Collections.<E>emptyList().iterator(), 0, 10);
    }

    /**
     * Creates a BoundedIterator that is used by the inherited abstract tests.
     * It skips the first element and is limited to the remaining elements.
     */
    @Override
    public Iterator<E> makeObject() {
        final int offset = 1;
        // max is the number of elements to return, not an end index.
        final int max = fullList.size() - offset;
        return new BoundedIterator<>(new ArrayList<>(fullList).iterator(), offset, max);
    }

    /**
     * Tests that the iterator correctly returns a sub-sequence from the middle
     * of the underlying collection based on the offset and max parameters.
     */
    @Test
    void testIterationIsCorrectlyBoundedByOffsetAndMax() {
        // Arrange: Create an iterator bounded to a sub-range.
        // It should skip the first 2 elements ("a", "b") and return at most 4 elements.
        final int offset = 2;
        final int maxElements = 4;
        final Iterator<E> boundedIterator = new BoundedIterator<>(fullList.iterator(), offset, maxElements);

        // The expected sequence is ["c", "d", "e", "f"].
        @SuppressWarnings("unchecked")
        final List<E> expectedElements = Arrays.asList((E) "c", (E) "d", (E) "e", (E) "f");

        // Act & Assert: Verify the iterator returns the expected elements in order.
        for (final E expected : expectedElements) {
            assertTrue(boundedIterator.hasNext(), "Iterator should have more elements.");
            assertEquals(expected, boundedIterator.next(), "Iterator should return the correct next element.");
        }

        // Assert: The iterator should now be exhausted.
        assertFalse(boundedIterator.hasNext(), "Iterator should have no more elements after iterating through the bounded range.");

        // Assert: Calling next() again throws an exception.
        assertThrows(NoSuchElementException.class, boundedIterator::next,
                "Should throw NoSuchElementException when no elements are left.");
    }
}