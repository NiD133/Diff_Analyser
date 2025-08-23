package org.apache.commons.collections4.iterators;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.ListIterator;
import org.junit.Test;

/**
 * Contains tests for the {@link FilterListIterator}.
 */
public class FilterListIteratorTest {

    /**
     * Tests that previousIndex() returns -1 for a newly created iterator,
     * consistent with the ListIterator contract for an iterator at the beginning
     * of a list.
     */
    @Test
    public void previousIndexShouldReturnNegativeOneOnNewIterator() {
        // Arrange: Create a FilterListIterator wrapping an empty iterator.
        // The specific type of wrapped iterator or predicate is not relevant for this test.
        final ListIterator<Object> emptyIterator = Collections.emptyListIterator();
        final FilterListIterator<Object> filteredIterator = new FilterListIterator<>(emptyIterator);

        // Act: Get the initial previous index.
        final int previousIndex = filteredIterator.previousIndex();

        // Assert: The index should be -1, as the iterator is at the start.
        assertEquals("The previous index of a new iterator should be -1", -1, previousIndex);
    }
}