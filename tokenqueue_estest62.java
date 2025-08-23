package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for the chompBalanced method in TokenQueue.
 */
public class TokenQueueTest {

    /**
     * Tests that chompBalanced throws an IllegalArgumentException if it consumes an opening
     * character but cannot find a corresponding closing character before the end of the queue.
     */
    @Test
    public void chompBalancedThrowsIfNoClosingMarkerIsFound() {
        // Arrange: Create a queue with an opening quote but no matching closing quote.
        String input = "\"@=e|.=e88-P~";
        TokenQueue queue = new TokenQueue(input);
        String expectedErrorMessage = "Did not find balanced marker at '@=e|.=e88-P~'";

        // Act & Assert
        try {
            queue.chompBalanced('"', '"');
            fail("Expected an IllegalArgumentException to be thrown due to an unclosed marker.");
        } catch (IllegalArgumentException e) {
            // Verify that the correct exception was thrown with the expected message.
            assertEquals(expectedErrorMessage, e.getMessage());
        }
    }
}