package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.DateTimeException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the {@link Symmetry454Chronology} class.
 */
public class Symmetry454ChronologyTest {

    /**
     * Tests that creating a date with a month value outside the valid range (1-12)
     * throws a DateTimeException.
     */
    @Test
    public void date_whenMonthIsInvalid_throwsDateTimeException() {
        // Arrange: Use the singleton instance of the chronology and define an invalid month.
        // Other date components are kept valid to isolate the failure condition.
        Symmetry454Chronology chronology = Symmetry454Chronology.INSTANCE;
        int year = 2023;
        int invalidMonth = 1363; // Valid months are 1-12
        int day = 1;

        // Act & Assert
        try {
            chronology.date(year, invalidMonth, day);
            fail("Expected a DateTimeException to be thrown for a month value outside the valid range.");
        } catch (DateTimeException e) {
            // Verify that the exception message is clear and correct.
            String expectedMessage = "Invalid value for MonthOfYear (valid values 1 - 12): " + invalidMonth;
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}