package org.apache.commons.collections4.iterators;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.LinkedList;
import java.util.List;

// Note: The class name and inheritance are artifacts from a test generation tool.
// In a typical, human-written test suite, they would be named more conventionally.
public class LoopingListIterator_ESTestTest23 extends LoopingListIterator_ESTest_scaffolding {

    /**
     * Tests that hasPrevious() returns false after the only element in the list is removed.
     * According to the LoopingListIterator Javadoc, hasPrevious() should return false
     * if all elements have been removed.
     */
    @Test(timeout = 4000)
    public void testHasPreviousIsFalseAfterRemovingLastElement() {
        // Arrange: Create a LoopingListIterator with an empty list.
        final List<String> list = new LinkedList<>();
        final LoopingListIterator<String> iterator = new LoopingListIterator<>(list);

        // Act: Add a single element, advance the iterator past it, and then remove it.
        iterator.add("A");
        iterator.next();   // Move to the element to enable removal.
        iterator.remove(); // The list should now be empty.

        // Assert: The iterator should report it has no elements.
        assertFalse("hasPrevious() should be false when the list is empty", iterator.hasPrevious());
        assertFalse("hasNext() should be false when the list is empty", iterator.hasNext());
        assertEquals("Size should be 0 after removing the last element", 0, iterator.size());
    }
}