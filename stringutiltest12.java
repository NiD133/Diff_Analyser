package org.jsoup.internal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for the StringUtil.isAscii() method.
 */
class StringUtilTest {

    @DisplayName("should return true for strings containing only ASCII characters")
    @ParameterizedTest
    @ValueSource(strings = {
        "",              // Empty string
        "example.com",   // Simple ASCII text
        "One Two",       // ASCII with whitespace
        "123-!@#$%"      // ASCII with numbers and symbols
    })
    void isAscii_givenAsciiString_returnsTrue(String asciiString) {
        assertTrue(StringUtil.isAscii(asciiString));
    }

    @DisplayName("should return false for strings containing non-ASCII characters")
    @ParameterizedTest
    @ValueSource(strings = {
        "🧔",            // Emoji
        "测试",          // Chinese characters (CJK)
        "测试.com",      // Mixed ASCII and non-ASCII
        "français"       // Latin-1 Supplement characters
    })
    void isAscii_givenNonAsciiString_returnsFalse(String nonAsciiString) {
        assertFalse(StringUtil.isAscii(nonAsciiString));
    }
}