package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the static helper methods in {@link TokenQueue}.
 */
public class TokenQueueTest {

    /**
     * Verifies that the escapeCssIdentifier method correctly handles the Unicode
     * Replacement Character (U+FFFD). This character is valid within a CSS identifier
     * and should not be escaped.
     */
    @Test
    public void escapeCssIdentifierShouldNotEscapeUnicodeReplacementCharacter() {
        // Arrange
        // The input string contains a standard character 'v' followed by the Unicode Replacement Character.
        String identifier = "v\uFFFD";

        // Act
        String escapedIdentifier = TokenQueue.escapeCssIdentifier(identifier);

        // Assert
        // The output should be identical to the input, as no characters require escaping.
        assertEquals(identifier, escapedIdentifier);
    }
}