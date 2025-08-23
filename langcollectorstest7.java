package org.apache.commons.lang3.stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link LangCollectors}.
 *
 * <p>This revised test suite focuses on clarity and maintainability by organizing tests
 * based on the method under test and using descriptive names.
 * </p>
 */
@DisplayName("LangCollectors")
class LangCollectorsTest {

    @Nested
    @DisplayName("joining() collector")
    class JoiningNoDelimiterTest {

        @Test
        @DisplayName("should return an empty string when collecting no elements")
        void shouldReturnEmptyStringForNoElements() {
            // Act: Collect from an empty set of elements.
            final String result = LangCollectors.collect(LangCollectors.joining());

            // Assert
            assertEquals("", result, "Collecting zero elements should result in an empty string.");
        }

        @Test
        @DisplayName("should return the element's string representation for a single element")
        void shouldReturnStringForSingleElement() {
            // Act: Collect from a single element.
            final String result = LangCollectors.collect(LangCollectors.joining(), "1");

            // Assert
            assertEquals("1", result, "Collecting a single element should return its string representation.");
        }

        @Test
        @DisplayName("should return concatenated element strings for multiple elements")
        void shouldReturnConcatenatedStringsForMultipleElements() {
            // Act: Collect from multiple elements.
            final String result = LangCollectors.collect(LangCollectors.joining(), "1", "2", "3");

            // Assert
            assertEquals("123", result, "Collecting multiple elements should concatenate their string representations without a delimiter.");
        }

        @Test
        @DisplayName("should convert null elements to the string 'null'")
        void shouldConvertNullToStringLiteral() {
            // Act: Collect from elements including a null.
            final String result = LangCollectors.collect(LangCollectors.joining(), "1", null, "3");

            // Assert
            assertEquals("1null3", result, "A null element in the collection should be converted to the string 'null'.");
        }
    }

    // Tests for other LangCollectors.joining() overloads (e.g., with a delimiter) would go in their own nested classes here.
    // @Nested
    // @DisplayName("joining(delimiter) collector")
    // class JoiningWithDelimiterTest { ... }
}