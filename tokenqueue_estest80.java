package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the static utility methods in {@link TokenQueue}.
 * This focuses on the {@code escapeCssIdentifier} method.
 */
public class TokenQueueTest {

    /**
     * Tests that a control character, which is not a valid CSS identifier character,
     * is correctly escaped according to CSS serialization rules.
     * <p>
     * According to the CSSOM specification, control characters (U+0000 to U+001F)
     * are escaped as their hexadecimal Unicode value followed by a space.
     *
     * @see <a href="https://www.w3.org/TR/cssom-1/#serialize-an-identifier">CSSOM Spec: Serialize an identifier</a>
     */
    @Test
    public void escapeCssIdentifierShouldEscapeControlCharacterWithTrailingSpace() {
        // Arrange
        // The input is the control character U+0005 (ENQUIRY).
        String identifierWithControlChar = "\u0005";
        String expectedEscapedIdentifier = "\\5 "; // Escaped as a hex value with a trailing space.

        // Act
        String actualEscapedIdentifier = TokenQueue.escapeCssIdentifier(identifierWithControlChar);

        // Assert
        assertEquals("Control characters should be escaped as a hex value followed by a space.",
                expectedEscapedIdentifier, actualEscapedIdentifier);
    }
}