package org.apache.commons.collections4.iterators;

import org.junit.Test;

import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.List;

/**
 * Tests the fail-fast behavior of LoopingListIterator when the underlying collection is modified.
 * This scenario specifically tests the iterator's behavior when it is created from a
 * sublist view, and the original backing list is subsequently modified.
 */
public class LoopingListIteratorTest { // Renamed from LoopingListIterator_ESTestTest12 for clarity

    /**
     * Verifies that the iterator throws a ConcurrentModificationException if the backing
     * list is modified after the iterator's creation. This is standard "fail-fast"
     * behavior inherited from the underlying list's iterator.
     */
    @Test(expected = ConcurrentModificationException.class)
    public void hasPreviousShouldThrowConcurrentModificationExceptionWhenBackingListIsModified() {
        // Arrange: Create an iterator over an empty sublist view of a backing list.
        final List<Integer> backingList = new LinkedList<>();
        final List<Integer> subListView = backingList.subList(0, 0);
        final LoopingListIterator<Integer> iterator = new LoopingListIterator<>(subListView);

        // Act: Modify the original backing list directly. This structural modification
        // is expected to invalidate any iterators created from its sublist views.
        backingList.add(100);

        // Assert: The subsequent call to hasPrevious() is expected to throw the exception.
        iterator.hasPrevious();
    }
}