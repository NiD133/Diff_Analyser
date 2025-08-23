package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Tests for the {@link IOCase} class.
 */
public class IOCaseTest {

    /**
     * Tests that checkRegionMatches returns false when both string inputs are null.
     * This behavior should be consistent regardless of the case-sensitivity rule.
     */
    @Test
    public void checkRegionMatchesShouldReturnFalseWhenBothStringsAreNull() {
        // Arrange
        final IOCase ioCase = IOCase.SYSTEM; // The specific IOCase shouldn't matter for null inputs.
        final int arbitraryStartIndex = 0;

        // Act
        final boolean matches = ioCase.checkRegionMatches(null, arbitraryStartIndex, null);

        // Assert
        assertFalse("checkRegionMatches should return false when both the string to check and the search string are null.", matches);
    }
}