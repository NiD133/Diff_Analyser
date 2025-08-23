package org.threeten.extra;

import org.junit.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DayOfYear_ESTestTest4 extends DayOfYear_ESTest_scaffolding {

    /**
     * Tests that compareTo correctly handles DayOfYear instances created from the same
     * instant but in different time zones that fall on different calendar days.
     */
    @Test
    public void compareTo_returnsPositive_whenDayFromLaterTimeZoneIsComparedToEarlierOne() {
        // Arrange: Set up a time where different time zones are on different days.
        // Use a fixed instant that is late in the day in UTC (10 PM on March 15th, 2023).
        Instant fixedInstant = Instant.parse("2023-03-15T22:00:00Z");

        // In a time zone far ahead of UTC (Tokyo, UTC+9), it's already the next day (March 16th).
        ZoneId tokyoZone = ZoneId.of("Asia/Tokyo");
        Clock tokyoClock = Clock.fixed(fixedInstant, tokyoZone);
        DayOfYear dayInTokyo = DayOfYear.now(tokyoClock); // Corresponds to day 75 of 2023

        // In a time zone far behind UTC (New York, UTC-4), it's still the same day (March 15th).
        ZoneId newYorkZone = ZoneId.of("America/New_York");
        Clock newYorkClock = Clock.fixed(fixedInstant, newYorkZone);
        DayOfYear dayInNewYork = DayOfYear.now(newYorkClock); // Corresponds to day 74 of 2023

        // Act: Compare the day from the later time zone to the earlier one.
        int comparisonResult = dayInTokyo.compareTo(dayInNewYork);

        // Assert: The day in Tokyo (75) should be greater than the day in New York (74).
        // The compareTo method should return a positive integer.
        assertTrue("Expected dayInTokyo to be after dayInNewYork", comparisonResult > 0);
        
        // For completeness, we can also check the exact value.
        assertEquals(1, comparisonResult);
    }
}