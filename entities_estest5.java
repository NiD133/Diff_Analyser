package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link Entities} class.
 */
public class EntitiesTest {

    /**
     * Verifies that unescaping an empty string results in an empty string.
     * This tests the base case for the unescape functionality.
     */
    @Test
    public void unescape_withEmptyInput_returnsEmptyString() {
        // Arrange
        String input = "";
        String expectedOutput = "";

        // Act
        String actualOutput = Entities.unescape(input);

        // Assert
        assertEquals("Unescaping an empty string should yield an empty string.", expectedOutput, actualOutput);
    }
}