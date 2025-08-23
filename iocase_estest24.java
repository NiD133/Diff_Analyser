package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Contains tests for the {@link IOCase} enum.
 */
public class IOCaseTest {

    /**
     * Tests that {@code checkRegionMatches} correctly returns false when called with
     * invalid arguments, specifically a negative start index and a null search string.
     * This ensures the method is robust against improper inputs.
     */
    @Test
    public void checkRegionMatchesShouldReturnFalseForNegativeStartIndexAndNullSearchString() {
        // Arrange
        IOCase ioCase = IOCase.SYSTEM;
        String text = "System";
        int negativeStartIndex = -1121;
        String searchString = null;

        // Act
        boolean matches = ioCase.checkRegionMatches(text, negativeStartIndex, searchString);

        // Assert
        assertFalse("Expected checkRegionMatches to return false for invalid inputs", matches);
    }
}