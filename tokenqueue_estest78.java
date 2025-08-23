package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Test suite for the {@link TokenQueue} class.
 */
public class TokenQueueTest {

    /**
     * Verifies that matchChomp(char) returns false when the queue is empty,
     * as there is no character to match or consume.
     */
    @Test
    public void matchChompCharShouldReturnFalseWhenQueueIsEmpty() {
        // Arrange: Create an empty TokenQueue.
        // The original test created a queue with content and then consumed it all.
        // Initializing with an empty string is a more direct and clearer way to set up the test state.
        TokenQueue queue = new TokenQueue("");

        // Act: Attempt to match and chomp a character from the empty queue.
        boolean wasMatched = queue.matchChomp('Z');

        // Assert: The method should return false because the queue is empty.
        assertFalse("matchChomp should return false on an empty queue", wasMatched);
    }
}