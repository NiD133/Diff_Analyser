package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for {@link TokenQueue}.
 */
public class TokenQueueTest {

    /**
     * Verifies that chompBalanced returns an empty string when the opening character is not found.
     * It also confirms that the method consumes the entire queue in its search.
     */
    @Test
    public void chompBalancedReturnsEmptyAndConsumesQueueWhenOpenCharIsNotFound() {
        // Arrange: Create a queue with a string that does not contain the balancing characters.
        TokenQueue queue = new TokenQueue("some data without brackets");

        // Act: Attempt to extract a balanced string.
        String balancedContent = queue.chompBalanced('(', ')');

        // Assert:
        // 1. The result should be an empty string because the open character '(' was never found.
        assertTrue("Expected an empty string as the open char was not found.", balancedContent.isEmpty());

        // 2. The chompBalanced method consumes the queue while searching. Since the open
        //    character was not found, the entire queue should now be empty.
        assertTrue("The queue should be fully consumed after the search.", queue.isEmpty());

        // 3. As a follow-up, consuming from the now-empty queue should also yield an empty string,
        //    confirming its state.
        String subsequentConsume = queue.consumeElementSelector();
        assertTrue("Consuming from an empty queue should yield an empty string.", subsequentConsume.isEmpty());
    }
}