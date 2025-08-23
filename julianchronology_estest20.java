package org.threeten.extra.chrono;

import org.junit.Test;

import java.time.DateTimeException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link JulianChronology}.
 * This class focuses on the date creation methods.
 */
public class JulianChronologyTest {

    /**
     * Tests that creating a date with day-of-year 366 in a non-leap year throws an exception.
     * In the Julian calendar, a year is a leap year if it is divisible by 4.
     * The year 366 is not divisible by 4, so it has only 365 days.
     */
    @Test
    public void dateYearDay_throwsExceptionForDay366InNonLeapYear() {
        // Arrange
        JulianChronology julianChronology = JulianChronology.INSTANCE;
        int nonLeapYear = 366; // 366 % 4 != 0, so it's not a Julian leap year.
        int invalidDayOfYear = 366;
        String expectedMessage = "Invalid date 'DayOfYear 366' as '366' is not a leap year";

        // Act & Assert
        try {
            julianChronology.dateYearDay(nonLeapYear, invalidDayOfYear);
            fail("Expected a DateTimeException to be thrown for day 366 in a non-leap year.");
        } catch (DateTimeException e) {
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}