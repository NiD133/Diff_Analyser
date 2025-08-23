package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link TokenQueue}.
 */
public class TokenQueueTest {

    @Test
    public void consumeToShouldReturnEmptyStringWhenDelimiterIsAtStartOfQueue() {
        // Arrange
        String contentAndDelimiter = "%>-8wJ/e4'{T9H";
        TokenQueue queue = new TokenQueue(contentAndDelimiter);

        // Act
        // Attempt to consume all characters *up to* the delimiter.
        String consumed = queue.consumeTo(contentAndDelimiter);

        // Assert
        // Since the delimiter is at the very beginning of the queue, nothing is consumed.
        assertEquals("Should consume nothing if the delimiter is at the start", "", consumed);
        
        // The queue's state should remain unchanged because the delimiter was not consumed.
        assertEquals("Queue should be unchanged", contentAndDelimiter, queue.remainder());
    }
}