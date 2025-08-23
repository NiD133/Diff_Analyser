package org.threeten.extra;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for the {@link DayOfYear} class.
 */
public class DayOfYearTest {

    /**
     * Tests that isValidYear returns true for day 366 when the provided year is a leap year.
     * Day 366 is only valid in leap years.
     */
    @Test
    public void isValidYear_forDay366_returnsTrueForLeapYear() {
        // Arrange: Define the 366th day of the year and a known leap year.
        // The year 364 is a leap year, making day 366 valid.
        DayOfYear lastDayOfLeapYear = DayOfYear.of(366);
        int leapYear = 364;

        // Act: Check if the day is valid for the given leap year.
        boolean isValid = lastDayOfLeapYear.isValidYear(leapYear);

        // Assert: Verify that the year is considered valid and the DayOfYear object is correct.
        assertTrue("Day 366 should be valid for a leap year like " + leapYear, isValid);
        assertEquals("The value of the DayOfYear object should be 366", 366, lastDayOfLeapYear.getValue());
    }
}