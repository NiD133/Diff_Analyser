package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * This class contains tests for the {@link IOCase} enum.
 */
public class IOCaseTest {

    /**
     * Tests that {@link IOCase#checkStartsWith(String, String)} returns false
     * when the string does not begin with the specified prefix, using SYSTEM sensitivity.
     * The result should be false regardless of the actual operating system's case sensitivity.
     */
    @Test
    public void checkStartsWithShouldReturnFalseForNonMatchingPrefixWithSystemSensitivity() {
        // Arrange
        final IOCase systemCase = IOCase.SYSTEM;
        final String text = "Apache Commons IO";
        final String nonMatchingPrefix = "WrongPrefix";

        // Act
        final boolean startsWith = systemCase.checkStartsWith(text, nonMatchingPrefix);

        // Assert
        assertFalse("Expected the string not to start with the non-matching prefix.", startsWith);
    }
}