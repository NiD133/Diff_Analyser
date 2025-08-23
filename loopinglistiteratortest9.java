package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link LoopingListIterator} focusing on element removal.
 */
@DisplayName("LoopingListIterator.remove()")
class LoopingListIteratorTest {

    private List<String> list;
    private LoopingListIterator<String> loopingIterator;

    @BeforeEach
    void setUp() {
        list = new ArrayList<>(Arrays.asList("a", "b", "c"));
        loopingIterator = new LoopingListIterator<>(list);
    }

    @Test
    @DisplayName("should correctly remove all elements while iterating backwards")
    void removeAfterPreviousShouldCorrectlyModifyListAndIteratorState() {
        // Sanity check: The iterator should be able to loop backwards from the start.
        assertTrue(loopingIterator.hasPrevious(), "Iterator should have a previous element on a non-empty list");

        // Step 1: Move backward from the start, which loops to the end, and remove "c".
        // State before: list is ["a", "b", "c"], iterator is at index 0.
        assertEquals("c", loopingIterator.previous(), "previous() should loop to the end and return the last element");
        loopingIterator.remove();
        // State after: list is ["a", "b"], iterator is now at the end of the modified list.
        assertEquals(Arrays.asList("a", "b"), list, "List should contain [a, b] after removing 'c'");
        assertEquals(2, list.size());

        // Step 2: Move backward again and remove "b".
        assertTrue(loopingIterator.hasPrevious(), "Iterator should still have a previous element");
        assertEquals("b", loopingIterator.previous(), "previous() should now return 'b'");
        loopingIterator.remove();
        // State after: list is ["a"], iterator is at the end of the modified list.
        assertEquals(Collections.singletonList("a"), list, "List should contain [a] after removing 'b'");
        assertEquals(1, list.size());

        // Step 3: Move backward one last time and remove "a".
        assertTrue(loopingIterator.hasPrevious(), "Iterator should still have a previous element");
        assertEquals("a", loopingIterator.previous(), "previous() should now return 'a'");
        loopingIterator.remove();
        // State after: list is [], iterator is over an empty list.
        assertTrue(list.isEmpty(), "List should be empty after removing all elements");

        // Step 4: Verify behavior on the now-empty list.
        assertFalse(loopingIterator.hasPrevious(), "hasPrevious() should be false for an empty list");
        assertThrows(NoSuchElementException.class, loopingIterator::previous,
            "previous() should throw NoSuchElementException for an empty list");
    }
}