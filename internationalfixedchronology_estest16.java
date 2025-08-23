package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link InternationalFixedChronology} class.
 */
public class InternationalFixedChronologyTest {

    /**
     * Tests that {@code dateNow(Clock)} returns the correct date when the clock is set
     * to the moment just before the Unix epoch.
     */
    @Test
    public void dateNow_withClockSetToJustBeforeEpoch_returnsCorrectDate() {
        // Arrange: Set up a fixed clock for one millisecond before the Unix epoch.
        // This corresponds to 1969-12-31T23:59:59.999Z. The date part is 1969-12-31.
        Instant instantBeforeEpoch = Instant.ofEpochMilli(-1L);
        Clock fixedClock = Clock.fixed(instantBeforeEpoch, ZoneOffset.UTC);
        InternationalFixedChronology ifcChronology = InternationalFixedChronology.INSTANCE;

        // Act: Obtain the current date from the chronology using the fixed clock.
        InternationalFixedDate date = ifcChronology.dateNow(fixedClock);

        // Assert: Verify that the obtained date corresponds to the day before the epoch.
        // The epoch day for 1970-01-01 is 0, so for 1969-12-31 it should be -1.
        assertEquals("The epoch day should be -1", -1L, date.toEpochDay());

        // The year 1969 is not a leap year, so it should have 365 days.
        assertEquals("The length of the year 1969 should be 365", 365, date.lengthOfYear());
    }
}