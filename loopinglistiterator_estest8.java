package org.apache.commons.collections4.iterators;

import org.junit.Test;

import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertThrows;

/**
 * Test class for LoopingListIterator focusing on concurrent modification scenarios.
 */
public class LoopingListIteratorConcurrentModificationTest {

    @Test
    public void iteratorShouldThrowConcurrentModificationExceptionWhenBackingListOfSubListIsModified() {
        // Arrange: Create an iterator from a sublist view of a backing list.
        final LinkedList<Integer> backingList = new LinkedList<>();
        final List<Integer> subListView = backingList.subList(0, 0);
        final LoopingListIterator<Integer> iterator = new LoopingListIterator<>(subListView);

        // Act: Modify the original backing list. This action is expected to invalidate
        // any iterators created from the sublist view.
        backingList.add(42);

        // Assert: Any subsequent operation on the iterator should fail fast by throwing
        // a ConcurrentModificationException.
        assertThrows(ConcurrentModificationException.class, iterator::previousIndex);
    }
}