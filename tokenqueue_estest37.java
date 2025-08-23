package org.jsoup.parser;

import org.junit.Test;

/**
 * Test suite for TokenQueue.
 * This class contains an improved, more understandable version of the original test case.
 */
public class TokenQueueTest {

    /**
     * Verifies that attempting to consume from a TokenQueue after it has been closed
     * results in a NullPointerException. This is the expected behavior, as closing the
     * queue nullifies its internal character reader, making subsequent operations invalid.
     */
    @Test(expected = NullPointerException.class)
    public void consumeElementSelectorOnClosedQueueShouldThrowException() {
        // Arrange: Create a TokenQueue and then immediately close it.
        // The initial content of the queue is irrelevant for this test.
        TokenQueue tokenQueue = new TokenQueue("any-content");
        tokenQueue.close();

        // Act & Assert: Attempt to consume from the closed queue.
        // The @Test(expected) annotation asserts that a NullPointerException is thrown.
        tokenQueue.consumeElementSelector();
    }
}