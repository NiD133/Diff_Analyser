package org.jsoup.internal;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link StringUtil}.
 */
class StringUtilTest {

    @ParameterizedTest
    @NullAndEmptySource // Covers null and ""
    @ValueSource(strings = {"  ", "   \r\n  "}) // Covers various whitespace-only strings
    void isBlank_shouldReturnTrue_forNullOrWhitespaceStrings(String blankInput) {
        // Act & Assert
        assertTrue(StringUtil.isBlank(blankInput));
    }

    @ParameterizedTest
    @ValueSource(strings = {"hello", "   hello   "})
    void isBlank_shouldReturnFalse_forNonBlankStrings(String nonBlankInput) {
        // Act & Assert
        assertFalse(StringUtil.isBlank(nonBlankInput));
    }
}