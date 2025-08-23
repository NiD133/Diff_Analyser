package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link IOCase} enum.
 */
public class IOCaseTest {

    /**
     * Tests that checkCompareTo with IOCase.INSENSITIVE performs a case-insensitive
     * comparison, returning a positive value when the first string is lexicographically
     * greater than the second.
     */
    @Test
    public void checkCompareTo_insensitive_returnsPositiveForGreaterString() {
        // Arrange
        final IOCase caseInsensitive = IOCase.INSENSITIVE;
        final String greaterString = "Cat";
        final String smallerString = "Bat"; // 'C' comes after 'B' regardless of case

        // Act
        final int result = caseInsensitive.checkCompareTo(greaterString, smallerString);

        // Assert
        assertTrue(
            "Expected a positive result when the first string is lexicographically greater (case-insensitive).",
            result > 0
        );
    }
}