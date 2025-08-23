package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Tests for {@link TokenQueue}.
 */
public class TokenQueueTest {

    @Test
    public void consumeElementSelectorStopsAtNonSelectorCharacter() {
        // Arrange: Create a queue where a valid element selector ('k') is
        // immediately followed by a non-selector character (a double quote).
        TokenQueue queue = new TokenQueue("k\"YT -6Ih:G3zAw");

        // Act: Consume the element selector from the queue.
        String elementSelector = queue.consumeElementSelector();

        // Assert: Verify that only the valid selector part was consumed
        // and the queue's position has advanced correctly.
        assertEquals("k", elementSelector);
        assertEquals("\"YT -6Ih:G3zAw", queue.remainder());
        
        // The original test also checked that the next character is not a word character.
        // This is implicitly covered by checking the remainder, but we can keep it for clarity.
        assertFalse(queue.matchesWord());
    }
}