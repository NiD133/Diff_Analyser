package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link IOCase} enum.
 */
public class IOCaseTest {

    /**
     * Tests that IOCase.INSENSITIVE correctly identifies two strings
     * as equal, regardless of their case.
     */
    @Test
    public void checkEqualsWithInsensitiveReturnsTrueForMixedCaseStrings() {
        // Arrange
        final String string1 = "System";
        final String string2 = "SYSTEM";

        // Act & Assert
        // The checkEquals method should return true because the comparison is case-insensitive.
        assertTrue("Strings that differ only in case should be considered equal",
                   IOCase.INSENSITIVE.checkEquals(string1, string2));
    }
}