package org.threeten.extra;

import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link DayOfYear}.
 */
public class DayOfYearTest {

    /**
     * Tests that {@link DayOfYear#now(Clock)} throws a DateTimeException
     * when the provided clock's instant is outside the valid range supported by java.time.
     */
    @Test
    void nowWithClock_whenInstantIsOutOfRange_throwsException() {
        // Arrange: Create a clock with an instant that is far in the future,
        // exceeding the supported range of java.time.Instant.
        Clock baseClock = Clock.system(ZoneOffset.UTC);
        
        // The duration of an ERA is extremely large and guaranteed to create an out-of-range Instant.
        Duration extremelyLargeDuration = ChronoUnit.ERAS.getDuration();
        Clock outOfRangeClock = Clock.offset(baseClock, extremelyLargeDuration);

        // Act & Assert: Calling now() with the out-of-range clock should throw an exception.
        // The DayOfYear.now(clock) method internally creates a LocalDate, which fails if the
        // clock's instant is too large or small.
        DateTimeException exception = assertThrows(
                DateTimeException.class,
                () -> DayOfYear.now(outOfRangeClock)
        );

        // Verify the exception message to ensure it's the one we expect.
        assertTrue(
                exception.getMessage().contains("Instant exceeds minimum or maximum instant"),
                "Exception message should indicate that the instant is out of range."
        );
    }
}