package org.joda.time.chrono;

import static org.junit.Assert.assertFalse;
import org.junit.Test;

/**
 * Unit tests for {@link GregorianChronology}.
 */
public class GregorianChronologyTest {

    /**
     * Tests that a year divisible by 100 but not by 400 is correctly identified
     * as a common (non-leap) year.
     */
    @Test
    public void isLeapYear_shouldReturnFalse_forYearDivisibleBy100ButNot400() {
        // The Gregorian calendar rule states a year is a leap year if it is
        // divisible by 4, except for end-of-century years, which must be
        // divisible by 400 to be a leap year.
        // The year -900 is divisible by 100 but not by 400, so it is not a leap year.

        // Arrange
        GregorianChronology chronology = GregorianChronology.getInstance();
        int year = -900;

        // Act
        boolean isLeap = chronology.isLeapYear(year);

        // Assert
        assertFalse("A year divisible by 100 but not 400 should not be a leap year.", isLeap);
    }
}