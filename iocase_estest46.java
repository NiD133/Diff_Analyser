package org.apache.commons.io;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

/**
 * Unit tests for the {@link IOCase} enum.
 */
public class IOCaseTest {

    /**
     * Tests that IOCase.isCaseSensitive() returns false for the INSENSITIVE constant.
     */
    @Test
    public void isCaseSensitive_forInsensitive_shouldReturnFalse() {
        // Arrange: Define the object under test. Using the enum constant directly
        // is clearer and more type-safe than using IOCase.forName("Insensitive").
        final IOCase insensitiveCase = IOCase.INSENSITIVE;

        // Act: Call the method being tested.
        final boolean isSensitive = insensitiveCase.isCaseSensitive();

        // Assert: Verify the result is as expected.
        assertFalse("IOCase.INSENSITIVE should not be case-sensitive.", isSensitive);
    }
}