package org.threeten.extra.chrono;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.time.DateTimeException;
import java.time.chrono.Era;
import org.junit.Test;

/**
 * Tests for the {@link InternationalFixedChronology} class, focusing on invalid date creation.
 */
public class InternationalFixedChronologyTest {

    /**
     * Tests that dateYearDay() throws a DateTimeException when the year-of-era
     * is outside the valid range. The valid range for year-of-era is 1 to 1,000,000.
     */
    @Test
    public void dateYearDay_whenYearOfEraIsTooLarge_throwsDateTimeException() {
        // Arrange
        InternationalFixedChronology chronology = InternationalFixedChronology.INSTANCE;
        Era era = InternationalFixedEra.CE;
        int invalidYear = 1_000_001; // Exceeds the maximum valid year of 1,000,000
        int validDayOfYear = 150;

        // Act & Assert
        try {
            chronology.dateYearDay(era, invalidYear, validDayOfYear);
            fail("Expected DateTimeException was not thrown for an out-of-range year.");
        } catch (DateTimeException e) {
            // Verify that the exception message is informative and correct
            String expectedMessage = "Invalid value for YearOfEra (valid values 1 - 1000000): " + invalidYear;
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}