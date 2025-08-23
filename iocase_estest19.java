package org.apache.commons.io;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the {@link IOCase} enum.
 */
public class IOCaseTest {

    /**
     * Tests that IOCase.INSENSITIVE.getName() returns the expected string "Insensitive".
     */
    @Test
    public void getName_whenCaseIsInsensitive_shouldReturnCorrectName() {
        // Arrange
        final IOCase insensitiveCase = IOCase.INSENSITIVE;
        final String expectedName = "Insensitive";

        // Act
        final String actualName = insensitiveCase.getName();

        // Assert
        assertEquals(expectedName, actualName);
    }
}