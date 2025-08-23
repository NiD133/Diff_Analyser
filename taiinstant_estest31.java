package org.threeten.extra.scale;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Test class for {@link TaiInstant}.
 * This focuses on improving a specific test case for understandability.
 */
public class TaiInstantTest {

    /**
     * Tests that {@link TaiInstant#ofTaiSeconds(long, long)} throws an
     * {@link ArithmeticException} when the calculation of total seconds
     * results in a long overflow.
     */
    @Test
    public void ofTaiSeconds_whenSecondsAndNanosSumOverflows_throwsArithmeticException() {
        // Arrange: Use Long.MAX_VALUE for both seconds and nanosecond adjustment.
        // The internal implementation of ofTaiSeconds converts the nanoAdjustment to
        // seconds and adds it to the taiSeconds parameter. This addition is
        // designed to overflow when using Math.addExact.
        long maxSeconds = Long.MAX_VALUE;
        long maxNanos = Long.MAX_VALUE;

        // Act & Assert: Verify that the expected exception is thrown and check its message.
        ArithmeticException exception = assertThrows(
            "An ArithmeticException should be thrown for long overflow",
            ArithmeticException.class,
            () -> TaiInstant.ofTaiSeconds(maxSeconds, maxNanos)
        );

        // Assert: Verify the exception message for more specific testing.
        // The message "long overflow" is thrown by java.lang.Math.addExact.
        assertEquals("long overflow", exception.getMessage());
    }
}