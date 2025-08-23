package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

// Renamed class for clarity and to follow standard naming conventions.
// The original name "LoopingListIteratorTestTest4" was likely a typo.
public class LoopingListIteratorTest {

    /**
     * Tests the iterator's looping behavior by "jogging" back and forth,
     * repeatedly crossing the list's start and end boundaries.
     */
    @Test
    void testJoggingBackAndForthOverBoundaries() {
        // Arrange: A two-element list and its looping iterator.
        final List<String> list = Arrays.asList("a", "b");
        final LoopingListIterator<String> iterator = new LoopingListIterator<>(list);

        // We will trace the iterator's state using a cursor visualization:
        // |a, b  - cursor is at the start, before 'a'
        // a|b   - cursor is between 'a' and 'b'
        // a, b| - cursor is at the end, after 'b'

        // Assert initial state: The iterator is at the beginning.
        // Cursor: |a, b
        assertEquals(0, iterator.nextIndex(), "Initial: nextIndex should be 0");
        assertEquals(1, iterator.previousIndex(), "Initial: previousIndex should loop to last index (1)");

        // Step 1: Call previous() at the start. It should loop around to the end.
        // Expected return: "b". New cursor position: a|b
        assertEquals("b", iterator.previous(), "previous() at start should loop and return 'b'");
        assertEquals(1, iterator.nextIndex(), "After looping backward, nextIndex should be 1");
        assertEquals(0, iterator.previousIndex(), "After looping backward, previousIndex should be 0");

        // Step 2: Call next(). It should return the element at the current position.
        // Expected return: "b". New cursor position: a, b|
        assertEquals("b", iterator.next(), "next() should return 'b'");
        assertEquals(0, iterator.nextIndex(), "After reaching the end, nextIndex should loop to 0");
        assertEquals(1, iterator.previousIndex(), "After reaching the end, previousIndex should be 1");

        // Step 3: Call previous(). It should return the element before the cursor.
        // Expected return: "b". New cursor position: a|b
        assertEquals("b", iterator.previous(), "previous() from the end should return 'b'");
        assertEquals(1, iterator.nextIndex(), "After moving back from end, nextIndex should be 1");
        assertEquals(0, iterator.previousIndex(), "After moving back from end, previousIndex should be 0");

        // Step 4: Call previous() again.
        // Expected return: "a". New cursor position: |a, b
        assertEquals("a", iterator.previous(), "previous() should return 'a'");
        assertEquals(0, iterator.nextIndex(), "After moving to start, nextIndex should be 0");
        assertEquals(1, iterator.previousIndex(), "After moving to start, previousIndex should loop to 1");

        // Step 5: Call next() from the start.
        // Expected return: "a". New cursor position: a|b
        assertEquals("a", iterator.next(), "next() from the start should return 'a'");
        assertEquals(1, iterator.nextIndex(), "After next() from start, nextIndex should be 1");
        assertEquals(0, iterator.previousIndex(), "After next() from start, previousIndex should be 0");

        // Step 6: Call previous() one last time to return to the start.
        // Expected return: "a". New cursor position: |a, b
        assertEquals("a", iterator.previous(), "previous() should return 'a'");
        assertEquals(0, iterator.nextIndex(), "Final state: nextIndex should be 0");
        assertEquals(1, iterator.previousIndex(), "Final state: previousIndex should be 1");
    }
}