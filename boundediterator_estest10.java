package org.apache.commons.collections4.iterators;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

import java.util.Collections;
import java.util.Iterator;

/**
 * Contains tests for the BoundedIterator class.
 */
public class BoundedIteratorTest {

    /**
     * Tests that hasNext() returns false when the iterator is bounded
     * with a maximum size of zero. An iterator with a zero-length bound
     * should always be empty, regardless of the underlying iterator.
     */
    @Test
    public void hasNextShouldReturnFalseWhenMaxIsZero() {
        // Arrange: Create an iterator and wrap it in a BoundedIterator
        // with a maximum size (max) of 0.
        final Iterator<String> emptyIterator = Collections.emptyIterator();
        final BoundedIterator<String> boundedIterator = new BoundedIterator<>(emptyIterator, 0L, 0L);

        // Act & Assert: Verify that hasNext() returns false, as no elements
        // should be returned when the maximum size is zero.
        assertFalse("An iterator with a max size of 0 should not have a next element.", boundedIterator.hasNext());
    }
}