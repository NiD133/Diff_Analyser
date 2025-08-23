package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link IOCase} enum.
 */
public class IOCaseTest {

    /**
     * Tests that IOCase.valueOf("INSENSITIVE") correctly resolves to the
     * INSENSITIVE enum and that its getName() method returns "Insensitive".
     */
    @Test
    public void testValueOfInsensitiveReturnsCorrectName() {
        // Arrange
        final String enumString = "INSENSITIVE";
        final String expectedName = "Insensitive";

        // Act
        final IOCase insensitiveCase = IOCase.valueOf(enumString);
        final String actualName = insensitiveCase.getName();

        // Assert
        assertEquals(expectedName, actualName);
    }
}