package org.jsoup.parser;

import org.junit.Test;

/**
 * Test suite for {@link TokenQueue}.
 * This improved version focuses on clarity and standard testing practices.
 */
// The original class name `TokenQueue_ESTestTest24` was likely auto-generated.
// A more conventional name like `TokenQueueTest` is used here for better readability.
public class TokenQueueTest {

    /**
     * Verifies that attempting to call a method like matchesWord() on a closed TokenQueue
     * results in a NullPointerException. Once a queue is closed, it should not be usable.
     */
    @Test(expected = NullPointerException.class)
    public void callingMatchesWordOnClosedQueueThrowsException() {
        // Arrange: Create a TokenQueue and then immediately close it.
        TokenQueue queue = new TokenQueue("some data");
        queue.close();

        // Act & Assert: Attempting to use the queue after it's closed should throw.
        // The @Test(expected) annotation asserts that a NullPointerException is thrown.
        queue.matchesWord();
    }
}