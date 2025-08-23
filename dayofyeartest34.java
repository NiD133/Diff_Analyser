package org.threeten.extra;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.Year;
import java.time.temporal.Temporal;
import org.junit.jupiter.api.Test;

/**
 * Test class for {@link DayOfYear}.
 * This snippet focuses on the adjustInto method.
 */
public class DayOfYearTest {

    private static final Year YEAR_LEAP = Year.of(2008);
    private static final int LEAP_YEAR_LENGTH = 366;

    @Test
    public void adjustInto_forEveryDayOfLeapYear_adjustsDateCorrectly() {
        // The date to be adjusted. The specific date doesn't matter, only its year (2008 is a leap year).
        LocalDate dateInLeapYear = LocalDate.of(YEAR_LEAP.getValue(), 1, 1);

        // Iterate through all 366 days of a leap year.
        for (int day = 1; day <= LEAP_YEAR_LENGTH; day++) {
            // Arrange: Create the DayOfYear to test and determine the expected result.
            DayOfYear dayOfYear = DayOfYear.of(day);
            LocalDate expectedDate = LocalDate.ofYearDay(YEAR_LEAP.getValue(), day);
            String failureMessage = "Failed for day-of-year: " + day;

            // Act: Adjust the date.
            Temporal adjustedDate = dayOfYear.adjustInto(dateInLeapYear);

            // Assert: The adjusted date should match the expected date.
            assertEquals(expectedDate, adjustedDate, failureMessage);
        }
    }
}