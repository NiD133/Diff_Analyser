package org.joda.time.chrono;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for the IslamicChronology class, focusing on year-based calculations.
 */
public class IslamicChronologyTest {

    /**
     * Tests that calculateFirstDayOfYearMillis() returns the correct timestamp
     * for a given Islamic year.
     */
    @Test
    public void calculateFirstDayOfYearMillis_shouldReturnCorrectTimestampForIslamicYear() {
        // Arrange: Get an instance of the IslamicChronology in UTC.
        IslamicChronology islamicChronology = IslamicChronology.getInstanceUTC();
        int islamicYear = 1576;

        // The first day of the Islamic year 1576 corresponds to
        // November 20, 2149, in the Gregorian calendar.
        // We create a Gregorian DateTime to establish the expected millisecond value
        // in a human-readable way, avoiding magic numbers.
        DateTime expectedDateInGregorian = new DateTime(2149, 11, 20, 0, 0, 0, DateTimeZone.UTC);
        long expectedMillis = expectedDateInGregorian.getMillis();

        // Act: Calculate the first day of the given Islamic year in milliseconds.
        long actualMillis = islamicChronology.calculateFirstDayOfYearMillis(islamicYear);

        // Assert: The calculated milliseconds should match the expected value.
        assertEquals(expectedMillis, actualMillis);
    }
}