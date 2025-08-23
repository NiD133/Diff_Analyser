package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the inner enum {@link Separators.Spacing}.
 */
public class SeparatorsSpacingTest {

    /**
     * Verifies that {@link Separators.Spacing#BOTH} correctly applies a single
     * space both before and after the given separator character.
     */
    @Test
    public void apply_withBothSpacing_shouldAddSpaceBeforeAndAfterSeparator() {
        // Arrange: Define the spacing type and the input character.
        Separators.Spacing spacing = Separators.Spacing.BOTH;
        char separatorChar = 'p';
        String expectedOutput = " p ";

        // Act: Apply the spacing to the character.
        String actualOutput = spacing.apply(separatorChar);

        // Assert: Check if the output matches the expected format.
        assertEquals(expectedOutput, actualOutput);
    }
}