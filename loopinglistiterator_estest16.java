package org.apache.commons.collections4.iterators;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

/**
 * Contains tests for the LoopingListIterator class.
 * This is a cleaned-up version of a single test case.
 */
public class LoopingListIteratorTest {

    /**
     * Tests that after adding an element to an empty list via the iterator,
     * previousIndex() correctly returns 0.
     */
    @Test
    public void previousIndexShouldBeZeroAfterAddingFirstElement() {
        // Arrange: Create a LoopingListIterator for an empty list.
        final List<String> list = new LinkedList<>();
        final LoopingListIterator<String> iterator = new LoopingListIterator<>(list);

        // Act: Add an element to the list using the iterator.
        // According to the ListIterator#add contract, the cursor is now positioned
        // immediately after the new element.
        iterator.add("Element 1");

        // Assert: The previousIndex() should be the index of the element that was just added (0).
        assertEquals("The previous index should point to the newly added element.", 0, iterator.previousIndex());
    }
}