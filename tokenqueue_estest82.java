package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Test suite for the {@link TokenQueue} class.
 */
public class TokenQueueTest {

    @Test
    public void matchesCharShouldReturnFalseWhenNextCharDoesNotMatch() {
        // Arrange: Create a queue that starts with a '%' character.
        TokenQueue queue = new TokenQueue("%>-8wJ/e4'{T9H");

        // Act: Check if the queue starts with a '2', which it does not.
        boolean result = queue.matches('2');

        // Assert: The result should be false because the first character is not '2'.
        assertFalse("The queue should not match a character that is not at the current position.", result);
    }
}