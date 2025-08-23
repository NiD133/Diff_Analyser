package org.threeten.extra;

import org.junit.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Tests for {@link DayOfYear}.
 * This improved test focuses on clarity, determinism, and maintainability.
 */
public class DayOfYear_ESTestTest1 {

    /**
     * Tests that DayOfYear.now(clock) correctly reflects the date in different time zones.
     * When the same instant in time is viewed from two different time zones that are on
     * opposite sides of a date line, the resulting DayOfYear should be different.
     */
    @Test
    public void now_withDifferentTimeZones_canReturnDifferentDays() {
        // Arrange: Set up a fixed point in time and two time zones with a large gap.
        // This instant is chosen so that UTC+18 and UTC-18 are on different calendar days.
        Instant fixedInstant = Instant.parse("2023-05-20T12:00:00Z");
        
        // At this instant:
        // - In UTC+18:00, the local time is 2023-05-21 06:00:00.
        // - In UTC-18:00, the local time is 2023-05-19 18:00:00.
        Clock clockInFutureZone = Clock.fixed(fixedInstant, ZoneOffset.MAX); // UTC+18
        Clock clockInPastZone = Clock.fixed(fixedInstant, ZoneOffset.MIN);   // UTC-18

        // Act: Get the DayOfYear for the same instant in each time zone.
        DayOfYear dayInFuture = DayOfYear.now(clockInFutureZone);
        DayOfYear dayInPast = DayOfYear.now(clockInPastZone);

        // Assert: The two DayOfYear instances should represent different days and thus not be equal.
        // Day for May 21st = 31(Jan)+28(Feb)+31(Mar)+30(Apr)+21(May) = 141
        // Day for May 19th = 31(Jan)+28(Feb)+31(Mar)+30(Apr)+19(May) = 139
        assertEquals(141, dayInFuture.getValue());
        assertEquals(139, dayInPast.getValue());
        assertNotEquals(dayInFuture, dayInPast);
    }
}