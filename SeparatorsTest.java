package com.fasterxml.jackson.core.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Separators}, focusing on the immutability and correctness
 * of its "with..." factory methods.
 *
 * The tests verify that:
 * 1. A new instance is created when a separator value is changed.
 * 2. The same instance is returned when a separator value is not changed (immutability optimization).
 * 3. The properties of new instances are set correctly.
 *
 * @see Separators
 */
@DisplayName("Separators")
class SeparatorsTest {

    @Nested
    @DisplayName("withArrayValueSeparator()")
    class WithArrayValueSeparator {

        @Test
        @DisplayName("should return a new instance when the separator is changed")
        void shouldReturnNewInstanceWhenSeparatorIsChanged() {
            // Arrange
            final char initialSep = ',';
            final char newSep = ';';
            final Separators initialSeparators = new Separators(':', ',', initialSep);

            // Act
            final Separators updatedSeparators = initialSeparators.withArrayValueSeparator(newSep);

            // Assert
            assertNotSame(initialSeparators, updatedSeparators,
                "A new instance should be created for a different separator.");

            assertAll("The new instance should have the updated separator and retain other values",
                () -> assertEquals(newSep, updatedSeparators.getArrayValueSeparator(), "Array value separator should be updated."),
                () -> assertEquals(':', updatedSeparators.getObjectFieldValueSeparator(), "Object field separator should be unchanged."),
                () -> assertEquals(',', updatedSeparators.getObjectEntrySeparator(), "Object entry separator should be unchanged.")
            );
        }

        @Test
        @DisplayName("should return the same instance when the separator is not changed")
        void shouldReturnSameInstanceWhenSeparatorIsUnchanged() {
            // Arrange
            final char sameSeparator = ',';
            final Separators initialSeparators = new Separators(':', ',', sameSeparator);

            // Act
            final Separators resultSeparators = initialSeparators.withArrayValueSeparator(sameSeparator);

            // Assert
            assertSame(initialSeparators, resultSeparators,
                "The same instance should be returned if the separator is unchanged.");
        }
    }

    @Nested
    @DisplayName("withObjectEntrySeparator()")
    class WithObjectEntrySeparator {

        @Test
        @DisplayName("should return a new instance when the separator is changed")
        void shouldReturnNewInstanceWhenSeparatorIsChanged() {
            // Arrange
            final char initialSep = ',';
            final char newSep = ';';
            final Separators initialSeparators = new Separators(':', initialSep, ',');

            // Act
            final Separators updatedSeparators = initialSeparators.withObjectEntrySeparator(newSep);

            // Assert
            assertNotSame(initialSeparators, updatedSeparators,
                "A new instance should be created for a different separator.");

            assertAll("The new instance should have the updated separator and retain other values",
                () -> assertEquals(newSep, updatedSeparators.getObjectEntrySeparator(), "Object entry separator should be updated."),
                () -> assertEquals(':', updatedSeparators.getObjectFieldValueSeparator(), "Object field separator should be unchanged."),
                () -> assertEquals(',', updatedSeparators.getArrayValueSeparator(), "Array value separator should be unchanged.")
            );
        }

        @Test
        @DisplayName("should return the same instance when the separator is not changed")
        void shouldReturnSameInstanceWhenSeparatorIsUnchanged() {
            // Arrange
            final char sameSeparator = ',';
            final Separators initialSeparators = new Separators(':', sameSeparator, ',');

            // Act
            final Separators resultSeparators = initialSeparators.withObjectEntrySeparator(sameSeparator);

            // Assert
            assertSame(initialSeparators, resultSeparators,
                "The same instance should be returned if the separator is unchanged.");
        }
    }

    @Nested
    @DisplayName("withObjectFieldValueSeparator()")
    class WithObjectFieldValueSeparator {

        @Test
        @DisplayName("should return a new instance when the separator is changed")
        void shouldReturnNewInstanceWhenSeparatorIsChanged() {
            // Arrange
            final char initialSep = ':';
            final char newSep = '=';
            final Separators initialSeparators = new Separators(initialSep, ',', ',');

            // Act
            final Separators updatedSeparators = initialSeparators.withObjectFieldValueSeparator(newSep);

            // Assert
            assertNotSame(initialSeparators, updatedSeparators,
                "A new instance should be created for a different separator.");

            assertAll("The new instance should have the updated separator and retain other values",
                () -> assertEquals(newSep, updatedSeparators.getObjectFieldValueSeparator(), "Object field separator should be updated."),
                () -> assertEquals(',', updatedSeparators.getObjectEntrySeparator(), "Object entry separator should be unchanged."),
                () -> assertEquals(',', updatedSeparators.getArrayValueSeparator(), "Array value separator should be unchanged.")
            );
        }

        @Test
        @DisplayName("should return the same instance when the separator is not changed")
        void shouldReturnSameInstanceWhenSeparatorIsUnchanged() {
            // Arrange
            final char sameSeparator = ':';
            final Separators initialSeparators = new Separators(sameSeparator, ',', ',');

            // Act
            final Separators resultSeparators = initialSeparators.withObjectFieldValueSeparator(sameSeparator);

            // Assert
            assertSame(initialSeparators, resultSeparators,
                "The same instance should be returned if the separator is unchanged.");
        }
    }
}