package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.DateTimeException;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test for the dateYearDay() method in JulianChronology.
 */
public class JulianChronologyTest {

    /**
     * Tests that dateYearDay() throws a DateTimeException when the day-of-year
     * value is outside the valid range.
     */
    @Test
    public void dateYearDay_whenDayOfYearIsTooLarge_throwsDateTimeException() {
        // Arrange: Use the recommended singleton instance and define an invalid day-of-year.
        JulianChronology julianChronology = JulianChronology.INSTANCE;
        int year = 2024; // An arbitrary leap year.
        int invalidDayOfYear = 367; // A value just beyond the maximum possible (366 for a leap year).

        // Act & Assert: Expect a DateTimeException for the out-of-range day.
        try {
            julianChronology.dateYearDay(year, invalidDayOfYear);
            fail("Expected DateTimeException was not thrown for an invalid day of year.");
        } catch (DateTimeException e) {
            // Verify that the exception message clearly indicates the problem.
            String expectedMessageContent = "Invalid value for DayOfYear";
            assertTrue(
                "Exception message should explain the error. Actual: " + e.getMessage(),
                e.getMessage().contains(expectedMessageContent)
            );
        }
    }
}