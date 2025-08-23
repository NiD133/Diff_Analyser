package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link IOCase} enum.
 */
public class IOCaseTest {

    /**
     * Verifies that IOCase.INSENSITIVE.checkEquals() returns true for two strings
     * that differ only in their character case. This confirms the case-insensitive
     * comparison logic.
     */
    @Test
    public void checkEqualsWithInsensitiveShouldReturnTrueForStringsDifferingOnlyInCase() {
        // Arrange
        final String string1 = "Apache Commons IO";
        final String string2 = "apache commons io";
        final IOCase insensitiveCase = IOCase.INSENSITIVE;

        // Act
        final boolean areEqual = insensitiveCase.checkEquals(string1, string2);

        // Assert
        assertTrue(
            "Strings differing only in case should be considered equal for IOCase.INSENSITIVE",
            areEqual
        );
    }
}