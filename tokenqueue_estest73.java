package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * A focused test suite for the TokenQueue class.
 * This improved version replaces the auto-generated, hard-to-understand test.
 */
public class TokenQueueTest {

    /**
     * Verifies that matchChomp(char) returns true and consumes the character
     * when the character at the front of the queue is a match.
     */
    @Test
    public void matchChompShouldReturnTrueAndAdvanceQueueWhenCharacterMatches() {
        // Arrange: Set up the test with a simple and clear input string.
        TokenQueue queue = new TokenQueue("org.jsoup");

        // Act: Call the method under test.
        boolean result = queue.matchChomp('o');

        // Assert: Verify both the return value and the change in the queue's state.
        assertTrue("The method should return true for a successful match.", result);
        assertEquals("The matching character should be consumed from the queue.", "rg.jsoup", queue.remainder());
    }
}