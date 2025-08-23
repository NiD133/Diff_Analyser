package org.threeten.extra.scale;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Contains tests for edge cases in {@link UtcInstant}.
 */
class UtcInstantTest {

    /**
     * Verifies that subtracting a duration from an instant at the maximum representable
     * Modified Julian Day throws an ArithmeticException due to an internal calculation overflow.
     */
    @Test
    void minus_fromInstantAtMaxDay_throwsArithmeticException() {
        // Arrange: Create an instant at the maximum possible Modified Julian Day.
        // This represents an extreme edge case for date calculations.
        final long nanoOfDay = 490L;
        UtcInstant instantAtMaxDay = UtcInstant.ofModifiedJulianDay(Long.MAX_VALUE, nanoOfDay);
        Duration smallDuration = Duration.ofNanos(nanoOfDay);

        // Act & Assert: Attempting to subtract the duration should trigger the exception.
        // We use assertThrows to clearly state that we expect an exception.
        ArithmeticException exception = assertThrows(
            ArithmeticException.class,
            () -> instantAtMaxDay.minus(smallDuration),
            "Subtracting from the maximum UtcInstant should cause an overflow."
        );

        // Optionally, assert the exception message for a more specific test.
        assertEquals("long overflow", exception.getMessage());
    }
}