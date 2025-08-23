package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Iterator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the iterator of {@link CharRange}.
 *
 * <p>This class was renamed from the original {@code CharRangeTestTest16}
 * to better reflect its specific focus on the iterator's behavior.</p>
 */
class CharRangeIteratorTest {

    /**
     * Verifies that the iterator's remove() method throws an
     * UnsupportedOperationException. This is the correct behavior because
     * CharRange is an immutable class, and its iterator should not
     * permit modifications.
     */
    @Test
    @DisplayName("Iterator.remove() should throw UnsupportedOperationException")
    void iteratorRemoveShouldThrowException() {
        // Arrange: Create a simple CharRange and get its iterator.
        final CharRange range = CharRange.is('a');
        final Iterator<Character> iterator = range.iterator();

        // Act & Assert: Attempting to call remove() on the iterator should fail.
        assertThrows(UnsupportedOperationException.class,
            iterator::remove,
            "The iterator for an immutable CharRange should not support the remove operation.");
    }
}