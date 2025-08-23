package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link IOCase} enum.
 */
public class IOCaseTest {

    /**
     * Tests that IOCase.checkEquals() returns true when comparing two empty strings.
     * This behavior should be consistent regardless of the case-sensitivity rule (SYSTEM, SENSITIVE, or INSENSITIVE).
     */
    @Test
    public void checkEquals_withTwoEmptyStrings_shouldReturnTrue() {
        // Arrange
        IOCase systemCase = IOCase.SYSTEM;

        // Act & Assert
        assertTrue("Two empty strings should always be considered equal", systemCase.checkEquals("", ""));
    }
}