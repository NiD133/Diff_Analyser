package org.apache.commons.collections4.iterators;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * This class contains tests for FilterListIterator, focusing on specific edge cases.
 * The original test was auto-generated and has been rewritten for clarity.
 */
public class FilterListIterator_ESTestTest37 extends FilterListIterator_ESTest_scaffolding {

    /**
     * Tests that hasNext() returns false when the FilterListIterator wraps another
     * iterator that is also empty. This ensures that the empty state is correctly
     * propagated through nested iterators.
     */
    @Test
    public void hasNextShouldReturnFalseWhenWrappingAnotherEmptyIterator() {
        // Arrange: Create an inner FilterListIterator that is empty because it doesn't
        // wrap any underlying collection iterator.
        FilterListIterator<Object> emptyInnerIterator = new FilterListIterator<>();

        // Arrange: Create the outer FilterListIterator, configured to wrap the empty inner iterator.
        FilterListIterator<Object> outerIterator = new FilterListIterator<>(emptyInnerIterator);

        // Act & Assert: Verify that hasNext() on the outer iterator returns false,
        // as the wrapped inner iterator has no elements.
        assertFalse("hasNext() should return false when the wrapped iterator is empty", outerIterator.hasNext());
    }
}