package org.threeten.extra;

import org.junit.Test;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import static org.junit.Assert.assertEquals;

/**
 * A test suite for the DayOfYear class, focusing on comparison logic.
 */
public class DayOfYearTest {

    /**
     * Tests that the compareTo method correctly compares two DayOfYear instances
     * that fall on different days due to their time zones.
     */
    @Test
    public void compareTo_returnsPositive_whenFirstDayIsLaterThanSecondDueToZoneDifference() {
        // Arrange: Set up a fixed point in time where different time zones will result in different days.
        // We choose a time late in the day in UTC (23:00 on May 15th).
        Instant fixedInstant = Instant.parse("2024-05-15T23:00:00Z");

        // In a time zone 2 hours ahead of UTC, the date is already the next day (May 16th).
        Clock clockForLaterDay = Clock.fixed(fixedInstant, ZoneOffset.ofHours(2));
        DayOfYear dayInLaterZone = DayOfYear.now(clockForLaterDay);

        // In a time zone 2 hours behind UTC, the date is still the current day (May 15th).
        Clock clockForEarlierDay = Clock.fixed(fixedInstant, ZoneOffset.ofHours(-2));
        DayOfYear dayInEarlierZone = DayOfYear.now(clockForEarlierDay);

        // Act: Compare the DayOfYear from the later time zone to the one from the earlier time zone.
        int comparisonResult = dayInLaterZone.compareTo(dayInEarlierZone);

        // Assert: The DayOfYear for May 16th is greater than the one for May 15th,
        // so the result of compareTo should be 1.
        assertEquals(1, comparisonResult);
    }
}