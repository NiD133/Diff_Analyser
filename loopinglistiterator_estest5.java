package org.apache.commons.collections4.iterators;

import org.junit.Test;

import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.List;

/**
 * Test suite for the LoopingListIterator class.
 */
public class LoopingListIteratorTest {

    /**
     * Verifies that the iterator's set() method throws a ConcurrentModificationException
     * if the underlying list is modified externally after the iterator has been created.
     */
    @Test(expected = ConcurrentModificationException.class)
    public void setShouldThrowConcurrentModificationExceptionWhenListIsModifiedExternally() {
        // Arrange: Create a list with one element and a LoopingListIterator for it.
        final List<Integer> list = new LinkedList<>();
        list.add(100);
        final LoopingListIterator<Integer> iterator = new LoopingListIterator<>(list);

        // To enable the set() method, we must first call next() or previous().
        // Calling previous() on a new iterator over a single-element list will
        // loop around and position the iterator after that element.
        iterator.previous();

        // Act: Modify the list directly, not through the iterator. This is a
        // "concurrent modification" that invalidates the iterator's state.
        list.clear();

        // Assert: Attempting to use the iterator's set() method should now fail.
        // The @Test(expected) annotation handles the exception verification.
        iterator.set(200);
    }
}