package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * A test suite for the {@link Seconds} class, focusing on its arithmetic operations.
 */
public class SecondsTest {

    /**
     * Verifies that the minus() method throws an ArithmeticException when the calculation
     * results in an integer overflow.
     *
     * This is tested by subtracting a negative number from Seconds.MAX_VALUE. This operation
     * is equivalent to an addition that exceeds Integer.MAX_VALUE (e.g., `Integer.MAX_VALUE - (-1)`),
     * which should safely fail by throwing an exception.
     */
    @Test
    public void minus_throwsArithmeticException_onIntegerOverflow() {
        // Arrange: Start with the maximum possible Seconds value.
        // Subtracting -1 will attempt to compute MAX_VALUE + 1, causing an overflow.
        final Seconds maxSeconds = Seconds.MAX_VALUE;
        final int valueToSubtract = -1;

        // Act & Assert: Verify that an ArithmeticException is thrown.
        try {
            maxSeconds.minus(valueToSubtract);
            fail("Expected an ArithmeticException to be thrown due to integer overflow.");
        } catch (ArithmeticException e) {
            // This is the expected outcome.
            // We can optionally verify the exception message for more specific feedback.
            // The underlying Joda-Time utility throws a message containing "overflow".
            assertTrue(
                "The exception message should indicate an overflow.",
                e.getMessage().contains("The calculation caused an overflow")
            );
        }
    }
}