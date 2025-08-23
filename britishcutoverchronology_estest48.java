package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link BritishCutoverChronology}.
 */
public class BritishCutoverChronologyTest {

    /**
     * Verifies that dateNow(Clock) returns the correct date based on the state of the provided clock.
     * This test uses a fixed clock to ensure the test is deterministic and its outcome is predictable.
     */
    @Test
    public void dateNow_withFixedClock_shouldReturnCorrectDate() {
        // Arrange: Set up a fixed clock for a specific, known date (e.g., 2023-10-26 UTC).
        // Using the singleton instance is the recommended practice.
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;
        
        ZoneId utc = ZoneId.of("UTC");
        Instant fixedInstant = LocalDate.of(2023, 10, 26).atStartOfDay(utc).toInstant();
        Clock fixedClock = Clock.fixed(fixedInstant, utc);

        // The expected date in the BritishCutoverChronology for the given instant.
        BritishCutoverDate expectedDate = BritishCutoverDate.of(2023, 10, 26);

        // Act: Call the method under test to get the "current" date from our fixed clock.
        BritishCutoverDate actualDate = chronology.dateNow(fixedClock);

        // Assert: Verify that the returned date is exactly what we expect.
        assertEquals("The date should match the date of the fixed clock", expectedDate, actualDate);
    }
}