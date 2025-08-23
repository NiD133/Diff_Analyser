package org.threeten.extra;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link DayOfYear}.
 * The original test class name "DayOfYear_ESTestTest33" suggests it was
 * auto-generated. This version provides a more focused and readable test case.
 */
public class DayOfYear_ESTestTest33 extends DayOfYear_ESTest_scaffolding {

    /**
     * Tests that isValidYear() correctly identifies a leap year as valid for day 366.
     */
    @Test
    public void isValidYear_forDay366_shouldReturnTrue_whenYearIsLeap() {
        // Arrange: Create a DayOfYear for the 366th day, which only occurs in leap years.
        // The year 364 is a leap year (divisible by 4).
        DayOfYear leapDay = DayOfYear.of(366);
        int aLeapYear = 364;

        // Act: Check if this day is valid for the given leap year.
        boolean isValid = leapDay.isValidYear(aLeapYear);

        // Assert: The result should be true, as day 366 is valid in a leap year.
        assertTrue("Day 366 should be considered valid for a leap year.", isValid);
    }
}