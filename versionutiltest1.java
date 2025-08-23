package com.fasterxml.jackson.core.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the {@link VersionUtil} class, focusing on the
 * {@code parseVersionPart} method.
 */
class VersionUtilTest {

    @Test
    @DisplayName("Should parse a simple numeric string into an integer")
    void parseVersionPart_whenGivenSimpleNumber_shouldReturnInteger() {
        // When parsing a string containing only digits
        int result = VersionUtil.parseVersionPart("13");

        // Then the corresponding integer value should be returned
        assertEquals(13, result);
    }

    @Test
    @DisplayName("Should parse only the number before a separator")
    void parseVersionPart_whenGivenNumberWithSeparator_shouldParseUntilSeparator() {
        // When parsing a string with numbers followed by a separator and more characters
        int result = VersionUtil.parseVersionPart("27.8");

        // Then only the number before the separator should be returned, as the
        // method is designed to extract a single version segment.
        assertEquals(27, result, "Should ignore characters from the separator '.' onwards");
    }

    @Test
    @DisplayName("Should return 0 for a negative number string")
    void parseVersionPart_whenGivenNegativeNumber_shouldReturnZero() {
        // When parsing a string representing a negative number
        int result = VersionUtil.parseVersionPart("-3");

        // Then the result should be 0, as version parts are non-negative.
        assertEquals(0, result);
    }

    @Test
    @DisplayName("Should return 0 for a non-numeric string")
    void parseVersionPart_whenGivenNonNumericString_shouldReturnZero() {
        // When parsing a string that does not start with a digit
        int result = VersionUtil.parseVersionPart("foo");

        // Then the result should be 0, indicating an unparseable version part.
        assertEquals(0, result);
    }
}