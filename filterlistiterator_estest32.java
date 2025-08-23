package org.apache.commons.collections4.iterators;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Tests for {@link FilterListIterator}.
 */
public class FilterListIteratorTest {

    /**
     * Tests that the nextIndex() method returns 0 for a newly created iterator
     * that has not been associated with an underlying list iterator.
     */
    @Test
    public void nextIndex_onNewIterator_returnsZero() {
        // Arrange: Create a new FilterListIterator using the default constructor.
        // This iterator is not yet backed by a collection.
        final FilterListIterator<Object> iterator = new FilterListIterator<>();

        // Act: Get the next index from the new iterator.
        final int index = iterator.nextIndex();

        // Assert: The index should be 0, which is the default initial state.
        assertEquals("The next index of a new, empty iterator should be 0", 0, index);
    }
}