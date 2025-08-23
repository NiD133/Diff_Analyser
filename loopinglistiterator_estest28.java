package org.apache.commons.collections4.iterators;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

import java.util.Collections;
import java.util.List;

/**
 * Tests for the {@link LoopingListIterator} class.
 */
public class LoopingListIteratorTest {

    /**
     * Tests that hasNext() correctly returns false when the iterator is created
     * with an empty list.
     */
    @Test
    public void hasNextShouldReturnFalseForEmptyList() {
        // Arrange: Create an iterator for an empty list.
        final List<Object> emptyList = Collections.emptyList();
        final LoopingListIterator<Object> iterator = new LoopingListIterator<>(emptyList);

        // Act: Check if the iterator has a next element.
        final boolean hasNext = iterator.hasNext();

        // Assert: The result should be false, as there are no elements.
        assertFalse("An iterator over an empty list should not have a next element.", hasNext);
    }
}