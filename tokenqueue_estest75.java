package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Test suite for the TokenQueue class.
 */
public class TokenQueueTest {

    @Test
    public void matchesWhitespaceShouldReturnFalseWhenQueueStartsWithNonWhitespace() {
        // Arrange: Create a queue that does not start with a whitespace character.
        TokenQueue queue = new TokenQueue("f7(L)+X");

        // Act: Check if the queue starts with whitespace.
        boolean result = queue.matchesWhitespace();

        // Assert: The result should be false.
        assertFalse("Expected matchesWhitespace() to be false for a non-whitespace character.", result);
    }
}