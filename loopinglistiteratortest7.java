package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for LoopingListIterator focusing on its looping and reset behaviors.
 */
@DisplayName("A LoopingListIterator")
class LoopingListIteratorTest {

    private final List<String> list = Arrays.asList("a", "b");
    private LoopingListIterator<String> iterator;

    @BeforeEach
    void setUp() {
        iterator = new LoopingListIterator<>(list);
    }

    @Nested
    @DisplayName("when iterating over a two-element list")
    class TwoElementListTests {

        @Test
        @DisplayName("should loop forward continuously")
        void testNextLoopsForward() {
            // The list is ["a", "b"]. The iterator should cycle through a, b, a, b, ...

            // First pass
            assertTrue(iterator.hasNext(), "Iterator should have a next element.");
            assertEquals("a", iterator.next(), "First call to next() should return 'a'.");
            assertTrue(iterator.hasNext(), "Iterator should have a next element.");
            assertEquals("b", iterator.next(), "Second call to next() should return 'b'.");

            // Should loop back to the start
            assertTrue(iterator.hasNext(), "Iterator should have a next element after reaching the end.");
            assertEquals("a", iterator.next(), "Third call to next() should loop back and return 'a'.");
            assertTrue(iterator.hasNext(), "Iterator should have a next element.");
            assertEquals("b", iterator.next(), "Fourth call to next() should return 'b'.");
        }

        @Test
        @DisplayName("should loop backward continuously")
        void testPreviousLoopsBackward() {
            // The list is ["a", "b"]. Calling previous() from the start should wrap to the end.
            // The iterator should cycle through b, a, b, a, ...

            // First pass (wraps around)
            assertTrue(iterator.hasPrevious(), "Iterator should have a previous element.");
            assertEquals("b", iterator.previous(), "First call to previous() should loop to the end and return 'b'.");
            assertTrue(iterator.hasPrevious(), "Iterator should have a previous element.");
            assertEquals("a", iterator.previous(), "Second call to previous() should return 'a'.");

            // Should loop back to the end again
            assertTrue(iterator.hasPrevious(), "Iterator should have a previous element after reaching the start.");
            assertEquals("b", iterator.previous(), "Third call to previous() should loop back and return 'b'.");
            assertTrue(iterator.hasPrevious(), "Iterator should have a previous element.");
            assertEquals("a", iterator.previous(), "Fourth call to previous() should return 'a'.");
        }

        @Test
        @DisplayName("should return to the start after reset")
        void testReset() {
            // Arrange: Advance the iterator past the first element.
            iterator.next(); // Returns "a"
            assertEquals("b", iterator.next(), "Iterator should be at the second element.");

            // Act: Reset the iterator.
            iterator.reset();

            // Assert: The iterator is back at the start of the list.
            assertEquals("a", iterator.next(), "After reset, next() should return the first element.");
        }
    }
}