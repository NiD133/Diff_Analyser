package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the static utility methods in {@link TokenQueue}.
 */
public class TokenQueueTest {

    @Test
    public void escapeCssIdentifier_shouldEscapeLeadingHyphen() {
        // The CSS Object Model spec requires a hyphen at the start of an identifier to be escaped.
        // See: https://www.w3.org/TR/cssom-1/#serialize-an-identifier

        // Arrange
        String identifier = "-";
        String expected = "\\-";

        // Act
        String actual = TokenQueue.escapeCssIdentifier(identifier);

        // Assert
        assertEquals(expected, actual);
    }
}