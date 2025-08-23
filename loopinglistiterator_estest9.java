package org.apache.commons.collections4.iterators;

import org.junit.Test;

import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.List;

/**
 * Unit tests for {@link LoopingListIterator} to verify its behavior
 * regarding concurrent modifications.
 */
public class LoopingListIteratorTest {

    /**
     * Tests that a call to previous() throws a ConcurrentModificationException
     * if the underlying list is modified externally after the iterator is created.
     */
    @Test(expected = ConcurrentModificationException.class)
    public void previousShouldThrowConcurrentModificationExceptionWhenListIsModifiedExternally() {
        // Arrange: Create an iterator for an empty list.
        final List<Object> list = new LinkedList<>();
        final LoopingListIterator<Object> iterator = new LoopingListIterator<>(list);

        // Act: Modify the underlying list directly, not through the iterator.
        // This invalidates the iterator's state.
        list.add("an external modification");

        // Assert: The next operation on the iterator is expected to fail.
        iterator.previous();
    }
}