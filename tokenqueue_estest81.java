package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for {@link TokenQueue}.
 */
public class TokenQueueTest {

    @Test
    public void toStringShouldReturnTheEntireContentsOfTheQueue() {
        // Arrange
        String initialData = ">'9sOEbGPT9N3{HK5";
        TokenQueue queue = new TokenQueue(initialData);

        // Act
        String queueAsString = queue.toString();

        // Assert
        assertEquals("The toString() method should return the original, unconsumed content of the queue.",
            initialData, queueAsString);
    }
}