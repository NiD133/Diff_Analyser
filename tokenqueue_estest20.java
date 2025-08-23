package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for the TokenQueue class.
 */
public class TokenQueueTest {

    /**
     * Tests the behavior of chompBalanced() when the specified open and close delimiters
     * are not present in the queue. This test case reveals that the method consumes characters
     * up to the first encountered parenthesis, even if it's not one of the specified delimiters.
     */
    @Test
    public void chompBalancedWithUnmatchedDelimitersConsumesUntilFirstParenthesis() {
        // Arrange: Create a queue with a string that does not contain the target delimiters ('P', 'u')
        // but does contain a parenthesis.
        TokenQueue queue = new TokenQueue("f7(L)+X");

        // Act: Call chompBalanced with delimiters that are not in the string.
        String chompedData = queue.chompBalanced('P', 'u');

        // Assert: The method should consume and return the content up to the parenthesis.
        assertEquals("Should have chomped only 'f'", "f", chompedData);

        // Assert: The queue should have advanced past the chomped character.
        // The next character to be consumed should be '7'.
        char nextChar = queue.consume();
        assertEquals("The next character in the queue should be '7'", '7', nextChar);
        assertEquals("The remainder of the queue should be correct", "(L)+X", queue.remainder());
    }
}