package org.threeten.extra.chrono;

import org.junit.Test;

import java.time.DateTimeException;

import static org.junit.Assert.fail;

/**
 * Unit tests for the {@link Symmetry010Chronology} class.
 */
public class Symmetry010ChronologyTest {

    @Test
    public void date_shouldThrowException_whenDayOfMonthIsInvalid() {
        // Arrange: The Symmetry010 calendar has 31 days in May.
        // We will attempt to create a date with an invalid day, 35.
        Symmetry010Chronology chronology = Symmetry010Chronology.INSTANCE;
        int year = 5;
        int month = 5; // May
        int invalidDayOfMonth = 35;

        // Act & Assert
        try {
            chronology.date(year, month, invalidDayOfMonth);
            fail("Expected a DateTimeException to be thrown for an invalid day of the month, but it was not.");
        } catch (DateTimeException expectedException) {
            // Success: The expected exception was correctly thrown.
            // For a more robust test, we could also assert the exception message, e.g.:
            // assertTrue(expectedException.getMessage().contains("Invalid value for DayOfMonth"));
        }
    }
}