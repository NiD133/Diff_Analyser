package org.jsoup.parser;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for the {@link TokenQueue} class.
 */
public class TokenQueueTest {

    /**
     * Verifies that chompBalanced throws an IllegalArgumentException when the closing
     * marker is not found before the end of the queue.
     */
    @Test
    public void chompBalancedThrowsExceptionForUnmatchedOpenMarker() {
        // Arrange: Create a queue with an opening marker ('$') but no corresponding closing marker.
        String input = "$dXr'&T^\"0qAav";
        TokenQueue queue = new TokenQueue(input);

        // Act & Assert
        try {
            queue.chompBalanced('$', '$');
            fail("Expected an IllegalArgumentException to be thrown due to an unclosed marker.");
        } catch (IllegalArgumentException e) {
            // The exception message should indicate the failure and show the remaining queue content.
            String expectedMessage = "Did not find balanced marker at 'dXr'&T^\"0qAav'";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}