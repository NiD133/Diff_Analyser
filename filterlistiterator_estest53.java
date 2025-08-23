package org.apache.commons.collections4.iterators;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for {@link FilterListIterator}.
 */
public class FilterListIteratorTest {

    /**
     * Tests that hasNext() returns false when the FilterListIterator is created
     * with the default constructor and has no underlying iterator set.
     */
    @Test
    public void hasNextShouldReturnFalseWhenNoIteratorIsSet() {
        // Arrange: Create a FilterListIterator using the default constructor.
        // This iterator is not backed by any collection.
        FilterListIterator<Object> emptyIterator = new FilterListIterator<>();

        // Act & Assert: Verify that hasNext() correctly reports no more elements.
        assertFalse("hasNext() must return false for an iterator with no underlying collection", emptyIterator.hasNext());
    }
}