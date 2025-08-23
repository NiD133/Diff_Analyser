package org.apache.commons.lang3;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Unit tests for {@link org.apache.commons.lang3.RandomUtils}.
 */
public class RandomUtilsTest {

    /**
     * Tests that {@link RandomUtils#nextInt(int, int)} throws an IllegalArgumentException
     * when the start value is greater than the end value, which constitutes an invalid range.
     */
    @Test
    public void nextIntShouldThrowIllegalArgumentExceptionWhenStartIsGreaterThanEnd() {
        // Arrange: Define an invalid range where the start is greater than the end.
        final int startInclusive = 0;
        final int endExclusive = -114;
        final String expectedErrorMessage = "Start value must be smaller or equal to end value.";

        // Act & Assert: Call the method and verify that the correct exception is thrown.
        // The assertThrows method is a modern and expressive way to test for exceptions.
        final IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> RandomUtils.nextInt(startInclusive, endExclusive)
        );

        // Assert: Further verify that the exception message is as expected.
        assertEquals(expectedErrorMessage, thrown.getMessage());
    }
}