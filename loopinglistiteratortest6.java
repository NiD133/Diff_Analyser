package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

// Class name improved for clarity and to follow standard conventions.
// The original "LoopingListIteratorTestTest6" was likely a typo.
public class LoopingListIteratorTest {

    /**
     * Tests that a LoopingListIterator loops correctly both forwards and
     * backwards when iterating over a single-element list.
     */
    @Test
    @DisplayName("A looping iterator on a single-element list should loop indefinitely")
    void testLoopingOnSingleElementList() {
        // Arrange: Create a list with a single element and its looping iterator.
        final List<String> list = List.of("a");
        final LoopingListIterator<String> iterator = new LoopingListIterator<>(list);

        // Act & Assert: Test forward looping.
        // The iterator should loop forward multiple times, always returning the single element.
        for (int i = 0; i < 3; i++) {
            assertTrue(iterator.hasNext(), "hasNext() should always be true for a non-empty looping iterator");
            assertEquals("a", iterator.next(), "next() should consistently return the single element on iteration " + (i + 1));
        }

        // Act & Assert: Test backward looping.
        // The iterator should also loop backward multiple times, always returning the single element.
        for (int i = 0; i < 3; i++) {
            assertTrue(iterator.hasPrevious(), "hasPrevious() should always be true for a non-empty looping iterator");
            assertEquals("a", iterator.previous(), "previous() should consistently return the single element on iteration " + (i + 1));
        }
    }
}