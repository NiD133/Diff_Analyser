package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for {@link TokenQueue}.
 */
public class TokenQueueTest {

    @Test
    public void consumeElementSelectorShouldConsumeSelectorAndAdvanceQueue() {
        // Arrange: Create a queue where 'k' is a valid element selector followed by other characters.
        TokenQueue queue = new TokenQueue("k\"YT -6Ih:G~3zAw");

        // Act: Consume the element selector from the front of the queue.
        String selector = queue.consumeElementSelector();

        // Assert: The correct selector was consumed and returned.
        assertEquals("The consumed selector should be 'k'", "k", selector);

        // Assert: The queue's internal position has advanced past the consumed selector.
        // A subsequent attempt to match the same selector should fail.
        assertFalse("Queue should have advanced, so 'k' should not be matched again", queue.matchChomp("k"));
    }
}