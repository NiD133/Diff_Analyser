package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the inner enum {@link Separators.Spacing}.
 */
public class SeparatorsTest {

    /**
     * Verifies that the {@code spacesAfter()} method for the {@code BOTH}
     * spacing option returns a single space.
     */
    @Test
    public void spacesAfter_whenSpacingIsBoth_shouldReturnSpace() {
        // Arrange: Define the expected behavior and inputs.
        Separators.Spacing spacing = Separators.Spacing.BOTH;
        String expectedSpace = " ";

        // Act: Execute the method under test.
        String actualSpace = spacing.spacesAfter();

        // Assert: Verify the result is as expected.
        assertEquals("The space after for BOTH should be a single space.", expectedSpace, actualSpace);
    }
}