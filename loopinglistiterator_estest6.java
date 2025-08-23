package org.apache.commons.collections4.iterators;

import org.junit.Test;

import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.List;

/**
 * Tests for {@link LoopingListIterator} focusing on concurrent modification scenarios.
 */
public class LoopingListIteratorTest {

    /**
     * Tests that the iterator fails fast with a ConcurrentModificationException
     * if the underlying list is modified externally.
     * <p>
     * This scenario is demonstrated by creating an iterator over a {@link List#subList(int, int)} view.
     * When the backing list of the sublist is modified, any subsequent operation on the
     * iterator (like {@link LoopingListIterator#reset()}) is expected to throw a
     * {@link ConcurrentModificationException}.
     */
    @Test(expected = ConcurrentModificationException.class)
    public void testResetThrowsCMEWhenBackingListIsModified() {
        // Arrange: Create an iterator from a sublist view of a backing list.
        final LinkedList<Integer> backingList = new LinkedList<>();
        final List<Integer> sublistView = backingList.subList(0, 0);
        final LoopingListIterator<Integer> iterator = new LoopingListIterator<>(sublistView);

        // Act: Modify the backing list directly. This invalidates the sublist view
        // and, by extension, the iterator that depends on it.
        backingList.add(42);

        // Assert: Calling reset() on the iterator should now throw a
        // ConcurrentModificationException because the underlying collection was modified.
        iterator.reset();
    }
}