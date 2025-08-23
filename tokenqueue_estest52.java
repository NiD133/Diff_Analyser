package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for the TokenQueue class.
 */
public class TokenQueueTest {

    @Test
    public void consumeWhitespaceShouldSucceedWhenQueueContainsOnlySpaces() {
        // Arrange: Create a TokenQueue containing only space characters.
        TokenQueue queue = new TokenQueue("             ");

        // Act: Consume the whitespace from the queue.
        boolean wasWhitespaceConsumed = queue.consumeWhitespace();

        // Assert: Verify that the method signaled success and that the queue is now empty.
        assertTrue("Should return true when whitespace is consumed.", wasWhitespaceConsumed);
        assertTrue("The queue should be empty after consuming all spaces.", queue.isEmpty());
    }
}