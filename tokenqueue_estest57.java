package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the static utility methods in {@link TokenQueue}.
 */
public class TokenQueueTest {

    /**
     * Tests that CSS meta-characters, which have special meaning in a selector,
     * are correctly escaped with a backslash. This test case covers a leading hyphen,
     * a space, and a percent sign.
     *
     * @see <a href="https://www.w3.org/TR/cssom-1/#serialize-an-identifier">CSSOM Spec: Serialize an identifier</a>
     */
    @Test
    public void escapeCssIdentifierShouldEscapeSpecialCharacters() {
        // Arrange
        String identifierWithSpecialChars = "- %";
        String expectedEscapedIdentifier = "-\\ \\%";

        // Act
        String actualEscapedIdentifier = TokenQueue.escapeCssIdentifier(identifierWithSpecialChars);

        // Assert
        assertEquals(expectedEscapedIdentifier, actualEscapedIdentifier);
    }
}