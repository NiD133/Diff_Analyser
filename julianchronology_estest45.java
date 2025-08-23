package org.threeten.extra.chrono;

import org.junit.Test;

import java.time.DateTimeException;
import java.time.chrono.Era;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link JulianChronology}.
 */
public class JulianChronologyTest {

    @Test
    public void date_throwsException_forInvalidMonthValue() {
        // Arrange: Set up the chronology and invalid date components.
        // The month value (983) is intentionally out of the valid range (1-12).
        JulianChronology chronology = JulianChronology.INSTANCE;
        Era era = JulianEra.BC;
        int year = 983;
        int invalidMonth = 983;
        int day = 983; // The day is also invalid, but the month is validated first.

        // Act & Assert: Attempt to create a date and verify that the correct exception is thrown.
        try {
            chronology.date(era, year, invalidMonth, day);
            fail("Expected a DateTimeException to be thrown due to an invalid month.");
        } catch (DateTimeException e) {
            // Verify the exception message is clear and correct.
            assertEquals(
                "Invalid value for MonthOfYear (valid values 1 - 12): 983",
                e.getMessage()
            );
        }
    }
}