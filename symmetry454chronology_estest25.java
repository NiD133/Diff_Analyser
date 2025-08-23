package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.DateTimeException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link Symmetry454Chronology}.
 */
public class Symmetry454ChronologyTest {

    /**
     * Tests that creating a date with a day-of-year that is only valid in a leap year
     * throws a DateTimeException when the year is not a leap year.
     */
    @Test
    public void dateYearDay_throwsExceptionForInvalidDayInNonLeapYear() {
        // Arrange: Get the chronology and define test data.
        // The Symmetry454 calendar has 364 days in a normal year and 371 in a leap year.
        // The year 371 is not a leap year in this chronology.
        Symmetry454Chronology chronology = Symmetry454Chronology.INSTANCE;
        int nonLeapYear = 371;
        int invalidDayOfYearForNonLeapYear = 371;

        // Act & Assert
        try {
            chronology.dateYearDay(nonLeapYear, invalidDayOfYearForNonLeapYear);
            fail("Expected DateTimeException was not thrown for an invalid day of year.");
        } catch (DateTimeException e) {
            // Verify that the exception has a clear, informative message.
            String expectedMessage = "Invalid date 'DayOfYear 371' as '371' is not a leap year";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}