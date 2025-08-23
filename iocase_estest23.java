package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Contains tests for the {@link IOCase} enum.
 */
public class IOCaseTest {

    /**
     * Tests that IOCase.checkStartsWith() returns false when both the string
     * to check and the prefix are null.
     * <p>
     * The Javadoc for checkStartsWith states it should return false for any
     * null input, so this behavior should be consistent across all case-sensitivity
     * settings (SYSTEM, SENSITIVE, INSENSITIVE).
     * </p>
     */
    @Test
    public void testCheckStartsWithReturnsFalseForNullInputs() {
        // Arrange
        // The specific IOCase instance does not affect the outcome with null inputs.
        final IOCase ioCase = IOCase.SYSTEM;
        final String nullString = null;
        final String nullPrefix = null;

        // Act
        final boolean result = ioCase.checkStartsWith(nullString, nullPrefix);

        // Assert
        assertFalse("checkStartsWith(null, null) should return false", result);
    }
}