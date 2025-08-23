package org.apache.commons.collections4.iterators;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link FilterListIterator} class.
 * This version focuses on a specific test case for improved clarity.
 */
public class FilterListIteratorImprovedTest {

    /**
     * Tests that previousIndex() returns -1 for a new FilterListIterator
     * that has not been initialized with an underlying ListIterator.
     * This behavior is consistent with a standard ListIterator on an empty list.
     */
    @Test
    public void previousIndexShouldReturnNegativeOneForIteratorWithoutDelegate() {
        // Arrange: Create a FilterListIterator using the default constructor,
        // which leaves the underlying iterator as null.
        final FilterListIterator<String> filterListIterator = new FilterListIterator<>();

        // Act: Call the previousIndex() method.
        final int previousIndex = filterListIterator.previousIndex();

        // Assert: The result should be -1, as there are no elements.
        assertEquals(-1, previousIndex);
    }
}