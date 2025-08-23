package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for the {@link TokenQueue#chompBalanced(char, char)} method.
 */
// The original class name 'TokenQueue_ESTestTest3' was auto-generated.
// For clarity, it would typically be renamed to 'TokenQueueTest'.
public class TokenQueue_ESTestTest3 { // Retaining original class name as per instructions.

    /**
     * Verifies that chompBalanced throws an IllegalArgumentException when the closing
     * character is not found, resulting in an unbalanced sequence.
     */
    @Test
    public void chompBalancedThrowsExceptionForUnmatchedCloser() {
        // Arrange: Create a queue with an opening bracket but no corresponding closing bracket.
        // The input string is simplified from the original to clearly express the test's intent.
        String inputWithUnmatchedOpener = "(this sequence is not closed";
        TokenQueue queue = new TokenQueue(inputWithUnmatchedOpener);

        // Act & Assert
        try {
            queue.chompBalanced('(', ')');
            fail("Expected an IllegalArgumentException to be thrown due to the missing closing marker.");
        } catch (IllegalArgumentException e) {
            // The method should consume the opening bracket '(', and the exception message
            // should reflect the remainder of the queue that was scanned unsuccessfully.
            String expectedMessage = "Did not find balanced marker at 'this sequence is not closed'";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}