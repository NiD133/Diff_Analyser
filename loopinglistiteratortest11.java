package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * A collection of tests for the {@link LoopingListIterator#reset()} method.
 * This suite is structured to test the reset functionality from various iterator states.
 */
@DisplayName("LoopingListIterator.reset()")
class LoopingListIteratorResetTest {

    private List<String> elements;
    private LoopingListIterator<String> iterator;

    @BeforeEach
    void setUp() {
        elements = List.of("a", "b", "c");
        iterator = new LoopingListIterator<>(elements);
    }

    @Test
    @DisplayName("should restart from the beginning after iterating forward")
    void resetAfterIteratingForward() {
        // Arrange: Move the iterator forward.
        iterator.next(); // returns "a"
        iterator.next(); // returns "b", cursor is now before "c"
        assertEquals(2, iterator.nextIndex(), "Pre-condition: iterator should be positioned before 'c'");

        // Act: Reset the iterator.
        iterator.reset();

        // Assert: The iterator is back at the start.
        assertEquals(0, iterator.nextIndex(), "After reset, nextIndex should be 0");
        assertEquals("a", iterator.next(), "After reset, next() should return the first element");
    }

    @Test
    @DisplayName("should restart from the beginning after iterating backward")
    void resetAfterIteratingBackward() {
        // Arrange: Move the iterator backward, causing it to loop to the end.
        iterator.previous(); // returns "c", cursor is now before "c"
        iterator.previous(); // returns "b", cursor is now before "b"
        assertEquals(1, iterator.nextIndex(), "Pre-condition: iterator should be positioned before 'b'");

        // Act: Reset the iterator.
        iterator.reset();

        // Assert: The iterator is back at the start.
        assertEquals(0, iterator.nextIndex(), "After reset, nextIndex should be 0");
        assertEquals("a", iterator.next(), "After reset, next() should return the first element");
    }

    @Test
    @DisplayName("should restore the iterator to its initial state")
    void resetRestoresInitialState() {
        // Arrange: Move the iterator to an arbitrary position.
        iterator.next(); // returns "a"
        iterator.next(); // returns "b"

        // Act: Reset the iterator.
        iterator.reset();

        // Assert: The iterator's state is identical to a newly created one.
        assertAll("Iterator state after reset",
            () -> assertEquals(0, iterator.nextIndex(), "nextIndex should be 0"),
            () -> assertEquals(elements.size() - 1, iterator.previousIndex(), "previousIndex should be the last index"),
            () -> assertTrue(iterator.hasNext(), "hasNext() should be true"),
            () -> assertTrue(iterator.hasPrevious(), "hasPrevious() should be true")
        );
    }

    @Test
    @DisplayName("should allow full backward iteration after a reset")
    void resetAllowsFullBackwardIteration() {
        // Arrange: Move the iterator forward to a non-start position.
        iterator.next(); // "a"
        iterator.next(); // "b"

        // Act: Reset the iterator.
        iterator.reset();

        // Assert: previous() loops around and can traverse the entire list backward.
        assertEquals("c", iterator.previous(), "First previous() after reset should be the last element");
        assertEquals("b", iterator.previous(), "Second previous() should be the second-to-last element");
        assertEquals("a", iterator.previous(), "Third previous() should be the first element");
    }

    @Test
    @DisplayName("should behave consistently across multiple resets")
    void multipleResetsAreConsistent() {
        // Arrange: Move the iterator forward.
        iterator.next(); // "a"
        iterator.next(); // "b"

        // Act & Assert: First reset.
        iterator.reset();
        assertEquals("a", iterator.next(), "next() after first reset should return the first element");

        // Arrange: Move the iterator to a different position.
        iterator.next(); // "b"
        iterator.next(); // "c"

        // Act & Assert: Second reset.
        iterator.reset();
        assertEquals("a", iterator.next(), "next() after second reset should also return the first element");
    }
}