package org.apache.commons.collections4.iterators;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

/**
 * Tests for {@link LoopingListIterator}.
 * This test focuses on the behavior of the remove() method.
 */
public class LoopingListIterator_ESTestTest33 extends LoopingListIterator_ESTest_scaffolding {

    /**
     * Tests that calling remove() before a call to next() or previous()
     * results in an IllegalStateException, as per the ListIterator contract.
     */
    @Test(expected = IllegalStateException.class)
    public void shouldThrowIllegalStateExceptionOnRemoveBeforeNextOrPrevious() {
        // Arrange: Create an iterator over an empty list. The state is "before the first element".
        final List<Object> emptyList = new LinkedList<>();
        final LoopingListIterator<Object> iterator = new LoopingListIterator<>(emptyList);

        // Act: Attempt to remove an element without having called next() or previous().
        iterator.remove();

        // Assert: The @Test(expected) annotation verifies that an IllegalStateException is thrown.
    }
}