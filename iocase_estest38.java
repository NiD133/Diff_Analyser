package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * This test class contains tests for the {@link IOCase} enum.
 * This specific test case was refactored for clarity from a machine-generated version.
 */
public class IOCaseTest {

    /**
     * Tests that checkEndsWith() returns false when the suffix argument is null.
     * The Javadoc for checkEndsWith specifies this behavior, and it should be
     * consistent regardless of the case-sensitivity rule.
     */
    @Test
    public void checkEndsWithShouldReturnFalseWhenSuffixIsNull() {
        // Arrange
        final IOCase ioCase = IOCase.INSENSITIVE;
        final String text = "Apache Commons IO";

        // Act
        final boolean result = ioCase.checkEndsWith(text, null);

        // Assert
        assertFalse("checkEndsWith must return false if the suffix is null.", result);
    }
}