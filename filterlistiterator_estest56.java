package org.apache.commons.collections4.iterators;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Contains tests for the {@link FilterListIterator} class.
 * This class was improved from an auto-generated test.
 */
public class FilterListIterator_ESTestTest56 extends FilterListIterator_ESTest_scaffolding {

    /**
     * Tests that hasPrevious() returns false when the underlying iterator is empty
     * and positioned at the beginning.
     */
    @Test
    public void hasPreviousShouldReturnFalseForEmptyWrappedIterator() {
        // Arrange: Create a FilterListIterator that wraps an iterator from an empty list.
        final List<Object> emptyList = new ArrayList<>();
        final ListIterator<Object> emptyIterator = emptyList.listIterator();
        final FilterListIterator<Object> filteredIterator = new FilterListIterator<>(emptyIterator);

        // Act: Check if there is a previous element.
        final boolean hasPrevious = filteredIterator.hasPrevious();

        // Assert: The result should be false, as the underlying iterator has no elements.
        assertFalse("hasPrevious() should return false for an empty iterator", hasPrevious);
    }
}