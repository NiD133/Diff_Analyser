package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link IOCase} enum.
 */
public class IOCaseTest {

    /**
     * Tests that checkRegionMatches() returns false when the starting index is out of bounds.
     * The behavior should be consistent regardless of case sensitivity.
     */
    @Test
    public void checkRegionMatchesShouldReturnFalseForOutOfBoundsStartIndex() {
        // Arrange
        final IOCase sensitiveCase = IOCase.SENSITIVE;
        final String text = "A sample string";
        final String searchString = "sample";
        // An index deliberately set far beyond the bounds of the 'text' string.
        final int outOfBoundsIndex = 100;

        // Act
        final boolean isMatch = sensitiveCase.checkRegionMatches(text, outOfBoundsIndex, searchString);

        // Assert
        assertFalse("checkRegionMatches should return false when the start index is out of bounds", isMatch);
    }
}