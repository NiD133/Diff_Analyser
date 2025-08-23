package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link IOCase} enum.
 */
public class IOCaseTest {

    /**
     * Tests that checkRegionMatches returns true for an exact, case-sensitive match.
     */
    @Test
    public void checkRegionMatches_sensitive_whenRegionsMatch_shouldReturnTrue() {
        // Arrange
        final IOCase sensitiveCase = IOCase.SENSITIVE;
        final String text = "NUL";
        final String search = "NUL";
        final int startIndex = 0;

        // Act
        final boolean isMatch = sensitiveCase.checkRegionMatches(text, startIndex, search);

        // Assert
        assertTrue("Expected a match for identical strings with case-sensitive comparison.", isMatch);
    }
}