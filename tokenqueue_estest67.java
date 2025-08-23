package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link TokenQueue} class.
 */
public class TokenQueueTest {

    /**
     * Verifies that chompBalanced correctly consumes the content between a specified
     * open and close character, returning the consumed content.
     */
    @Test
    public void chompBalancedConsumesContentBetweenSimpleDelimiters() {
        // Arrange: Create a queue with content wrapped by 'Z' and 'T'.
        TokenQueue queue = new TokenQueue("Z<}0z:#; O.[w{T3D");
        String expectedContent = "<}0z:#; O.[w{";
        String expectedRemainder = "3D";

        // Act: Chomp the content balanced between 'Z' and 'T'.
        // The initial 'Z' is consumed by the method but not included in the result.
        String actualContent = queue.chompBalanced('Z', 'T');

        // Assert: Verify the returned content and the remaining queue state are correct.
        assertEquals("The content between the delimiters should be returned.", expectedContent, actualContent);
        assertEquals("The queue should contain the characters after the closing delimiter.", expectedRemainder, queue.remainder());
    }
}