package org.apache.commons.collections4.iterators;

import static org.junit.Assert.assertFalse;
import org.junit.Test;

/**
 * Contains tests for the FilterListIterator class.
 * Note: The original test class name "FilterListIterator_ESTestTest58" was auto-generated.
 * A more conventional name would be "FilterListIteratorTest".
 */
public class FilterListIterator_ESTestTest58 extends FilterListIterator_ESTest_scaffolding {

    /**
     * Tests that hasPrevious() returns false when the FilterListIterator
     * is created with the default constructor and has no underlying iterator set.
     */
    @Test
    public void hasPreviousShouldReturnFalseWhenIteratorIsNotSet() {
        // Arrange: Create a FilterListIterator using the default constructor.
        // This means it has no underlying iterator to traverse.
        final FilterListIterator<Integer> emptyIterator = new FilterListIterator<>();

        // Act & Assert: Calling hasPrevious() on an iterator without a backing
        // iterator should correctly return false.
        assertFalse("hasPrevious() should be false for an uninitialized iterator", emptyIterator.hasPrevious());
    }
}