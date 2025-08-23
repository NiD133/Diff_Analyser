package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for LoopingListIterator focusing on the behavior of
 * nextIndex() and previousIndex() during various traversal scenarios.
 */
@DisplayName("LoopingListIterator Indexing")
class LoopingListIteratorTest {

    private LoopingListIterator<String> iterator;
    private final List<String> list = List.of("a", "b", "c");

    @BeforeEach
    void setUp() {
        iterator = new LoopingListIterator<>(list);
    }

    @Test
    @DisplayName("should report correct indices at initial state")
    void indicesShouldBeCorrectAtInitialState() {
        // For a list [a, b, c], the initial state is before 'a'.
        // State: [<a>, b, c]
        // next() would return 'a' (index 0).
        // previous() would wrap around and return 'c' (index 2).
        assertAll("Initial indices",
            () -> assertEquals(0, iterator.nextIndex(), "nextIndex should point to the first element (0)"),
            () -> assertEquals(2, iterator.previousIndex(), "previousIndex should point to the last element (2)")
        );
    }

    @Test
    @DisplayName("should update indices correctly after calling next()")
    void indicesShouldUpdateAfterNext() {
        // Arrange: Initial state is [<a>, b, c]
        // Act
        iterator.next(); // Returns "a", moves cursor.

        // Assert: New state is [a, <b>, c]
        // next() would return 'b' (index 1).
        // previous() would return 'a' (index 0).
        assertAll("Indices after one next() call",
            () -> assertEquals(1, iterator.nextIndex(), "nextIndex should advance to 1"),
            () -> assertEquals(0, iterator.previousIndex(), "previousIndex should now be 0")
        );
    }

    @Test
    @DisplayName("should revert indices after calling next() then previous()")
    void indicesShouldRevertAfterNextThenPrevious() {
        // Arrange: Initial state is [<a>, b, c]
        // Act
        iterator.next();     // State: [a, <b>, c]
        iterator.previous(); // State: [<a>, b, c]

        // Assert: The iterator is back at the initial state.
        assertAll("Indices after returning to start",
            () -> assertEquals(0, iterator.nextIndex(), "nextIndex should return to 0"),
            () -> assertEquals(2, iterator.previousIndex(), "previousIndex should return to 2")
        );
    }

    @Nested
    @DisplayName("when traversing backwards")
    class BackwardTraversalTests {

        @Test
        @DisplayName("should wrap around to the end when previous() is called at the start")
        void indicesShouldWrapAroundWhenCallingPreviousAtStart() {
            // Arrange: Initial state is [<a>, b, c]
            // Act
            iterator.previous(); // Returns "c", wraps cursor to the end.

            // Assert: New state is [a, b, <c>]
            // next() would return 'c' (index 2).
            // previous() would return 'b' (index 1).
            assertAll("Indices after wrapping backwards",
                () -> assertEquals(2, iterator.nextIndex(), "nextIndex should point to the last element (2)"),
                () -> assertEquals(1, iterator.previousIndex(), "previousIndex should point to the second to last element (1)")
            );
        }

        @Test
        @DisplayName("should update indices correctly through a full backward loop")
        void indicesShouldUpdateCorrectlyDuringFullBackwardLoop() {
            // This test covers a full backward traversal starting from the initial position.
            // Arrange: Initial state is [<a>, b, c]

            // Act & Assert: Step 1 - Call previous(), wraps around to the end.
            iterator.previous(); // State: [a, b, <c>]
            assertEquals(2, iterator.nextIndex());
            assertEquals(1, iterator.previousIndex());

            // Act & Assert: Step 2 - Call previous() again, moves to the middle.
            iterator.previous(); // State: [a, <b>, c]
            assertEquals(1, iterator.nextIndex());
            assertEquals(0, iterator.previousIndex());

            // Act & Assert: Step 3 - Call previous() again, moves back to the start.
            iterator.previous(); // State: [<a>, b, c]
            assertEquals(0, iterator.nextIndex());
            assertEquals(2, iterator.previousIndex());
        }
    }
}