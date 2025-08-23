package org.jsoup.parser;

import org.junit.Test;

/**
 * Test suite for {@link TokenQueue}.
 * This class contains tests for the TokenQueue's state and behavior.
 */
public class TokenQueueTest {

    /**
     * Verifies that attempting to use a TokenQueue after it has been closed
     * results in a NullPointerException. This ensures that the queue correctly
     * invalidates its state upon closure and prevents further operations.
     */
    @Test(expected = NullPointerException.class)
    public void callingConsumeWhitespaceOnClosedQueueThrowsException() {
        // Arrange: Create a TokenQueue and then immediately close it.
        TokenQueue queue = new TokenQueue("some data");
        queue.close();

        // Act & Assert: Attempt to consume whitespace from the closed queue.
        // The @Test(expected) annotation asserts that a NullPointerException is thrown.
        queue.consumeWhitespace();
    }
}