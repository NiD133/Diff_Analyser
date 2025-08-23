package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Contains tests for the {@link IOCase} enum.
 */
public class IOCaseTest {

    /**
     * Verifies that checkRegionMatches returns false when provided with a negative start index.
     * The underlying String.regionMatches method, which this method delegates to,
     * is specified to return false for any negative start index.
     */
    @Test
    public void checkRegionMatchesShouldReturnFalseForNegativeStartIndex() {
        // Arrange
        IOCase insensitiveCase = IOCase.INSENSITIVE;
        String text = "AnyString";
        String search = "Any";
        int negativeStartIndex = -1;

        // Act
        boolean matches = insensitiveCase.checkRegionMatches(text, negativeStartIndex, search);

        // Assert
        assertFalse("A negative start index should always result in false.", matches);
    }
}