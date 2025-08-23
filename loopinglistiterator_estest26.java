package org.apache.commons.collections4.iterators;

import static org.junit.Assert.assertFalse;

import java.util.Collections;
import java.util.List;
import org.junit.Test;

/**
 * Contains tests for the {@link LoopingListIterator}.
 */
public class LoopingListIteratorTest {

    @Test
    public void hasPrevious_onEmptyList_returnsFalse() {
        // Arrange: Create an iterator for an empty list.
        // The Javadoc states that hasPrevious() should return false for an empty list.
        final List<Object> emptyList = Collections.emptyList();
        final LoopingListIterator<Object> iterator = new LoopingListIterator<>(emptyList);

        // Act: Check if the iterator has a previous element.
        final boolean hasPrevious = iterator.hasPrevious();

        // Assert: The result must be false.
        assertFalse("An iterator for an empty list should not have a previous element.", hasPrevious);
    }
}