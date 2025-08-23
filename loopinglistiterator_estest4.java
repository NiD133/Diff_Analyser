package org.apache.commons.collections4.iterators;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

/**
 * Contains improved tests for the {@link LoopingListIterator}.
 * This class focuses on demonstrating specific behaviors in a clear and understandable manner.
 */
public class LoopingListIteratorImprovedTest {

    /**
     * Tests that calling add() on the iterator correctly inserts an element
     * and updates the position for the next call to nextIndex().
     */
    @Test
    public void testNextIndexIsCorrectAfterAddingElement() {
        // Arrange: Create a list with one element and an iterator for it.
        List<String> list = new LinkedList<>();
        list.add("initialElement");
        LoopingListIterator<String> iterator = new LoopingListIterator<>(list);

        // The iterator is initially positioned before "initialElement".
        // A call to next() would return "initialElement" at index 0.
        assertEquals("Pre-condition: nextIndex should be 0", 0, iterator.nextIndex());

        // Act: Add a new element using the iterator.
        // Per the ListIterator#add contract, this inserts the element immediately
        // before the element that would be returned by next().
        iterator.add("newElement");

        // The underlying list is now ["newElement", "initialElement"].
        // The iterator's cursor is now between the two elements.

        // Assert: The next element to be returned is "initialElement", which is now at index 1.
        final int nextIndex = iterator.nextIndex();
        assertEquals("The next index should point to the original element's new position.", 1, nextIndex);

        // For added clarity, verify the final state of the underlying list.
        assertEquals("List size should be 2 after add.", 2, list.size());
        assertEquals("The new element should be at index 0.", "newElement", list.get(0));
        assertEquals("The original element should be at index 1.", "initialElement", list.get(1));
    }
}