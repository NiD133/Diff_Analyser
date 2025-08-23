package org.threeten.extra;

import org.junit.Test;
import org.evosuite.runtime.mock.java.time.MockClock; // Retained from original test setup

import java.time.Clock;
import java.time.Duration;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

/**
 * Tests for {@link DayOfYear} focusing on edge cases for the now(Clock) method.
 */
public class DayOfYearTest {

    /**
     * Tests that DayOfYear.now(clock) throws an ArithmeticException when the clock's
     * instant is so far in the future that it causes a long overflow during
     * the conversion to an epoch day.
     */
    @Test(expected = ArithmeticException.class)
    public void now_withClockAtFarFuture_throwsArithmeticException() {
        // Arrange: Create a clock that represents an instant that will cause an overflow.
        // The `FOREVER` unit provides a duration large enough to exceed the maximum value
        // of a `long` when added to the current time's epoch seconds.
        Clock baseClock = MockClock.system(ZoneOffset.UTC);
        Duration extremelyLargeDuration = ChronoUnit.FOREVER.getDuration();
        Clock clockAtFarFuture = MockClock.offset(baseClock, extremelyLargeDuration);

        // Act: Attempt to get the DayOfYear from the far-future clock.
        // This is expected to throw an ArithmeticException internally during date calculation.
        DayOfYear.now(clockAtFarFuture);
    }
}