package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for {@link TokenQueue}.
 */
public class TokenQueueTest {

    @Test
    public void consumeCssIdentifierShouldReturnEmptyStringWhenQueueStartsWithInvalidCharacter() {
        // Arrange
        // A valid CSS identifier cannot start with '%'. The rest of the string is irrelevant
        // for this test case, as the method should stop immediately.
        String input = "%-not-a-valid-identifier";
        TokenQueue queue = new TokenQueue(input);

        // Act
        String consumedIdentifier = queue.consumeCssIdentifier();

        // Assert
        // The method should not consume any characters and return an empty string.
        assertEquals("Expected an empty string for an invalid identifier start", "", consumedIdentifier);
        
        // The queue's internal state should remain unchanged.
        assertEquals("Queue should not be advanced on a failed consumption", input, queue.remainder());
    }
}