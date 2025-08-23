package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link TokenQueue} class.
 */
public class TokenQueueTest {

    @Test
    public void toString_whenQueueIsEmpty_returnsEmptyString() {
        // Arrange: Create a TokenQueue with an empty string.
        TokenQueue emptyQueue = new TokenQueue("");

        // Act: Get the string representation of the queue.
        String result = emptyQueue.toString();

        // Assert: Verify the result is an empty string.
        assertEquals("The string representation of an empty queue should be an empty string.", "", result);
    }
}