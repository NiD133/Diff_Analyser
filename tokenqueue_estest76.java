package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for the {@link TokenQueue} class.
 */
public class TokenQueueTest {

    /**
     * Verifies that the {@link TokenQueue#matches(String)} method returns true
     * when the provided string is an exact match for the entire content of the queue.
     */
    @Test
    public void matchesReturnsTrueForFullExactMatch() {
        // Arrange: Create a TokenQueue with a specific string.
        String content = "|;tj;PF%cdg`,";
        TokenQueue queue = new TokenQueue(content);

        // Act & Assert: Check if the queue matches the exact same string.
        assertTrue("Expected matches() to return true for an identical string.", queue.matches(content));
    }
}