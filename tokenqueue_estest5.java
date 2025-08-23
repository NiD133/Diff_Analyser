package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the static methods of {@link TokenQueue}.
 */
public class TokenQueueTest {

    @Test
    public void unescape_shouldReturnEmptyString_whenInputIsEmpty() {
        // Arrange
        String input = "";
        String expected = "";

        // Act
        String result = TokenQueue.unescape(input);

        // Assert
        assertEquals(expected, result);
    }
}