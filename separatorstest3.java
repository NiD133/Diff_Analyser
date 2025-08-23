package com.fasterxml.jackson.core.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link Separators} class, focusing on the immutability
 * and behavior of its 'with...' methods.
 */
@DisplayName("Separators immutability")
class SeparatorsTest {

    @Test
    @DisplayName("withObjectFieldValueSeparator should return same instance when separator is unchanged")
    void withObjectFieldValueSeparator_whenSeparatorIsUnchanged_shouldReturnSameInstance() {
        // Arrange: Create an instance with distinct, descriptive separators.
        final Separators originalSeparators = new Separators(':', ',', ';');
        final char currentSeparator = originalSeparators.getObjectFieldValueSeparator();

        // Act: Call the 'with' method using the same separator value.
        final Separators result = originalSeparators.withObjectFieldValueSeparator(currentSeparator);

        // Assert: The method should return the original instance for efficiency.
        assertSame(originalSeparators, result,
            "Should return the same instance if the separator is not changed (immutability optimization).");
    }

    @Test
    @DisplayName("withObjectFieldValueSeparator should return new instance when separator is changed")
    void withObjectFieldValueSeparator_whenSeparatorIsChanged_shouldReturnNewInstance() {
        // Arrange: Create an instance with distinct initial separators.
        final Separators originalSeparators = new Separators(':', ',', ';');
        final char newSeparator = '*';

        // Act: Call the 'with' method with a different separator value.
        final Separators updatedSeparators = originalSeparators.withObjectFieldValueSeparator(newSeparator);

        // Assert: A new instance should be created due to the class's immutability.
        assertNotSame(originalSeparators, updatedSeparators,
            "Should return a new instance if the separator is changed.");

        // Verify the state of the new instance.
        assertAll("New instance state",
            () -> assertEquals(newSeparator, updatedSeparators.getObjectFieldValueSeparator(),
                "The object field value separator should be updated."),
            () -> assertEquals(originalSeparators.getObjectEntrySeparator(), updatedSeparators.getObjectEntrySeparator(),
                "The object entry separator should remain unchanged."),
            () -> assertEquals(originalSeparators.getArrayValueSeparator(), updatedSeparators.getArrayValueSeparator(),
                "The array value separator should remain unchanged.")
        );
    }
}