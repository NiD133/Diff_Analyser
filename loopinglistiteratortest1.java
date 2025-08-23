package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the add() method of the LoopingListIterator.
 *
 * This class focuses on verifying the correctness of adding elements
 * to the list via the iterator in various scenarios.
 */
@DisplayName("LoopingListIterator.add()")
class LoopingListIteratorAddTest {

    private List<String> list;
    private LoopingListIterator<String> iterator;

    @BeforeEach
    void setUp() {
        // Initial state for most tests: [b, e, f]
        list = new ArrayList<>(Arrays.asList("b", "e", "f"));
        iterator = new LoopingListIterator<>(list);
    }

    @Test
    @DisplayName("Should insert an element at the beginning of the list")
    void addShouldInsertElementAtStart() {
        // Arrange: Iterator is at the start, before "b".
        // The list is [b, e, f].

        // Act: Add "a" at the current position.
        iterator.add("a");

        // Assert: The list is now [a, b, e, f].
        // The iterator is positioned between the new element "a" and "b".
        assertEquals(Arrays.asList("a", "b", "e", "f"), list);
        assertEquals("b", iterator.next(), "next() should return the element after the newly added one.");
    }

    @Test
    @DisplayName("Should insert an element in the middle of the list")
    void addShouldInsertElementInMiddle() {
        // Arrange: Move iterator to the middle.
        // The list is [b, e, f].
        iterator.next(); // cursor after "b"

        // Act: Add "c" at the current position.
        iterator.add("c");

        // Assert: The list is now [b, c, e, f].
        // The iterator is positioned between "c" and "e".
        assertEquals(Arrays.asList("b", "c", "e", "f"), list);
        assertEquals("e", iterator.next(), "next() should return the element after the newly added one.");
    }

    @Test
    @DisplayName("Should allow navigation with previous() after an add")
    void addThenPreviousShouldNavigateCorrectly() {
        // Arrange: Move iterator to the middle.
        // The list is [b, e, f].
        iterator.next(); // cursor after "b"

        // Act: Add "c". The list becomes [b, c, e, f].
        // The iterator is now between "c" and "e".
        iterator.add("c");

        // Assert: previous() should return the newly added element, then the one before it.
        assertEquals("c", iterator.previous());
        assertEquals("b", iterator.previous());
    }

    @Test
    @DisplayName("Should correctly loop with previous() after adding at the start")
    void addAtStartThenPreviousShouldLoopToEnd() {
        // Arrange: Iterator is at the start.
        // The list is [b, e, f].

        // Act: Add "a". The list becomes [a, b, e, f].
        // The iterator is now between "a" and "b".
        iterator.add("a");

        // Assert: previous() should loop around to the end of the modified list.
        assertEquals("f", iterator.previous(), "previous() should loop to the last element.");
        assertEquals("e", iterator.previous());
    }
    
    @Test
    @DisplayName("Should reflect additions after a reset")
    void resetShouldAcknowledgeAddedElements() {
        // Arrange: Add an element "a" at the start.
        // The list is [b, e, f].
        iterator.add("a"); // list is now [a, b, e, f]
        iterator.next();   // cursor after "b"

        // Act: Reset the iterator.
        iterator.reset();

        // Assert: The iterator is at the start of the modified list.
        assertEquals("a", iterator.next(), "After reset, next() should return the first element of the modified list.");
        assertEquals(Arrays.asList("a", "b", "e", "f"), list);
    }

    @Test
    @DisplayName("Should allow a full forward loop after multiple additions")
    void fullForwardLoopShouldWorkAfterMultipleAdds() {
        // Arrange: Add multiple elements at different positions.
        // The list is [b, e, f].
        iterator.add("a");      // list: [a, b, e, f], cursor before "b"
        iterator.next();        // returns "b", cursor before "e"
        iterator.add("c");      // list: [a, b, c, e, f], cursor before "e"
        iterator.next();        // returns "e", cursor before "f"
        iterator.add("d");      // list: [a, b, c, d, e, f], cursor before "f"
        
        // Act: Reset to start iterating from the beginning.
        iterator.reset();

        // Assert: The final list is correct.
        final List<String> expectedList = Arrays.asList("a", "b", "c", "d", "e", "f");
        assertEquals(expectedList, list);

        // Assert: A full loop traverses all elements in the correct order and then loops.
        assertEquals("a", iterator.next());
        assertEquals("b", iterator.next());
        assertEquals("c", iterator.next());
        assertEquals("d", iterator.next());
        assertEquals("e", iterator.next());
        assertEquals("f", iterator.next());
        assertEquals("a", iterator.next(), "Iterator should loop back to the start.");
    }
}