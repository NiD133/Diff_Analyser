package org.threeten.extra;

import org.junit.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link DayOfYear} class, focusing on comparison logic.
 */
public class DayOfYear_ESTestTest5 {

    /**
     * Tests that compareTo() returns a negative value when comparing an earlier day
     * to a later day.
     * <p>
     * This test clarifies an auto-generated test case. The original used a mock clock
     * with an implicit default date, leading to a "magic number" in the assertion.
     * This version makes the date explicit to clearly explain the expected result.
     */
    @Test
    public void compareTo_returnsNegative_whenComparingToLaterDay() {
        // Arrange
        // The original test's mock clock defaulted to an instant representing 2014-02-14.
        // February 14th is the 45th day of a non-leap year (31 days in Jan + 14).
        // We create a fixed clock for that specific date to make the test deterministic and clear.
        Instant instanceForDay45 = Instant.parse("2014-02-14T10:15:30.00Z");
        Clock fixedClock = Clock.fixed(instanceForDay45, ZoneOffset.UTC);

        DayOfYear firstDay = DayOfYear.of(1);
        DayOfYear laterDay = DayOfYear.now(fixedClock); // This will resolve to DayOfYear(45)

        // Act
        int comparisonResult = firstDay.compareTo(laterDay);

        // Assert
        // The comparison is based on the underlying day-of-year value.
        // The expected result is the difference between the two values: 1 - 45 = -44.
        assertEquals(-44, comparisonResult);
    }
}