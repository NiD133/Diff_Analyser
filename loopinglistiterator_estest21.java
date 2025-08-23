package org.apache.commons.collections4.iterators;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

/**
 * Contains tests for the {@link LoopingListIterator}.
 * This class focuses on improving a specific auto-generated test case.
 */
public class LoopingListIterator_ESTestTest21 extends LoopingListIterator_ESTest_scaffolding {

    /**
     * Tests that nextIndex() returns 0 after an element is added to an
     * initially empty list.
     * <p>
     * When an element is added via ListIterator.add(), the cursor is positioned
     * immediately after the new element. For a looping iterator with a single
     * element, the "next" element is the one at the beginning of the list (index 0).
     */
    @Test
    public void testNextIndexIsZeroAfterAddingFirstElement() {
        // Arrange: Create a LoopingListIterator with an empty list.
        final List<String> list = new LinkedList<>();
        final LoopingListIterator<String> loopingIterator = new LoopingListIterator<>(list);

        // Act: Add an element to the list via the iterator.
        loopingIterator.add("first_element");
        final int nextIndex = loopingIterator.nextIndex();

        // Assert: The next index should be 0, as the iterator loops back to the start.
        assertEquals("After adding the first element, nextIndex() should be 0.", 0, nextIndex);
    }
}