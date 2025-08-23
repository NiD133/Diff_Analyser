package org.threeten.extra.chrono;

import org.junit.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link JulianChronology#dateNow(Clock)}.
 */
public class JulianChronologyTest {

    /**
     * Tests that dateNow(Clock) returns the correct JulianDate for a given fixed instant.
     * This ensures the method correctly converts the instant provided by the clock
     * into the corresponding date in the Julian calendar system.
     */
    @Test
    public void dateNow_withFixedClock_returnsCorrectJulianDate() {
        // Arrange: Set up a fixed clock for a specific instant.
        // The Gregorian date is 2014-02-15 UTC.
        // The corresponding Julian date is 2014-02-02.
        final JulianChronology chronology = JulianChronology.INSTANCE;
        final Instant fixedInstant = LocalDateTime.of(2014, 2, 15, 12, 30, 0).toInstant(ZoneOffset.UTC);
        final Clock fixedClock = Clock.fixed(fixedInstant, ZoneOffset.UTC);
        
        JulianDate expectedDate = JulianDate.of(2014, 2, 2);

        // Act: Get the current date from the chronology using the fixed clock.
        JulianDate actualDate = chronology.dateNow(fixedClock);

        // Assert: The returned date must match the expected Julian date.
        assertEquals("The date should be correctly converted from the clock's instant", expectedDate, actualDate);
    }
}