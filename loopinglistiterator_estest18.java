package org.apache.commons.collections4.iterators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.LinkedList;
import java.util.List;
import org.junit.Test;

/**
 * Contains improved, understandable test cases for the {@link LoopingListIterator}.
 */
public class LoopingListIteratorTest {

    /**
     * Tests that calling {@code previous()} immediately after {@code add()}
     * returns the element that was just added.
     *
     * The {@code add} operation places the new element before the cursor, and
     * a subsequent call to {@code previous} should return that new element.
     */
    @Test
    public void addThenPreviousShouldReturnTheAddedElement() {
        // Arrange: Create an empty list and a looping iterator for it.
        final List<Object> list = new LinkedList<>();
        final LoopingListIterator<Object> iterator = new LoopingListIterator<>(list);

        // The element to be added. In this case, it's the list itself,
        // which is a valid, though unusual, scenario to test.
        final Object elementToAdd = list;

        // Act: Add the element via the iterator. The iterator's cursor is now
        // positioned immediately after the new element.
        iterator.add(elementToAdd);
        
        // Call previous(), which should move the cursor back and return the element.
        final Object returnedElement = iterator.previous();

        // Assert: Verify the state of the list and the returned element.
        // The list should now contain exactly one element.
        assertEquals("The list size should be 1 after adding an element.", 1, list.size());
        
        // The element returned by previous() must be the same instance that was added.
        assertSame("previous() should return the same element that was just added.", elementToAdd, returnedElement);
    }
}