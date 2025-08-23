package org.threeten.extra.chrono;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.time.DateTimeException;
import org.junit.Test;

/**
 * Unit tests for {@link BritishCutoverChronology}.
 */
public class BritishCutoverChronologyTest {

    /**
     * Tests that creating a date with day-of-year 366 in a non-leap year throws an exception.
     * The year 366 is before the 1752 cutover, so it follows Julian calendar rules.
     * In the Julian calendar, a year is a leap year if it is divisible by 4.
     * Since 366 is not divisible by 4, it is a common year with 365 days.
     */
    @Test
    public void dateYearDay_forDay366InNonLeapYear_throwsException() {
        // Arrange
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;
        int nonLeapJulianYear = 366;
        int invalidDayOfYear = 366;

        // Act & Assert
        DateTimeException exception = assertThrows(
                "Creating a date for day 366 in a 365-day year should fail.",
                DateTimeException.class,
                () -> chronology.dateYearDay(nonLeapJulianYear, invalidDayOfYear));

        // Verify the exception message provides a clear reason for the failure.
        assertTrue(
                "Exception message should explain that the year is not a leap year.",
                exception.getMessage().contains("not a leap year"));
    }
}