package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link TokenQueue} class.
 * This class focuses on testing the CSS identifier consumption logic.
 */
public class TokenQueueTest {

    @Test
    public void consumeCssIdentifierReplacesNullCharWithReplacementChar() {
        // Objective: Verify that a null character ('\u0000') within a potential CSS identifier
        // is replaced with the Unicode replacement character ('\uFFFD'), as per CSS syntax rules.
        // The consumption should stop at the first character that is not a valid part of an identifier.

        // Arrange
        // The input string contains 'v', a null character, and then a '{' which terminates the identifier.
        String inputWithNullChar = "v\u0000{Z|N V";
        TokenQueue queue = new TokenQueue(inputWithNullChar);

        // Act
        String consumedIdentifier = queue.consumeCssIdentifier();

        // Assert
        // The expected identifier should be 'v' followed by the replacement character.
        assertEquals("v\uFFFD", consumedIdentifier);

        // Verify that the rest of the queue is left untouched.
        assertEquals("{Z|N V", queue.remainder());
    }
}