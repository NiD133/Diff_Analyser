package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for the TokenQueue class.
 */
public class TokenQueueTest {

    /**
     * Verifies that consumeTo() consumes the entire remaining queue
     * when the target sequence is not found.
     */
    @Test
    public void consumeToWithNonMatchingSequenceConsumesEntireQueue() {
        // Arrange: Create a queue and advance its position so the target sequence is no longer present.
        String originalData = "Jkl):ip4E/0M";
        TokenQueue queue = new TokenQueue(originalData);
        queue.consume(); // Consume 'J', queue is now "kl):ip4E/0M"

        // The sequence to search for is the original string, which is guaranteed not to be found.
        String sequenceToFind = originalData;
        String expectedConsumedString = "kl):ip4E/0M";

        // Act: Call the method under test.
        String consumed = queue.consumeTo(sequenceToFind);

        // Assert: The method should have consumed the rest of the queue.
        assertEquals(expectedConsumedString, consumed);
        assertTrue("The queue should be empty after consuming to the end", queue.isEmpty());
    }
}