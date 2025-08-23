package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * A test suite for the {@link Entities} class, focusing on understandability and maintainability.
 */
public class EntitiesTest {

    /**
     * Verifies that {@link Entities#codepointsForName(String, int[])} correctly populates an array
     * with the single codepoint for a known named entity.
     */
    @Test
    public void codepointsForNameShouldPopulateArrayForSingleCodepointEntity() {
        // Arrange
        String entityName = "deg"; // The HTML entity for the degree symbol (°).
        int[] codepointBuffer = new int[5]; // An array to hold the resulting codepoints.

        // The expected result: the codepoint for '°' (176) followed by zeros,
        // as the buffer is larger than the number of codepoints.
        int[] expectedCodepoints = {176, 0, 0, 0, 0};

        // Act
        // The method populates the buffer and returns the number of codepoints found.
        int codepointCount = Entities.codepointsForName(entityName, codepointBuffer);

        // Assert
        // 1. Verify that one codepoint was found.
        assertEquals("Should find one codepoint for the 'deg' entity.", 1, codepointCount);
        
        // 2. Verify that the buffer was populated correctly.
        assertArrayEquals("The buffer should contain the correct codepoint for 'deg'.",
                expectedCodepoints, codepointBuffer);
    }
}