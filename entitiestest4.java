package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link Entities} class.
 */
public class EntitiesTest {

    /**
     * Verifies that calling Entities.unescape with an empty string
     * correctly returns an empty string.
     */
    @Test
    public void unescape_withEmptyString_shouldReturnEmptyString() {
        // Arrange
        String input = "";
        String expectedOutput = "";

        // Act
        // The 'strict' parameter is false, meaning no trailing semicolon is required for entities.
        String actualOutput = Entities.unescape(input, false);

        // Assert
        assertEquals("Unescaping an empty string should result in an empty string.", expectedOutput, actualOutput);
    }
}