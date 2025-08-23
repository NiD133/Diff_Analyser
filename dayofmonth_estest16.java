package org.threeten.extra;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.time.Clock;
import org.junit.Test;

/**
 * Tests for the factory methods of {@link DayOfMonth}.
 */
public class DayOfMonthTest {

    /**
     * Tests that DayOfMonth.now(Clock) throws a NullPointerException when the clock is null.
     * The method's contract explicitly forbids a null clock argument.
     */
    @Test
    public void now_withNullClock_throwsNullPointerException() {
        // The Javadoc for now(Clock) specifies the clock must not be null.
        // We expect a NullPointerException, which is standard for null argument violations.
        NullPointerException exception = assertThrows(
            NullPointerException.class,
            () -> DayOfMonth.now((Clock) null)
        );

        // Verify the exception message to confirm the null check is for the 'clock' parameter.
        // This is the message produced by Objects.requireNonNull(clock, "clock").
        assertEquals("clock", exception.getMessage());
    }
}