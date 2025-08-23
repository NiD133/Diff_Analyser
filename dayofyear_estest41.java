package org.threeten.extra;

import org.junit.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;

import static org.junit.Assert.assertEquals;

// The original test class name and inheritance are kept to match the provided context.
public class DayOfYear_ESTestTest41 extends DayOfYear_ESTest_scaffolding {

    /**
     * Tests that the current DayOfYear is correctly determined using a fixed clock and a specific time zone.
     */
    @Test
    public void now_withFixedClockAndMaxOffsetZone_returnsCorrectDayOfYear() {
        // Arrange: Create a clock fixed to a specific instant in time.
        // Using Clock.fixed() makes the test deterministic and self-contained,
        // which is a significant improvement over relying on an implicit mock clock.
        Instant fixedInstant = Instant.parse("2014-02-14T20:21:21.320Z");
        ZoneId zoneWithMaxOffset = ZoneOffset.MAX; // +18:00
        Clock clock = Clock.fixed(fixedInstant, zoneWithMaxOffset);

        // Act: Get the "current" DayOfYear using the fixed clock.
        // The now(Clock) method is the preferred, testable way to handle time-dependent logic.
        DayOfYear dayOfYear = DayOfYear.now(clock);

        // Assert: Verify the day of the year is correct.
        // The instant 20:21Z on Feb 14, when viewed in a +18:00 timezone,
        // becomes 14:21 on Feb 15 of the next day.
        // The day of the year for Feb 15 is 31 (Jan) + 15 = 46.
        int expectedDayOfYear = 46;
        assertEquals(expectedDayOfYear, dayOfYear.getValue());
    }
}