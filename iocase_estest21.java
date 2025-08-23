package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link IOCase} enum.
 */
public class IOCaseTest {

    /**
     * Tests that {@link IOCase#checkStartsWith(String, String)} returns true
     * for identical strings when using case-insensitive comparison.
     */
    @Test
    public void checkStartsWith_insensitive_returnsTrueForIdenticalStrings() {
        // Arrange
        final IOCase insensitiveCase = IOCase.INSENSITIVE;
        final String text = "Apache Commons IO";
        final String prefix = "Apache Commons IO";

        // Act
        final boolean result = insensitiveCase.checkStartsWith(text, prefix);

        // Assert
        assertTrue("Expected the string to be considered starting with itself.", result);
    }
}