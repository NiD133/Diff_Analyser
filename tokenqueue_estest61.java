package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for the {@link TokenQueue} class.
 */
public class TokenQueueTest {

    @Test
    public void chompBalancedThrowsExceptionWhenNoClosingMarkerIsFound() {
        // Arrange: Create a queue where the opening marker exists but a closing one does not.
        String input = " 5mL\"2HD\"v"; // The initial space is the 'open' marker.
        TokenQueue queue = new TokenQueue(input);

        // Act & Assert
        try {
            // Attempt to find a balanced segment where the open and close markers are both spaces.
            queue.chompBalanced(' ', ' ');
            fail("Expected an IllegalArgumentException to be thrown, but it was not.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception message correctly identifies the failure point.
            String expectedMessage = "Did not find balanced marker at '5mL\"2HD\"v'";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}