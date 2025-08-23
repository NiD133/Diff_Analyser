package org.threeten.extra.chrono;

import org.junit.jupiter.api.Test;
import java.time.DateTimeException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for the {@link Symmetry010Chronology} class.
 */
public class Symmetry010ChronologyTest {

    /**
     * Tests that dateYearDay() throws a DateTimeException when the day-of-year
     * value is outside the valid range.
     *
     * According to the Symmetry010 calendar system, a standard year has 364 days
     * and a leap year has 371. The value 999 is used here as it is clearly
     * outside this valid range.
     */
    @Test
    void dateYearDay_whenDayOfYearIsOutOfRange_throwsDateTimeException() {
        // Arrange
        Symmetry010Chronology chronology = Symmetry010Chronology.INSTANCE;
        int year = 999; // A valid year for the test
        int invalidDayOfYear = 999; // An out-of-range day, as the max is 371
        String expectedErrorMessage = "Invalid value for DayOfYear (valid values 1 - 364/371): " + invalidDayOfYear;

        // Act & Assert
        // Verify that calling the method with an invalid dayOfYear throws the expected exception.
        DateTimeException thrown = assertThrows(
            DateTimeException.class,
            () -> chronology.dateYearDay(year, invalidDayOfYear)
        );

        // Also, verify that the exception message is correct to ensure the right validation failed.
        assertEquals(expectedErrorMessage, thrown.getMessage());
    }
}