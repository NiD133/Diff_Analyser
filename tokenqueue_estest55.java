package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the static CSS identifier escaping method in {@link TokenQueue}.
 */
public class TokenQueueCssIdentifierTest {

    @Test
    public void shouldEscapeDigitWhenItFollowsLeadingHyphen() {
        // According to CSS syntax, an identifier cannot start with a hyphen immediately followed by a digit.
        // For example, "-6" would be parsed as a negative number, not an identifier.
        // To represent this sequence literally, the digit must be escaped.
        // The character '6' is Unicode U+0036. The CSS escape is "\36 ", including a trailing space
        // to delimit the hex escape sequence from subsequent characters.
        // See: https://www.w3.org/TR/css-syntax-3/#ident-token-diagram

        // Arrange
        String identifier = "-6Ih";
        String expectedEscapedIdentifier = "-\\36 Ih";

        // Act
        String actualEscapedIdentifier = TokenQueue.escapeCssIdentifier(identifier);

        // Assert
        assertEquals(expectedEscapedIdentifier, actualEscapedIdentifier);
    }
}