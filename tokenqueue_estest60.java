package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link TokenQueue}.
 */
public class TokenQueueTest {

    /**
     * Tests that chompBalanced throws an exception when the input string does not contain
     * a matching closing character for the initial opening character.
     */
    @Test
    public void chompBalancedThrowsExceptionForUnbalancedMarkers() {
        // Arrange: Create a queue with an input string that has a nested structure
        // but is ultimately unbalanced because the initial '{' is never closed.
        String unbalancedInput = "{#7l{C/gwF;D^Xtn&";
        TokenQueue queue = new TokenQueue(unbalancedInput);

        // The method is expected to throw an exception. The exception message should
        // contain the state of the queue after the initial opening marker is consumed.
        String expectedRemainingQueue = "#7l{C/gwF;D^Xtn&";
        String expectedMessage = "Did not find balanced marker at '" + expectedRemainingQueue + "'";

        // Act & Assert
        try {
            queue.chompBalanced('{', ';');
            fail("Expected an IllegalArgumentException because the markers are not balanced.");
        } catch (IllegalArgumentException e) {
            // Verify that the correct exception was thrown with the expected message.
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}