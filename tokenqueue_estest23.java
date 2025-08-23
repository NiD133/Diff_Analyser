package org.jsoup.parser;

import org.junit.Test;

/**
 * Tests for the {@link TokenQueue} class.
 */
public class TokenQueueTest {

    /**
     * Verifies that attempting to access the remainder of a TokenQueue after it has been closed
     * results in a NullPointerException. This is the expected behavior, as closing the queue
     * releases its underlying resources.
     */
    @Test(expected = NullPointerException.class)
    public void remainderOnClosedQueueThrowsNullPointerException() {
        // Arrange: Create a TokenQueue with some data.
        TokenQueue tokenQueue = new TokenQueue("some data");

        // Act: Close the queue, which should release its internal reader.
        tokenQueue.close();

        // Assert: Attempting to get the remainder now should throw a NullPointerException.
        // The @Test(expected=...) annotation handles this assertion automatically.
        tokenQueue.remainder();
    }
}