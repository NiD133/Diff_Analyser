package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link LoopingListIterator} focusing on the interaction
 * between the next() and previous() methods.
 */
// The original class name "LoopingListIteratorTestTest3" was corrected to follow standard conventions.
class LoopingListIteratorTest {

    /**
     * Tests the interaction between next() and previous() calls.
     * <p>
     * This test simulates "jogging" back and forth between elements to ensure
     * that the iterator behaves correctly, i.e., that a call to previous()
     * after next() returns the same element. The test also verifies the
     * iterator's state (cursor position) using assertions on nextIndex() and
     * previousIndex(), which is crucial for a looping iterator.
     */
    @Test
    void testJoggingBackAndForth() {
        // Arrange: Create a list with two elements and a looping iterator for it.
        final List<String> list = Arrays.asList("a", "b");
        final LoopingListIterator<String> iterator = new LoopingListIterator<>(list);

        // The test follows a sequence of next() and previous() calls,
        // asserting the returned element and the iterator's state after each call.

        // --- Scenario 1: Move forward, then immediately backward ---

        // Action: Move forward from the start.
        // State before: |a, b. nextIndex=0, previousIndex=1 (loops)
        assertEquals("a", iterator.next());

        // Assert state after moving forward.
        // State after: a| b
        assertEquals(1, iterator.nextIndex(), "Cursor should be after 'a'");
        assertEquals(0, iterator.previousIndex(), "Cursor should be after 'a'");

        // Action: Move backward. Should return the same element.
        assertEquals("a", iterator.previous());

        // Assert state after moving backward.
        // State after: |a, b
        assertEquals(0, iterator.nextIndex(), "Cursor should be back at the start");
        assertEquals(1, iterator.previousIndex(), "previousIndex should loop to the end from the start");

        // --- Scenario 2: Move forward twice, then backward once ---

        // Action: Move forward past 'a', then 'b'.
        assertEquals("a", iterator.next()); // State: a| b
        assertEquals("b", iterator.next()); // State: a, b|

        // Assert state at the end of the list.
        // State after: a, b|. Cursor is at the end, so indices loop.
        assertEquals(0, iterator.nextIndex(), "nextIndex should loop to the start");
        assertEquals(1, iterator.previousIndex(), "previousIndex should point to the last element");

        // Action: Move backward from the end.
        assertEquals("b", iterator.previous());

        // Assert state after moving backward.
        // State after: a| b
        assertEquals(1, iterator.nextIndex(), "Cursor should be between 'a' and 'b'");
        assertEquals(0, iterator.previousIndex(), "Cursor should be between 'a' and 'b'");

        // --- Final check: Move forward again from the middle ---
        assertEquals("b", iterator.next());

        // Assert state at the end of the list again.
        // State after: a, b|
        assertEquals(0, iterator.nextIndex(), "nextIndex should loop to the start again");
        assertEquals(1, iterator.previousIndex(), "previousIndex should be the last element again");
    }
}