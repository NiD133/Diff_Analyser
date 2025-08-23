package org.threeten.extra;

import org.junit.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link DayOfYear} class, focusing on its factory methods.
 */
public class DayOfYearTest {

    /**
     * Tests that DayOfYear.now(clock) correctly determines the day of the year
     * from a fixed clock.
     */
    @Test
    public void now_withFixedClock_returnsCorrectDayOfYear() {
        // Arrange: Create a clock fixed to a specific date.
        // February 14th is the 45th day of a non-leap year (31 days in Jan + 14).
        Instant instant = Instant.parse("2021-02-14T10:00:00Z");
        Clock fixedClock = Clock.fixed(instant, ZoneOffset.UTC);

        // Act: Get the DayOfYear using the fixed clock.
        DayOfYear dayOfYear = DayOfYear.now(fixedClock);

        // Assert: The value should match the day of the year for the fixed date.
        assertEquals(45, dayOfYear.getValue());
    }
}