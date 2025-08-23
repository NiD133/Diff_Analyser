package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for sequential consumption methods in TokenQueue.
 */
public class TokenQueueConsumptionTest {

    @Test
    public void shouldConsumeDifferentTokenTypesInSequence() {
        // Arrange: Create a queue with a string containing an element selector ('k'),
        // a quoted section, and a CSS identifier ('YT'), among other characters.
        String input = "k\"YT -6Ih:G~3zAw";
        TokenQueue queue = new TokenQueue(input);

        // Act & Assert: Part 1 - Consume an element selector
        // The consumeElementSelector() method should read the initial tag name 'k'.
        String elementSelector = queue.consumeElementSelector();
        assertEquals("k", elementSelector);
        // At this point, the queue's remainder is: "\"YT -6Ih:G~3zAw"

        // Act & Assert: Part 2 - Chomp a "balanced" part
        // This is an edge-case test for chompBalanced. The open/close characters ('!')
        // are not present. The queue starts with a quote ('"'). The method consumes
        // only the initial quote character in this scenario.
        String balancedPart = queue.chompBalanced('!', '!');
        assertEquals("\"", balancedPart);
        // The queue's remainder is now: "YT -6Ih:G~3zAw"

        // Act & Assert: Part 3 - Consume a CSS identifier
        // The consumeCssIdentifier() method should read the identifier 'YT'.
        String cssIdentifier = queue.consumeCssIdentifier();
        assertEquals("YT", cssIdentifier);
        // The final remainder of the queue is: " -6Ih:G~3zAw"
    }
}