package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link IOCase} enum, focusing on its comparison logic.
 */
public class IOCaseTest {

    /**
     * Verifies that {@code checkCompareTo()} returns 0 when comparing a string
     * to itself using case-insensitive rules, indicating equality.
     */
    @Test
    public void checkCompareTo_insensitiveWithIdenticalStrings_shouldReturnZero() {
        // Arrange
        final IOCase caseInsensitive = IOCase.INSENSITIVE;
        final String filename = "MyTestFile.txt";

        // Act
        final int comparisonResult = caseInsensitive.checkCompareTo(filename, filename);

        // Assert
        assertEquals("Comparing a string to itself should always result in 0.", 0, comparisonResult);
    }
}