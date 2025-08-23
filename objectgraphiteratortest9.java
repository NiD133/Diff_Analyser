package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.IllegalStateException;
import java.util.Iterator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for specific edge cases in {@link ObjectGraphIterator}.
 */
class ObjectGraphIteratorTest {

    /**
     * This test verifies that the iterator correctly adheres to the java.util.Iterator
     * contract, which states that calling remove() before a call to next() must
     * throw an IllegalStateException.
     */
    @Test
    @DisplayName("remove() should throw IllegalStateException if next() has not been called")
    void removeShouldThrowIllegalStateExceptionBeforeFirstCallToNext() {
        // Arrange: Create an ObjectGraphIterator.
        // Constructing it with a null root is a simple way to get an instance
        // that has not had next() called on it.
        final Iterator<Object> iterator = new ObjectGraphIterator<>(null);

        // Act & Assert: Verify that calling remove() immediately throws an exception.
        assertThrows(
            IllegalStateException.class,
            iterator::remove,
            "Iterator.remove() should throw IllegalStateException if next() has not been called."
        );
    }
}