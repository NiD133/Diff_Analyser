package org.apache.commons.collections4.iterators;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Tests for {@link BoundedIterator}.
 */
public class BoundedIteratorTest {

    /**
     * Tests that hasNext() returns true when the underlying iterator has elements
     * and the current position is within the specified bounds.
     */
    @Test
    public void hasNextShouldReturnTrueForNonEmptyIteratorWithinBounds() {
        // Arrange: Create an iterator with one element.
        final List<Integer> sourceList = Collections.singletonList(1);
        final Iterator<Integer> underlyingIterator = sourceList.iterator();

        // Create a BoundedIterator starting at offset 0 with a max of 5 elements.
        // The underlying iterator has fewer elements than max, so hasNext() should be true.
        final BoundedIterator<Integer> boundedIterator = new BoundedIterator<>(underlyingIterator, 0L, 5L);

        // Act: Check if there is a next element.
        final boolean hasNext = boundedIterator.hasNext();

        // Assert: The result should be true.
        assertTrue("hasNext() should return true as the iterator has one element and is within bounds.", hasNext);
    }
}