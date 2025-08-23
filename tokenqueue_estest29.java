package org.jsoup.parser;

import org.junit.Test;

/**
 * Test for verifying the behavior of a TokenQueue after it has been closed.
 */
public class TokenQueueClosedStateTest {

    /**
     * Verifies that attempting to use a TokenQueue after it has been closed
     * throws a NullPointerException. The close() method nullifies the internal
     * CharacterReader, and subsequent operations that rely on it should fail.
     */
    @Test(expected = NullPointerException.class)
    public void callingMatchChompOnClosedQueueThrowsNullPointerException() {
        // Arrange: Create a TokenQueue and immediately close it.
        TokenQueue queue = new TokenQueue("some data");
        queue.close();

        // Act & Assert: Attempting to use the closed queue should throw a NullPointerException.
        // The expected exception is declared in the @Test annotation.
        queue.matchChomp("some data");
    }
}