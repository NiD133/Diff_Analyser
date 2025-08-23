package org.threeten.extra.chrono;

import org.junit.Test;

import java.time.DateTimeException;
import java.time.chrono.IsoEra;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests for {@link Symmetry010Chronology}.
 * This test focuses on validating date creation logic.
 */
public class Symmetry010ChronologyTest {

    /**
     * Tests that creating a date with a day-of-year greater than 364
     * for a non-leap year throws a DateTimeException.
     */
    @Test
    public void dateYearDay_throwsExceptionForInvalidDayInNonLeapYear() {
        // Arrange
        Symmetry010Chronology chronology = Symmetry010Chronology.INSTANCE;
        // According to the Symmetry010 calendar, year 1 is not a leap year and has 364 days.
        int nonLeapYear = 1;
        int invalidDayOfYear = 365;

        // Act & Assert
        try {
            chronology.dateYearDay(IsoEra.CE, nonLeapYear, invalidDayOfYear);
            fail("Expected DateTimeException was not thrown for day 365 in a non-leap year.");
        } catch (DateTimeException e) {
            // Verify that the exception message clearly explains the failure.
            String message = e.getMessage();
            assertTrue("Exception message should indicate the year is not a leap year.",
                    message.contains("not a leap year"));
            assertTrue("Exception message should reference the invalid day-of-year.",
                    message.contains("DayOfYear 365"));
        }
    }
}