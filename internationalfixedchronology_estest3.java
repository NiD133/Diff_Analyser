package org.threeten.extra.chrono;

import org.junit.Test;

import java.time.DateTimeException;
import java.time.chrono.Era;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link InternationalFixedChronology}.
 * This class focuses on date creation with invalid parameters.
 */
// Note: The original class name and scaffolding are kept to match the context,
// but the test method inside has been rewritten for clarity.
public class InternationalFixedChronology_ESTestTest3 extends InternationalFixedChronology_ESTest_scaffolding {

    /**
     * Tests that creating a date with a day-of-month value outside the valid range
     * throws a DateTimeException.
     */
    @Test
    public void date_whenDayOfMonthIsInvalid_throwsDateTimeException() {
        // Arrange: Set up the chronology and parameters for the date creation.
        // The day of the month is intentionally set to an invalid value.
        InternationalFixedChronology chronology = InternationalFixedChronology.INSTANCE;
        Era era = InternationalFixedEra.of(1); // The 'Common Era' for this chronology
        int year = 6;
        int month = 1;
        int invalidDayOfMonth = -347;

        // Act & Assert: Attempt to create the date and verify that the correct exception is thrown.
        try {
            chronology.date(era, year, month, invalidDayOfMonth);
            fail("Expected a DateTimeException to be thrown for an invalid day of month.");
        } catch (DateTimeException e) {
            // Verify that the exception message is informative and correct.
            String expectedMessage = "Invalid value for DayOfMonth (valid values 1 - 29): " + invalidDayOfMonth;
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}