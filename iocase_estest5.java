package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

/**
 * Tests for the {@link IOCase} enum.
 */
public class IOCaseTest {

    /**
     * Tests that IOCase.values() returns all defined enum constants in the correct
     * declaration order. This ensures that no constants are accidentally added or
     * removed without updating the tests.
     */
    @Test
    public void values_shouldReturnAllConstantsInDeclarationOrder() {
        // Arrange
        final IOCase[] expectedCases = {IOCase.SENSITIVE, IOCase.INSENSITIVE, IOCase.SYSTEM};

        // Act
        final IOCase[] actualCases = IOCase.values();

        // Assert
        assertArrayEquals("The values() method should return all IOCase constants in their declaration order.",
                          expectedCases, actualCases);
    }
}