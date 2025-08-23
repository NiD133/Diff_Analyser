package org.threeten.extra.chrono;

import org.junit.Test;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Tests for {@link BritishCutoverChronology}.
 * This test focuses on edge cases related to time representation limits.
 */
public class BritishCutoverChronologyTest {

    /**
     * Tests that dateNow() throws an ArithmeticException when the provided Clock's
     * instant is too large to be represented.
     *
     * The dateNow(Clock) method internally calculates an epoch-day from the clock's instant.
     * If the instant represents a time far enough in the future, this calculation
     * will overflow a long, resulting in an ArithmeticException.
     */
    @Test
    public void dateNow_whenClockInstantOverflows_throwsArithmeticException() {
        // ARRANGE
        // The chronology under test.
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;

        // Create a clock that represents a time so far in the future it will cause an overflow.
        // We use a fixed base clock for determinism and offset it by a near-maximum duration.
        // The resulting instant's epoch-second value will exceed Long.MAX_VALUE.
        Clock baseClock = Clock.fixed(Instant.parse("2000-01-01T00:00:00Z"), ZoneOffset.UTC);
        Duration hugeDuration = Duration.ofSeconds(Long.MAX_VALUE);
        Clock overflowClock = Clock.offset(baseClock, hugeDuration);

        // ACT & ASSERT
        // The call to dateNow() should fail when trying to process the overflowing instant.
        // We use assertThrows for a clear and concise declaration of the expected exception.
        ArithmeticException exception = assertThrows(
                ArithmeticException.class,
                () -> chronology.dateNow(overflowClock)
        );

        // Verify the exception message to confirm it's the expected overflow.
        // This message comes from java.lang.Math.addExact, used by Instant.plus().
        assertEquals("long overflow", exception.getMessage());
    }
}