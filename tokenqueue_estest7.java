package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for the {@link TokenQueue} class.
 */
public class TokenQueueTest {

    /**
     * Verifies that the remainder() method consumes the entire queue.
     * A subsequent call to remainder() on the same queue should return an empty string.
     */
    @Test
    public void remainderConsumesTheRestOfTheQueue() {
        // Arrange: Create a TokenQueue with some initial data.
        String initialData = "d:,&:wx";
        TokenQueue tq = new TokenQueue(initialData);

        // Act: Call remainder() twice to get the initial and subsequent content.
        String firstRemainder = tq.remainder();
        String secondRemainder = tq.remainder();

        // Assert: Check that the first call returned the full string and the second returned empty.
        assertEquals("The first call to remainder() should return the full initial string.", initialData, firstRemainder);
        assertEquals("The second call to remainder() should return an empty string.", "", secondRemainder);
        assertTrue("The queue should be empty after remainder() is called.", tq.isEmpty());
    }
}