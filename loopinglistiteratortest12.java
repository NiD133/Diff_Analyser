package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;

public class LoopingListIteratorTestTest12 {

    /**
     * Tests that set() correctly replaces the last element returned by next() or previous()
     * and that the underlying list is modified accordingly.
     */
    @Test
    void testSetReplacesLastReturnedElementAndUpdatesList() {
        // Arrange: Create a mutable list and the looping iterator.
        // The list is ["q", "r", "z"] and the iterator is at the start.
        final List<String> list = new ArrayList<>(Arrays.asList("q", "r", "z"));
        final LoopingListIterator<String> iterator = new LoopingListIterator<>(list);

        // --- Scenario 1: Set after previous() ---

        // Act: Call previous() to wrap around and get the last element.
        assertEquals("z", iterator.previous(), "previous() should wrap around and return the last element");

        // Act: Replace the last returned element ("z") with "c".
        iterator.set("c");

        // Assert: The underlying list is now ["q", "r", "c"].
        assertEquals(Arrays.asList("q", "r", "c"), list, "List should be updated after set() following previous()");

        // --- Scenario 2: Set after next() ---

        // Act: Reset iterator and get the first element.
        iterator.reset();
        assertEquals("q", iterator.next(), "next() should return the first element after reset");

        // Act: Replace the last returned element ("q") with "a".
        iterator.set("a");

        // Assert: The underlying list is now ["a", "r", "c"].
        assertEquals(Arrays.asList("a", "r", "c"), list, "List should be updated after set() following next()");

        // --- Scenario 3: Set after a subsequent next() ---

        // Act: Get the next element in the modified list.
        assertEquals("r", iterator.next(), "next() should return the second element");

        // Act: Replace the last returned element ("r") with "b".
        iterator.set("b");

        // Assert: The underlying list is now ["a", "b", "c"].
        assertEquals(Arrays.asList("a", "b", "c"), list, "List should be updated after a second set() call");

        // --- Verification: Iterate over the final list ---

        // Act: Reset and iterate through the fully modified list.
        iterator.reset();

        // Assert: The iterator correctly traverses the modified list and loops.
        assertEquals("a", iterator.next());
        assertEquals("b", iterator.next());
        assertEquals("c", iterator.next());
        assertEquals("a", iterator.next(), "Iterator should loop back to the start");
    }
}