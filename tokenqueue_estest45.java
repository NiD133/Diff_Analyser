package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link TokenQueue} class, focusing on element selector consumption.
 */
public class TokenQueueTest {

    /**
     * Verifies that consumeElementSelector() correctly extracts a selector
     * containing letters and wildcards, and properly stops at the first
     * invalid selector character (in this case, the '~' combinator).
     */
    @Test
    public void consumeElementSelectorStopsAtInvalidCharacter() {
        // Arrange: Create a queue with a valid selector followed by a combinator and other characters.
        String input = "WC**~WF-]9Y$]";
        TokenQueue queue = new TokenQueue(input);
        String expectedSelector = "WC**";

        // Act: Consume the element selector from the queue.
        String actualSelector = queue.consumeElementSelector();

        // Assert: The consumed part should match the expected selector.
        assertEquals(expectedSelector, actualSelector);
    }
}