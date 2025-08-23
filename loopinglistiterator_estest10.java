package org.apache.commons.collections4.iterators;

import org.junit.Test;

import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.List;

/**
 * Tests for {@link LoopingListIterator} focusing on its behavior with
 * concurrent modifications.
 */
public class LoopingListIteratorConcurrentModificationTest {

    /**
     * This test verifies that the iterator fails fast with a
     * {@link ConcurrentModificationException} if the underlying list is modified
     * after the iterator has been created.
     *
     * This scenario is tested using a {@link List#subList(int, int)} view, as modifications
     * to the backing list are a common source of such exceptions.
     */
    @Test(expected = ConcurrentModificationException.class)
    public void shouldThrowConcurrentModificationExceptionWhenBackingListIsModified() {
        // Arrange: Create an iterator over a sublist view of an empty list.
        final LinkedList<Integer> backingList = new LinkedList<>();
        final List<Integer> subListView = backingList.subList(0, 0);
        final LoopingListIterator<Integer> iterator = new LoopingListIterator<>(subListView);

        // Act: Modify the backing list directly. This action invalidates any
        // iterators that were created from its sublist view.
        backingList.add(42);

        // Assert: Calling any method on the iterator should now throw.
        // The @Test(expected=...) annotation asserts that a
        // ConcurrentModificationException is thrown.
        iterator.nextIndex();
    }
}