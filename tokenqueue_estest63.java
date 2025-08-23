package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for {@link TokenQueue}.
 * This class contains tests for the chompBalanced method.
 */
public class TokenQueueChompBalancedTest {

    @Test
    public void chompBalanced_shouldThrowException_whenClosingMarkerIsNotFound() {
        // Arrange: Create a queue where the opening marker exists but the closing one does not.
        String input = "The '%s' parameter must not be empty.";
        TokenQueue queue = new TokenQueue(input);
        char marker = 'T';

        // Act & Assert: Expect an IllegalArgumentException when chompBalanced is called.
        try {
            queue.chompBalanced(marker, marker);
            fail("Expected an IllegalArgumentException because the closing marker is missing.");
        } catch (IllegalArgumentException e) {
            // The exception message should confirm that the marker was not found
            // and show the remainder of the queue where the search took place.
            String expectedMessage = "Did not find balanced marker at 'he '%s' parameter must not be empty.'";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}