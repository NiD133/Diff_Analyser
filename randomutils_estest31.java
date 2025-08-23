package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * Contains tests for the {@link RandomUtils} class, focusing on edge cases and exception handling.
 */
public class RandomUtilsTest {

    /**
     * Verifies that RandomUtils.nextLong() throws an IllegalArgumentException
     * when the starting bound is greater than the ending bound.
     */
    @Test
    public void nextLong_shouldThrowIllegalArgumentException_whenStartIsGreaterThanEnd() {
        // Arrange: Define an invalid range where the start value is greater than the end value.
        final long startInclusive = 920L;
        final long endExclusive = 32L;
        final String expectedErrorMessage = "Start value must be smaller or equal to end value.";

        // Act & Assert: Execute the method and verify that the correct exception is thrown.
        final IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> RandomUtils.nextLong(startInclusive, endExclusive)
        );

        // Assert: Further verify that the exception message is as expected.
        assertEquals(expectedErrorMessage, thrown.getMessage());
    }
}