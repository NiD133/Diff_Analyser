package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link TokenQueue}.
 */
public class TokenQueueTest {

    /**
     * Tests that consumeCssIdentifier() correctly reads a valid identifier
     * and stops at the first character that is not part of a valid CSS identifier.
     */
    @Test
    public void consumeCssIdentifierStopsAtInvalidCharacter() {
        // Arrange
        // A CSS identifier can contain letters, digits, hyphens, and underscores.
        // The '}' character is an invalid character and should terminate consumption.
        String input = "oUY}ddkFQ:tJ.q";
        TokenQueue queue = new TokenQueue(input);
        String expectedIdentifier = "oUY";

        // Act
        String actualIdentifier = queue.consumeCssIdentifier();

        // Assert
        assertEquals(expectedIdentifier, actualIdentifier);
    }
}