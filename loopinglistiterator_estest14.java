package org.apache.commons.collections4.iterators;

import static org.junit.Assert.assertThrows;

import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.List;
import org.junit.Test;

/**
 * Tests for {@link LoopingListIterator} focusing on concurrent modification scenarios.
 */
public class LoopingListIteratorConcurrentModificationTest {

    /**
     * Tests that the constructor throws a ConcurrentModificationException if it is
     * given a subList whose backing list has been structurally modified.
     * <p>
     * The LoopingListIterator constructor internally calls list.listIterator(),
     * which, for a subList, checks for concurrent modifications of the parent list.
     */
    @Test
    public void constructorShouldThrowCMEWhenBackingListOfSubListIsModified() {
        // Arrange: Create a list and a subList view of it.
        final List<Integer> originalList = new LinkedList<>();
        final List<Integer> subListView = originalList.subList(0, 0);

        // Act: Structurally modify the original list. This invalidates the subList view.
        originalList.add(100);

        // Assert: Creating an iterator from the invalidated subList view should fail.
        assertThrows(ConcurrentModificationException.class, () -> {
            new LoopingListIterator<>(subListView);
        });
    }
}