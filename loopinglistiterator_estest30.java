package org.apache.commons.collections4.iterators;

import static org.junit.Assert.assertFalse;

import java.util.Collections;
import java.util.List;
import org.junit.Test;

/**
 * Unit tests for {@link LoopingListIterator}.
 */
public class LoopingListIteratorTest {

    /**
     * Verifies that hasNext() returns false for an iterator over an empty list,
     * even after the reset() method is called.
     */
    @Test
    public void hasNextShouldReturnFalseForEmptyListAfterReset() {
        // Arrange: Create an iterator for an empty list.
        final List<Object> emptyList = Collections.emptyList();
        final LoopingListIterator<Object> iterator = new LoopingListIterator<>(emptyList);

        // Act: Reset the iterator.
        iterator.reset();

        // Assert: The iterator should still report that it has no next element.
        assertFalse("An iterator over an empty list should not have a next element after being reset.", iterator.hasNext());
    }
}