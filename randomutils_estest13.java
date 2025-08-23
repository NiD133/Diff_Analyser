package org.apache.commons.lang3;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests for {@link RandomUtils} focusing on exception-throwing behavior for invalid inputs.
 */
public class RandomUtilsTest {

    /**
     * Tests that randomLong() throws an IllegalArgumentException when the starting
     * range value is negative, as the range must be non-negative.
     */
    @Test
    public void randomLongShouldThrowIllegalArgumentExceptionWhenStartIsNegative() {
        // Arrange: Create a RandomUtils instance and define an invalid range
        // with a negative start value.
        final RandomUtils randomUtils = RandomUtils.secureStrong();
        final long negativeStartInclusive = -10L;
        final long endExclusive = -5L;

        // Act & Assert: Verify that the call throws the expected exception.
        // The assertThrows method is a clean way to test for exceptions.
        final IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> randomUtils.randomLong(negativeStartInclusive, endExclusive)
        );

        // Assert: Further verify that the exception message is correct,
        // ensuring the right validation is triggered.
        final String expectedMessage = "must be non-negative";
        assertTrue(
            "Exception message should indicate that range values must be non-negative.",
            thrown.getMessage().contains(expectedMessage)
        );
    }
}