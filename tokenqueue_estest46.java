package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link TokenQueue#consumeElementSelector()} method.
 */
public class TokenQueueConsumeElementSelectorTest {

    @Test
    public void shouldConsumeSimpleSingleCharacterTagName() {
        // Arrange: Create a queue with a simple, valid element selector (a tag name).
        TokenQueue queue = new TokenQueue("b");

        // Act: Consume the element selector from the queue.
        String selector = queue.consumeElementSelector();

        // Assert: The consumed selector should be the single character tag name.
        assertEquals("b", selector);
    }
}