package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link IOCase} enum.
 */
public class IOCaseTest {

    /**
     * Tests that {@link IOCase#checkEndsWith(String, String)} returns true
     * for a case-sensitive match when the string is identical to the suffix.
     */
    @Test
    public void checkEndsWith_sensitive_shouldReturnTrueForIdenticalString() {
        // Arrange
        final IOCase sensitiveCase = IOCase.SENSITIVE;
        final String text = "ApacheCommons.IO";
        final String suffix = "ApacheCommons.IO";

        // Act
        final boolean result = sensitiveCase.checkEndsWith(text, suffix);

        // Assert
        assertTrue("Expected the string to end with the suffix in a case-sensitive comparison", result);
    }
}