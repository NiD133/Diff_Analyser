package org.jsoup.parser;

import org.junit.Test;

/**
 * Test suite for {@link TokenQueue}.
 */
public class TokenQueueTest {

    /**
     * Verifies that attempting to use a TokenQueue after it has been closed
     * results in a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void consumeOnClosedQueueThrowsException() {
        // Arrange: Create a queue and then close it.
        TokenQueue queue = new TokenQueue("some data");
        queue.close();

        // Act & Assert: Attempting to consume from the closed queue should throw.
        // The @Test(expected=...) annotation handles the assertion.
        queue.consume("some data");
    }
}