package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for the {@link TokenQueue} class.
 */
public class TokenQueueTest {

    /**
     * Verifies that a TokenQueue initialized with an empty string
     * correctly reports that it is empty.
     */
    @Test
    public void isEmptyShouldReturnTrueWhenQueueIsCreatedWithEmptyString() {
        // Arrange: Create a TokenQueue with an empty string.
        TokenQueue queue = new TokenQueue("");

        // Act: Call the method under test.
        boolean isEmpty = queue.isEmpty();

        // Assert: Verify the queue is empty.
        assertTrue("A queue initialized with an empty string should be empty.", isEmpty);
    }
}