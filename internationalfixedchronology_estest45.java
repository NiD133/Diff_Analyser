package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.DateTimeException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link InternationalFixedChronology}.
 */
public class InternationalFixedChronologyTest {

    @Test
    public void dateYearDay_whenDayOfYearIsOutOfBounds_throwsException() {
        // Arrange
        InternationalFixedChronology chronology = InternationalFixedChronology.INSTANCE;
        int year = 2191; // A valid, non-leap year
        int invalidDayOfYear = 2191; // An invalid day, well outside the valid range of 1-365

        // Act & Assert
        try {
            chronology.dateYearDay(year, invalidDayOfYear);
            fail("Expected a DateTimeException to be thrown for an invalid day-of-year.");
        } catch (DateTimeException e) {
            // Verify that the exception message is informative and correct
            String expectedMessage = "Invalid value for DayOfYear (valid values 1 - 365/366): " + invalidDayOfYear;
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}