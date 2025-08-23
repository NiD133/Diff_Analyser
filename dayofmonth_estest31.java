package org.threeten.extra;

import org.junit.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.chrono.Chronology;
import java.time.chrono.IsoChronology;

import static org.junit.Assert.assertEquals;

/**
 * Test class for {@link DayOfMonth}.
 */
public class DayOfMonthTest {

    @Test
    public void now_withFixedClock_createsCorrectDayAndProvidesIsoChronology() {
        // Arrange: Set up a fixed clock for a predictable "now".
        // The date is February 14th, 2023.
        Instant fixedInstant = Instant.parse("2023-02-14T10:15:30.00Z");
        Clock fixedClock = Clock.fixed(fixedInstant, ZoneOffset.UTC);

        // Act: Create a DayOfMonth using the fixed clock and query its chronology.
        DayOfMonth dayOfMonth = DayOfMonth.now(fixedClock);
        Chronology chronology = Chronology.from(dayOfMonth);

        // Assert: Verify the day value and that it correctly reports the ISO chronology.
        assertEquals("The day of month should be 14", 14, dayOfMonth.getValue());
        assertEquals("DayOfMonth should be based on the ISO chronology", IsoChronology.INSTANCE, chronology);
    }
}