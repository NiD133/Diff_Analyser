package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the static helper methods in {@link TokenQueue}.
 */
public class TokenQueueTest {

    /**
     * Tests that escaping an empty CSS identifier results in an empty string.
     * This is the expected behavior as there are no characters to escape.
     */
    @Test
    public void escapeCssIdentifierShouldReturnEmptyStringForEmptyInput() {
        // Arrange
        String emptyInput = "";

        // Act
        String result = TokenQueue.escapeCssIdentifier(emptyInput);

        // Assert
        assertEquals("Escaping an empty string should result in an empty string.", "", result);
    }
}