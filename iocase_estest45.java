package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link IOCase} enum.
 */
public class IOCaseTest {

    /**
     * Tests that IOCase.INSENSITIVE.toString() returns the correct string "Insensitive".
     */
    @Test
    public void toString_forInsensitive_returnsCorrectName() {
        // Arrange
        final IOCase insensitiveCase = IOCase.INSENSITIVE;
        final String expectedName = "Insensitive";

        // Act
        final String actualName = insensitiveCase.toString();

        // Assert
        assertEquals(expectedName, actualName);
    }
}