package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the static helper methods in {@link TokenQueue}.
 */
public class TokenQueueTest {

    /**
     * Verifies that {@link TokenQueue#escapeCssIdentifier(String)} correctly escapes various
     * special characters that are invalid in a CSS identifier. This includes quotes,
     * spaces, colons, and other symbols, which should be prefixed with a backslash.
     */
    @Test
    public void shouldEscapeSpecialCharactersInCssIdentifier() {
        // Arrange
        String identifierWithSpecialChars = "k\"YT -6Ih:G~3zAw";

        // The expected output, where characters like ", :, ~, and space are escaped with a backslash.
        // Note: The original auto-generated test had a different expected value ("k\\\"YT\\7f -6Ih\\:G\\~3zAw").
        // The "\\7f" (DEL character) appears to be an error, as it's not present in the input string.
        // This corrected version aligns with the CSS specification and the documented behavior.
        String expectedEscapedIdentifier = "k\\\"YT\\ -6Ih\\:G\\~3zAw";

        // Act
        String actualEscapedIdentifier = TokenQueue.escapeCssIdentifier(identifierWithSpecialChars);

        // Assert
        assertEquals(expectedEscapedIdentifier, actualEscapedIdentifier);
    }
}