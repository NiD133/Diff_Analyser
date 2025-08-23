package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.DateTimeException;
import java.time.chrono.IsoEra;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the {@link Symmetry010Chronology} class.
 */
public class Symmetry010Chronology_ESTestTest1 {

    /**
     * Tests that creating a date with an invalid month value throws a DateTimeException.
     * The valid range for a month is 1 to 12.
     */
    @Test
    public void date_whenMonthIsInvalid_throwsDateTimeException() {
        // Arrange
        Symmetry010Chronology chronology = Symmetry010Chronology.INSTANCE;
        int year = 2023;
        int invalidMonth = -2552; // An obviously invalid month value
        int day = 15;

        // Act & Assert
        try {
            chronology.date(IsoEra.CE, year, invalidMonth, day);
            fail("Expected a DateTimeException to be thrown due to an invalid month.");
        } catch (DateTimeException e) {
            String expectedMessage = "Invalid value for MonthOfYear (valid values 1 - 12): " + invalidMonth;
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}