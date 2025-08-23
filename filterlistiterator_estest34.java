package org.apache.commons.collections4.iterators;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link FilterListIterator}.
 * This test focuses on the behavior of the previousIndex() method.
 */
public class FilterListIteratorTest {

    @Test
    public void previousIndex_onNewIterator_shouldReturnNegativeOne() {
        // Arrange: Create a FilterListIterator using the default constructor.
        // At this stage, it has no underlying iterator.
        final FilterListIterator<Object> iterator = new FilterListIterator<>();

        // Act: Call previousIndex() on the new iterator.
        final int result = iterator.previousIndex();

        // Assert: The index should be -1, which is the expected state
        // for an iterator positioned at the beginning of a list.
        assertEquals(-1, result);
    }
}