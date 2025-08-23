package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Tests for the {@link IOCase} enum.
 */
public class IOCaseTest {

    /**
     * Tests that checkEquals() returns false for two completely different strings
     * when using case-insensitive comparison.
     */
    @Test
    public void checkEqualsWithInsensitiveShouldReturnFalseForDifferentStrings() {
        // Arrange
        final IOCase insensitiveComparison = IOCase.INSENSITIVE;
        final String string1 = "Apple";
        final String string2 = "Banana";

        // Act
        final boolean result = insensitiveComparison.checkEquals(string1, string2);

        // Assert
        assertFalse("Expected two different strings to not be equal, even with case-insensitivity.", result);
    }
}