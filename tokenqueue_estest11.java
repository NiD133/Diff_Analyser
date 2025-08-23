package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Test suite for the {@link TokenQueue} class.
 */
public class TokenQueueTest {

    /**
     * Verifies that {@link TokenQueue#matches(String)} returns false when the
     * sequence at the front of the queue does not match the provided string.
     */
    @Test
    public void matches_withNonMatchingSequence_shouldReturnFalse() {
        // Arrange: Create a queue with some initial data.
        TokenQueue queue = new TokenQueue("QueueData");
        String nonMatchingSequence = "DifferentData";

        // Act: Check if the queue starts with a sequence that is clearly different.
        boolean result = queue.matches(nonMatchingSequence);

        // Assert: The method should return false as the queue does not start with the given sequence.
        assertFalse("Expected matches() to return false for a non-matching sequence.", result);
    }
}