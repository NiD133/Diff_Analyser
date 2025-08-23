package org.apache.commons.collections4.iterators;

import org.junit.Test;

import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Tests for {@link FilterListIterator} focusing on its fail-fast behavior.
 */
public class FilterListIteratorTest {

    /**
     * Tests that the iterator throws a ConcurrentModificationException if the
     * underlying list is modified after the iterator is created.
     * This confirms that the fail-fast behavior of the wrapped iterator is preserved.
     */
    @Test(expected = ConcurrentModificationException.class)
    public void nextShouldThrowConcurrentModificationExceptionIfCollectionIsModified() {
        // Arrange: Create a list and an iterator for it.
        List<Integer> list = new LinkedList<>();
        ListIterator<Integer> underlyingIterator = list.listIterator();
        FilterListIterator<Integer> filterIterator = new FilterListIterator<>(underlyingIterator);

        // Act: Modify the list directly, which should invalidate the underlying iterator.
        list.add(100);

        // Assert: Calling next() on the filter iterator should propagate the
        // ConcurrentModificationException from the underlying iterator.
        filterIterator.next();
    }
}