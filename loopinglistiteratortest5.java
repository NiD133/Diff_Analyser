package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link LoopingListIterator}.
 * This test focuses on the behavior when the iterator is created with an empty list.
 */
@DisplayName("A LoopingListIterator")
public class LoopingListIteratorTest {

    @Test
    @DisplayName("should handle an empty list correctly")
    void whenConstructedWithEmptyList_thenIteratorIsExhausted() {
        // Arrange: Create an empty list. Using Collections.emptyList() is a clear
        // and efficient way to represent an immutable empty list.
        final List<Object> emptyList = Collections.emptyList();

        // Act: Create a LoopingListIterator for the empty list.
        final LoopingListIterator<Object> iterator = new LoopingListIterator<>(emptyList);

        // Assert: The iterator should immediately report that it is exhausted.
        assertFalse(iterator.hasNext(), "hasNext() should return false for an empty list.");
        assertFalse(iterator.hasPrevious(), "hasPrevious() should return false for an empty list.");

        // Assert: Attempting to access an element should result in an exception.
        assertThrows(NoSuchElementException.class, iterator::next,
            "next() should throw NoSuchElementException for an empty list.");
        assertThrows(NoSuchElementException.class, iterator::previous,
            "previous() should throw NoSuchElementException for an empty list.");
    }
}