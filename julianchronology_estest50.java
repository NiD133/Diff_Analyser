package org.threeten.extra.chrono;

import org.junit.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link JulianChronology} class.
 */
public class JulianChronologyTest {

    /**
     * Tests that dateNow(Clock) returns the correct date for a fixed point in time.
     * This ensures the method correctly uses the provided clock, making the test deterministic.
     */
    @Test
    public void dateNow_withFixedClock_returnsCorrectDate() {
        // Arrange: Set up a fixed clock for a specific moment.
        // The ISO date 2014-02-15 corresponds to the Julian date 2014-02-02.
        Instant fixedInstant = Instant.parse("2014-02-15T10:15:30.00Z");
        Clock fixedClock = Clock.fixed(fixedInstant, ZoneOffset.UTC);
        
        JulianChronology julianChronology = JulianChronology.INSTANCE;
        
        // The expected JulianDate for the given instant.
        JulianDate expectedDate = JulianDate.of(2014, 2, 2);

        // Act: Call the method under test with the fixed clock.
        JulianDate actualDate = julianChronology.dateNow(fixedClock);

        // Assert: Verify that the returned date matches the expected date exactly.
        assertEquals(expectedDate, actualDate);
    }
}