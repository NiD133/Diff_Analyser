package org.apache.commons.collections4.iterators;

import org.junit.Test;
import java.util.LinkedList;
import java.util.List;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link LoopingListIterator} class.
 */
public class LoopingListIteratorTest {

    /**
     * Verifies that hasNext() returns true after an element is added to an
     * initially empty iterator.
     *
     * The LoopingListIterator should correctly reflect changes made to the
     * underlying list via its own add() method.
     */
    @Test
    public void testHasNextBecomesTrueAfterAddingToEmptyIterator() {
        // Arrange: Create an empty list and a LoopingListIterator for it.
        final List<String> list = new LinkedList<>();
        final LoopingListIterator<String> iterator = new LoopingListIterator<>(list);

        // Assert: Initially, the iterator for an empty list should have no next element.
        assertFalse("An iterator over an empty list should not have a next element.", iterator.hasNext());

        // Act: Add an element to the list using the iterator's add() method.
        iterator.add("one");

        // Assert: After adding an element, the iterator should now have a next element.
        assertTrue("The iterator should have a next element after one was added.", iterator.hasNext());
    }
}