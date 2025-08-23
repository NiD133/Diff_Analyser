package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link TokenQueue}.
 * This suite focuses on clarifying the behavior of consumption methods under specific edge cases.
 */
public class TokenQueueTest {

    /**
     * The original test combined two unrelated method calls. This test focuses on the second one:
     * {@link TokenQueue#consumeToAny(String...)}. It verifies that if none of the provided terminators
     * are found in the queue, the entire queue content is consumed.
     */
    @Test
    public void consumeToAnyConsumesEntireQueueWhenNoTerminatorsAreFound() {
        // Arrange
        String initialData = ">-8wJ/e4'{T9H";
        TokenQueue queue = new TokenQueue(initialData);
        String[] noMatchingTerminators = {"nonexistent", "terminators"};

        // Act
        String consumed = queue.consumeToAny(noMatchingTerminators);

        // Assert
        assertEquals("Should consume the entire queue content when no terminators match.",
            initialData, consumed);
        assertTrue("The queue should be empty after consuming all its content.",
            queue.isEmpty());
    }

    /**
     * The original test included a confusing call to {@link TokenQueue#chompBalanced(char, char)}
     * that did not align with the method's primary purpose. The assertion in the original test
     * suggested a behavior (consuming and returning the first character) that is incorrect for
     * current versions of jsoup.
     *
     * A more useful and correct test is to verify that `chompBalanced` does nothing if the queue
     * does not start with the specified opening character.
     */
    @Test
    public void chompBalancedReturnsEmptyAndConsumesNothingIfQueueDoesNotStartWithOpenChar() {
        // Arrange
        String initialData = "content (balanced) more";
        TokenQueue queue = new TokenQueue(initialData);

        // Act
        // Attempt to chomp a balanced section, but the queue doesn't start with '('.
        String balancedPart = queue.chompBalanced('(', ')');

        // Assert
        assertTrue("Should return an empty string as the open character was not at the start.",
            balancedPart.isEmpty());
        assertEquals("The queue should remain unchanged.",
            initialData, queue.remainder());
    }
}