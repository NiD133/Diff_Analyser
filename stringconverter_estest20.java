package org.joda.time.convert;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.MutableInterval;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests the functionality of {@link StringConverter} for parsing ISO 8601 interval strings.
 * Note: These tests call {@link MutableInterval#parse(String)}, which internally uses
 * a {@link StringConverter} to perform the conversion.
 */
public class StringConverterTest {

    /**
     * Tests that an interval string with a single-digit year and a period is parsed correctly.
     * The Joda-Time parser has a specific behavior where a single integer in a date-time
     * string, like "6", is interpreted as the year.
     */
    @Test
    public void parseIntervalFromYearAndPeriodStringShouldCalculateCorrectEnd() {
        // Arrange
        // The interval string "6/P7m" represents a start time followed by a period.
        // The parser interprets "6" as the start of the year 6 in the UTC time zone.
        // "P7m" represents a period of 7 months.
        String intervalString = "6/P7m";

        DateTime expectedStart = new DateTime(6, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC);
        DateTime expectedEnd = expectedStart.plusMonths(7);

        // Act
        MutableInterval parsedInterval = MutableInterval.parse(intervalString);

        // Assert
        assertEquals("The start of the interval should be the beginning of year 6.",
                expectedStart.getMillis(), parsedInterval.getStartMillis());
        assertEquals("The end of the interval should be 7 months after the start.",
                expectedEnd.getMillis(), parsedInterval.getEndMillis());
    }
}