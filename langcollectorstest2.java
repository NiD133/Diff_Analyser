package org.apache.commons.lang3.stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for the joining() collectors in {@link LangCollectors}.
 */
@DisplayName("LangCollectors.joining")
class LangCollectorsJoiningTest {

    /**
     * A simple fixture class for testing with custom objects.
     * Its toString() method returns the integer value as a String.
     */
    private static final class Fixture {
        private final int value;

        private Fixture(final int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return Integer.toString(value);
        }
    }

    @Nested
    @DisplayName("with no delimiter")
    class JoiningWithoutDelimiter {

        @Test
        @DisplayName("should return an empty string for no elements")
        void shouldReturnEmptyStringForNoElements() {
            // Act
            final String result = LangCollectors.collect(LangCollectors.joining());

            // Assert
            assertEquals("", result);
        }

        @Test
        @DisplayName("should return the string representation of a single element")
        void shouldReturnStringForSingleElement() {
            // Act
            final String result = LangCollectors.collect(LangCollectors.joining(), 1L);

            // Assert
            assertEquals("1", result);
        }

        @Test
        @DisplayName("should concatenate the string representations of multiple elements")
        void shouldConcatenateMultipleElements() {
            // Act
            final String result = LangCollectors.collect(LangCollectors.joining(), 1L, 2L, 3L);

            // Assert
            assertEquals("123", result);
        }

        @Test
        @DisplayName("should represent a null element as the string 'null'")
        void shouldRepresentNullAsString() {
            // Act
            final String result = LangCollectors.collect(LangCollectors.joining(), 1L, null, 3L);

            // Assert
            assertEquals("1null3", result);
        }

        @Test
        @DisplayName("should use the toString() method of custom objects")
        void shouldUseToStringOnCustomObjects() {
            // Arrange
            final Fixture fixture1 = new Fixture(1);
            final Fixture fixture2 = new Fixture(2);

            // Act
            final String result = LangCollectors.collect(LangCollectors.joining(), fixture1, fixture2);

            // Assert
            assertEquals("12", result);
        }

        @Test
        @DisplayName("should work with various non-string object types like AtomicLong")
        void shouldWorkWithStandardObjectTypes() {
            // Arrange
            final AtomicLong atomic1 = new AtomicLong(1);
            final AtomicLong atomic2 = new AtomicLong(2);

            // Act
            final String result = LangCollectors.collect(LangCollectors.joining(), atomic1, atomic2);

            // Assert
            assertEquals("12", result);
        }
    }

    // Additional nested classes could be added here to test other overloads
    // of LangCollectors.joining(), for example:
    // @Nested
    // @DisplayName("with a delimiter")
    // class JoiningWithDelimiter { ... }
}