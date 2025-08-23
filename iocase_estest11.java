package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link IOCase} enum.
 * This class focuses on improving the understandability of a specific test case.
 */
public class IOCase_ESTestTest11 {

    /**
     * Tests that {@link IOCase#checkCompareTo(String, String)} returns a negative value
     * when the first string is lexicographically smaller than the second.
     * This test uses {@code IOCase.SYSTEM}, which adapts its behavior based on the
     * underlying operating system's file system case-sensitivity. The chosen test
     * strings ensure the test passes on both case-sensitive (e.g., Linux) and
     * case-insensitive (e.g., Windows) systems.
     */
    @Test
    public void checkCompareToWithSystemCaseReturnsNegativeForLexicographicallySmallerString() {
        // Arrange
        final IOCase systemCase = IOCase.SYSTEM;
        final String smallerString = "Apple";
        final String largerString = "Banana";

        // Act
        final int comparisonResult = systemCase.checkCompareTo(smallerString, largerString);

        // Assert
        assertTrue(
            "Expected a negative result when comparing a lexicographically smaller string to a larger one.",
            comparisonResult < 0
        );
    }
}