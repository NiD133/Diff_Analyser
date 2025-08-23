package org.jsoup.parser;

import org.junit.Test;

/**
 * Test suite for {@link TokenQueue}.
 */
public class TokenQueueTest {

    /**
     * Verifies that attempting to use a TokenQueue after it has been closed
     * results in a NullPointerException, as its internal state is invalidated.
     */
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionWhenOperatingOnClosedQueue() {
        // Arrange: Create a queue and then close it to invalidate its state.
        TokenQueue queue = new TokenQueue("some-data");
        queue.close();

        // Act & Assert: Attempting to use the queue should throw a NullPointerException.
        // The assertion is handled by the `expected` parameter of the @Test annotation.
        queue.matchChomp('A');
    }
}