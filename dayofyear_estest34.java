package org.threeten.extra;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link DayOfYear#isValidYear(int)} method.
 */
public class DayOfYear_ESTestTest34 extends DayOfYear_ESTest_scaffolding {

    /**
     * Tests that isValidYear() returns true for a day-of-year that exists in every year (i.e., not day 366).
     * Any day from 1 to 365 should be considered valid for any given year, leap or not.
     */
    @Test
    public void isValidYear_forDayInAnyYear_shouldReturnTrue() {
        // Arrange: Create a DayOfYear instance for a day that is not the 366th day.
        // Day 46 is used to maintain consistency with the original test's value,
        // but any day from 1 to 365 would demonstrate the same behavior.
        DayOfYear dayOfYear = DayOfYear.of(46);
        int anyYear = 8; // The year 8 is a leap year, but this is irrelevant for day 46.

        // Act: Check if this day is valid for the given year.
        boolean isValid = dayOfYear.isValidYear(anyYear);

        // Assert: The result must be true because day 46 exists in every year.
        assertTrue("Day 46 should be valid for any year", isValid);
    }
}