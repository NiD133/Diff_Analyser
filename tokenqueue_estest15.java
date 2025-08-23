package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link TokenQueue} class.
 */
public class TokenQueueTest {

    @Test
    public void currentShouldPeekAtFirstCharacterWithoutConsuming() {
        // Arrange
        String input = "$dXr'&T^\"0qAav";
        TokenQueue queue = new TokenQueue(input);

        // Act
        char firstCharacter = queue.current();

        // Assert
        assertEquals("The first character should be '$'", '$', firstCharacter);
        assertEquals("Calling current() should not advance the queue", input, queue.toString());
    }
}