package org.jsoup.internal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for the StringUtil class, focusing on the isWhitespace() method.
 */
// The class was renamed from StringUtilTestTest6 for clarity and convention.
public class StringUtilTest {

    @DisplayName("isWhitespace() should return true for characters defined as whitespace in the HTML spec")
    @ParameterizedTest(name = "Recognizes character ''{0}'' as whitespace")
    @ValueSource(chars = {' ', '\t', '\n', '\f', '\r'})
    void isWhitespace_ReturnsTrue_ForHtmlSpecWhitespace(char c) {
        // According to the StringUtil#isWhitespace Javadoc, this method checks for
        // whitespace as defined by the HTML spec.
        assertTrue(StringUtil.isWhitespace(c));
    }

    @DisplayName("isWhitespace() should return false for other space-like, non-HTML-spec characters")
    @ParameterizedTest(name = "Rejects character ''{0}'' as whitespace")
    @ValueSource(chars = {
        '\u00a0', // non-breaking space
        '\u2000', // en quad
        '\u3000'  // ideographic space
    })
    void isWhitespace_ReturnsFalse_ForNonHtmlSpecWhitespace(char c) {
        // These characters are visually similar to spaces but are not considered
        // whitespace by the HTML spec definition, which this method implements.
        // The method isActuallyWhitespace() handles these cases differently.
        assertFalse(StringUtil.isWhitespace(c));
    }
}