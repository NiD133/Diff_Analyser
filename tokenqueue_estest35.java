package org.jsoup.parser;

import org.junit.Test;

/**
 * Test suite for the {@link TokenQueue} class.
 */
public class TokenQueueTest {

    /**
     * Verifies that calling {@link TokenQueue#consumeTo(String)} with an empty string
     * argument is an invalid operation and results in a StringIndexOutOfBoundsException.
     * This test covers the edge case where the delimiter is empty.
     */
    @Test(expected = StringIndexOutOfBoundsException.class)
    public void consumeToWithEmptyStringDelimiterShouldThrowException() {
        // Arrange: Create an empty TokenQueue.
        TokenQueue queue = new TokenQueue("");

        // Act: Attempt to consume up to an empty string delimiter.
        // Assert: The test expects a StringIndexOutOfBoundsException to be thrown.
        queue.consumeTo("");
    }
}