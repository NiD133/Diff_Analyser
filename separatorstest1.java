package com.fasterxml.jackson.core.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Separators} class, focusing on its immutability
 * and the behavior of its "with" methods.
 */
class SeparatorsTest {

    @Nested
    @DisplayName("withArrayValueSeparator method")
    class WithArrayValueSeparator {

        @Test
        @DisplayName("should return the same instance when the separator is unchanged")
        void whenSeparatorIsUnchanged_shouldReturnSameInstance() {
            // Arrange
            final char separator = '5';
            Separators originalSeparators = new Separators(separator, separator, separator);

            // Act
            Separators updatedSeparators = originalSeparators.withArrayValueSeparator(separator);

            // Assert
            assertSame(originalSeparators, updatedSeparators,
                    "Calling with the same separator value should return the same instance, preserving immutability.");
        }

        @Test
        @DisplayName("should return a new instance when the separator is changed")
        void whenSeparatorIsChanged_shouldReturnNewInstance() {
            // Arrange
            final char originalSeparator = '5';
            final char newSeparator = '6';
            Separators originalSeparators = new Separators(originalSeparator, originalSeparator, originalSeparator);

            // Act
            Separators updatedSeparators = originalSeparators.withArrayValueSeparator(newSeparator);

            // Assert
            assertNotSame(originalSeparators, updatedSeparators,
                    "Calling with a different separator value should return a new instance.");

            // Verify the new instance has the correct values
            assertEquals(newSeparator, updatedSeparators.getArrayValueSeparator(), "Array separator should be updated.");
            assertEquals(originalSeparator, updatedSeparators.getObjectEntrySeparator(), "Object entry separator should remain unchanged.");
            assertEquals(originalSeparator, updatedSeparators.getObjectFieldValueSeparator(), "Object field separator should remain unchanged.");
        }
    }
}