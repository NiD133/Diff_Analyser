package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link TokenQueue} class.
 */
public class TokenQueueTest {

    @Test
    public void current_shouldReturnFirstCharacter_whenQueueIsNotEmpty() {
        // Arrange
        String inputData = "8Nw@t]UFUu54QXB.";
        TokenQueue queue = new TokenQueue(inputData);

        // Act
        char firstChar = queue.current();

        // Assert
        assertEquals("The current() method should peek at the first character without consuming it.", '8', firstChar);
    }
}