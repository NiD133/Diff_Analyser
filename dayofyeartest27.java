package org.threeten.extra;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DayOfYear#isValidYear(int)}.
 */
class DayOfYearIsValidYearTest {

    @Test
    void isValidYear_forDay366_isTrueOnlyForLeapYears() {
        // Arrange: Day 366 only exists in a leap year.
        DayOfYear day366 = DayOfYear.of(366);
        int leapYear = 2012;
        int nonLeapYearBefore = 2011;
        int nonLeapYearAfter = 2013;

        // Act & Assert
        assertTrue(day366.isValidYear(leapYear), "Day 366 should be valid in a leap year (2012)");
        assertFalse(day366.isValidYear(nonLeapYearBefore), "Day 366 should not be valid in a non-leap year (2011)");
        assertFalse(day366.isValidYear(nonLeapYearAfter), "Day 366 should not be valid in a non-leap year (2013)");
    }

    @Test
    void isValidYear_forAnyDayBefore366_isAlwaysTrue() {
        // Arrange: Days 1-365 exist in all years.
        DayOfYear commonDay = DayOfYear.of(1);       // First day
        DayOfYear anotherCommonDay = DayOfYear.of(365); // Last day of a common year

        int leapYear = 2012;
        int nonLeapYear = 2011;

        // Act & Assert
        assertTrue(commonDay.isValidYear(leapYear));
        assertTrue(commonDay.isValidYear(nonLeapYear));
        assertTrue(anotherCommonDay.isValidYear(leapYear));
        assertTrue(anotherCommonDay.isValidYear(nonLeapYear));
    }
}