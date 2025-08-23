package org.threeten.extra;

import org.junit.Test;
import java.time.Clock;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;

/**
 * Contains tests for the {@link DayOfMonth#now(Clock)} method.
 */
public class DayOfMonthTest {

    /**
     * Tests that DayOfMonth.now(clock) throws a DateTimeException if the clock's
     * instant is outside the valid range supported by java.time.Instant.
     */
    @Test(expected = DateTimeException.class)
    public void nowWithClock_whenInstantIsOutOfRange_throwsDateTimeException() {
        // Arrange: Create a clock that represents an instant just beyond the maximum
        // representable Instant. The DayOfMonth.now(clock) method internally calls
        // clock.instant(), which will throw an exception if the resulting instant is out of bounds.
        Clock baseClock = Clock.fixed(Instant.MAX, ZoneOffset.UTC);
        Clock outOfRangeClock = Clock.offset(baseClock, Duration.ofNanos(1));

        // Act: Call now() with the out-of-range clock.
        // Assert: A DateTimeException is expected, as declared in the @Test annotation.
        DayOfMonth.now(outOfRangeClock);
    }
}