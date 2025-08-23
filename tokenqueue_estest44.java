package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Test suite for the {@link TokenQueue} class.
 */
public class TokenQueueTest {

    /**
     * Verifies that isEmpty() returns false when the queue is initialized
     * with a non-empty string.
     */
    @Test
    public void isEmptyShouldReturnFalseWhenQueueIsNotEmpty() {
        // Arrange: Create a TokenQueue with some initial data.
        TokenQueue queue = new TokenQueue("some data");

        // Act: Check if the queue is empty.
        boolean isEmpty = queue.isEmpty();

        // Assert: The queue should not be empty.
        assertFalse("A queue initialized with data should not be reported as empty.", isEmpty);
    }
}