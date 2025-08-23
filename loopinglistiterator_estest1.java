package org.apache.commons.collections4.iterators;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;

/**
 * Tests for {@link LoopingListIterator}.
 */
public class LoopingListIteratorTest {

    @Test
    public void testSetAfterPreviousWrapAroundShouldNotExhaustIterator() {
        // Arrange: Create a list with a single element and a looping iterator for it.
        final List<Integer> list = new LinkedList<>();
        list.add(100);
        final LoopingListIterator<Integer> iterator = new LoopingListIterator<>(list);

        // Act:
        // 1. Call previous() on the new iterator. This wraps around to the end of the
        //    list and makes the single element the "last returned element".
        iterator.previous();
        // 2. Replace the last returned element (100) with null.
        iterator.set(null);

        // Assert:
        // The core assertion is that the iterator is still valid and can continue.
        // Because the list is not empty, a looping iterator should always have a previous element.
        assertTrue("Iterator should still have a previous element after set()", iterator.hasPrevious());

        // Also, verify the side-effects on the underlying list for completeness.
        assertEquals("List size should remain unchanged", 1, list.size());
        assertNull("The element in the list should have been updated to null", list.get(0));
    }
}