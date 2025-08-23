package org.jsoup.parser;

import org.junit.Test;

/**
 * Test suite for the TokenQueue class.
 */
public class TokenQueueTest {

    /**
     * Verifies that attempting to consume from a queue after it has been closed
     * results in a NullPointerException. This is expected because closing the queue
     * releases its internal resources.
     */
    @Test(expected = NullPointerException.class)
    public void consumeAfterCloseThrowsNullPointerException() {
        // Arrange: Create a queue and then close it.
        TokenQueue queue = new TokenQueue("Some data");
        queue.close();

        // Act: Attempt to consume from the closed queue.
        queue.consume();

        // Assert: A NullPointerException is expected, as declared in the @Test annotation.
    }
}