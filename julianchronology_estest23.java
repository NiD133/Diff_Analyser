package org.threeten.extra.chrono;

import org.junit.Test;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link JulianChronology}.
 */
public class JulianChronologyTest {

    /**
     * Tests that {@code dateNow(Clock)} propagates an {@code ArithmeticException}
     * when the provided clock's instant is so large that it causes a long overflow.
     */
    @Test
    public void dateNow_whenClockInstantOverflows_throwsArithmeticException() {
        // Arrange: Create a clock that represents a time so far in the future
        // that calculating its instant will cause a numeric overflow.
        // ChronoUnit.FOREVER provides a duration that is effectively infinite.
        JulianChronology julianChronology = JulianChronology.INSTANCE;
        Clock baseClock = Clock.fixed(Instant.EPOCH, ZoneOffset.UTC);
        Duration effectivelyInfiniteDuration = ChronoUnit.FOREVER.getDuration();
        Clock overflowingClock = Clock.offset(baseClock, effectivelyInfiniteDuration);

        // Act & Assert: Verify that dateNow throws the expected ArithmeticException.
        // The exception originates from java.time.Instant when adding the huge duration.
        ArithmeticException exception = assertThrows(
            ArithmeticException.class,
            () -> julianChronology.dateNow(overflowingClock)
        );

        // Further assert that the exception message indicates an overflow,
        // ensuring we've caught the correct error.
        assertTrue(exception.getMessage().toLowerCase().contains("overflow"));
    }
}