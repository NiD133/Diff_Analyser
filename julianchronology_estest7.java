package org.threeten.extra.chrono;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Tests for the {@link JulianChronology}.
 */
public class JulianChronologyTest {

    /**
     * Tests that {@link JulianChronology#isLeapYear(long)} correctly identifies a non-leap year.
     */
    @Test
    public void isLeapYear_shouldReturnFalse_whenYearIsNotDivisibleBy4() {
        // Arrange: In the Julian calendar, a year is a leap year if it is divisible by 4.
        // The year 2110 is not divisible by 4 (2110 % 4 = 2), so it is not a leap year.
        long nonLeapYear = 2110L;

        // Act & Assert: Verify that the chronology identifies the year as a non-leap year.
        assertFalse(
            "Year " + nonLeapYear + " should not be a leap year in the Julian calendar.",
            JulianChronology.INSTANCE.isLeapYear(nonLeapYear)
        );
    }
}