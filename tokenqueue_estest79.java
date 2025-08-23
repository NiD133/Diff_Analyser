package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Test suite for the {@link TokenQueue} class.
 */
public class TokenQueueTest {

    /**
     * Verifies that matchesAny() returns false when the character at the front of the queue
     * is not present in the provided array of characters.
     */
    @Test
    public void matchesAnyShouldReturnFalseForNonMatchingCharacter() {
        // Arrange: Create a queue and a set of characters that does not include the queue's first character.
        TokenQueue queue = new TokenQueue("Zebra");
        char[] nonMatchingChars = {'a', 'b', 'c'};

        // Act: Check if the start of the queue matches any of the provided characters.
        boolean result = queue.matchesAny(nonMatchingChars);

        // Assert: The result must be false because the queue's first character 'Z' is not in the search set.
        assertFalse("matchesAny should return false when the queue's first character is not in the search array.", result);
    }
}