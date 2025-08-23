package org.apache.commons.collections4.iterators;

import org.junit.Test;

import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Contains tests for the FilterListIterator class, focusing on its behavior
 * when the underlying collection is modified during iteration.
 */
public class FilterListIteratorConcurrencyTest {

    /**
     * Tests that hasNext() throws a ConcurrentModificationException if the underlying
     * collection is modified after the FilterListIterator is created. This is the
     * expected "fail-fast" behavior inherited from the underlying iterator.
     */
    @Test(expected = ConcurrentModificationException.class)
    public void hasNextShouldThrowConcurrentModificationExceptionWhenUnderlyingListIsModified() {
        // Arrange: Create a list, get an iterator, and wrap it in a FilterListIterator.
        final LinkedList<Integer> list = new LinkedList<>();
        final ListIterator<Integer> underlyingIterator = list.listIterator();
        final FilterListIterator<Integer> filteredIterator = new FilterListIterator<>(underlyingIterator);

        // Act: Modify the underlying list directly. This invalidates the iterator's state.
        list.add(100);

        // Assert: Calling hasNext() on the now-invalidated iterator is expected to throw
        // a ConcurrentModificationException.
        filteredIterator.hasNext();
    }
}