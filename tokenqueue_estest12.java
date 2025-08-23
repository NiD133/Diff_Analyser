package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for {@link TokenQueue}.
 */
public class TokenQueueTest {

    @Test
    public void consumeElementSelectorShouldConsumeTagNameAndStopAtDelimiter() {
        // Arrange: Create a queue where a simple tag name 'k' is followed by a delimiter ('"'),
        // which is not a valid character for an element selector.
        TokenQueue queue = new TokenQueue("k\"YT -6Ih:G~3zAw");

        // Act: Consume the element selector from the queue.
        String tagName = queue.consumeElementSelector();

        // Assert: Verify that only the tag name was consumed and the queue is now positioned
        // at the delimiter.
        assertEquals("The consumed tag name should be 'k'", "k", tagName);
        assertTrue("The queue should now start with the '\"' character", queue.matches('\"'));
        
        // A more comprehensive assertion can also check the entire remainder of the queue.
        assertEquals("The remainder of the queue should be the unconsumed part", "\"YT -6Ih:G~3zAw", queue.remainder());
    }
}