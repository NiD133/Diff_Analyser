package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link TokenQueue} class, focusing on edge cases
 * for the chompBalanced method.
 */
public class TokenQueueTest {

    /**
     * Verifies that {@link TokenQueue#chompBalanced(char, char)} throws an
     * {@link IllegalArgumentException} when the open and close characters are identical.
     * <p>
     * The method's balancing logic is not designed to handle this scenario. It increments a
     * depth counter upon finding an 'open' character but, due to an if/else-if structure,
     * it does not subsequently check if that same character is also the 'close' character.
     * As a result, the depth counter never decreases, causing the method to consume the
     * entire queue and fail, reporting that a balanced marker was not found.
     * </p>
     */
    @Test
    public void chompBalancedThrowsExceptionWhenOpenAndCloseCharsAreSame() {
        // Arrange: Create a queue where the open and close markers are the same ('?').
        TokenQueue queue = new TokenQueue("?&KL?gZd\"8't");
        final char marker = '?';

        // Act & Assert
        try {
            queue.chompBalanced(marker, marker);
            fail("Expected an IllegalArgumentException to be thrown, but no exception occurred.");
        } catch (IllegalArgumentException e) {
            // The exception message should confirm that the remainder of the queue was
            // scanned without finding a valid closing marker.
            final String expectedMessage = "Did not find balanced marker at '&KL?gZd\"8't'";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}