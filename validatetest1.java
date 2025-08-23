package org.jsoup.helper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for the {@link Validate} helper class.
 */
class ValidateTest {

    @Test
    @DisplayName("notNull should not throw an exception for a non-null object")
    void notNull_withNonNullObject_doesNotThrow() {
        // The "happy path" where the input is valid.
        // The method should complete without throwing an exception.
        assertDoesNotThrow(() -> Validate.notNull("foo"));
    }

    @Test
    @DisplayName("notNull should throw IllegalArgumentException for a null object")
    void notNull_withNullObject_throwsException() {
        // The failure case where the input is invalid (null).
        // We expect an IllegalArgumentException to be thrown.
        assertThrows(IllegalArgumentException.class, () -> Validate.notNull(null));
    }
}