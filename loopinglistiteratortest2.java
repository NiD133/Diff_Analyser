package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link LoopingListIterator}.
 */
class LoopingListIteratorTest {

    @Test
    @DisplayName("Constructor should throw a NullPointerException for a null list")
    void constructorShouldThrowNullPointerExceptionWhenListIsNull() {
        // The constructor contract specifies that a null list is not permissible.
        // This test verifies that the expected NullPointerException is thrown.
        assertThrows(NullPointerException.class,
            () -> new LoopingListIterator<>(null),
            "The constructor must throw a NullPointerException when initialized with a null list.");
    }
}