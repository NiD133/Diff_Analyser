package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the static utility methods in {@link TokenQueue}.
 */
public class TokenQueueUtilTest {

    @Test
    public void shouldEscapeCssIdentifierWithLeadingDigitAndSpecialChars() {
        // Arrange
        // An identifier starting with a digit and containing various special characters.
        String identifier = "4O*!*d1^m3R|W";

        // According to the CSSOM spec, an identifier cannot start with a digit.
        // It must be escaped as a hex code (e.g., '4' becomes '\34 ').
        // Other non-alphanumeric characters must also be backslash-escaped.
        String expectedEscapedIdentifier = "\\34 O\\*\\!\\*d1\\^m3R\\|W";

        // Act
        String actualEscapedIdentifier = TokenQueue.escapeCssIdentifier(identifier);

        // Assert
        assertEquals(expectedEscapedIdentifier, actualEscapedIdentifier);
    }
}