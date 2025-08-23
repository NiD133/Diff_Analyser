package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.DateTimeException;
import java.time.chrono.Era;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link BritishCutoverChronology}.
 */
public class BritishCutoverChronologyTest {

    /**
     * Tests that creating a date with a month value outside the valid range (1-12)
     * throws a DateTimeException.
     */
    @Test
    public void date_withInvalidMonth_throwsDateTimeException() {
        // Arrange
        // The SUT's documentation recommends using the singleton INSTANCE.
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;
        Era adEra = JulianEra.AD;
        int yearOfEra = 6;
        int invalidMonth = 1000; // Valid months are 1-12.
        int dayOfMonth = 1;
        
        String expectedErrorMessage = "Invalid value for MonthOfYear (valid values 1 - 12): 1000";

        // Act & Assert
        try {
            chronology.date(adEra, yearOfEra, invalidMonth, dayOfMonth);
            fail("Expected a DateTimeException to be thrown for an invalid month, but it was not.");
        } catch (DateTimeException e) {
            assertEquals("The exception message did not match the expected value.", expectedErrorMessage, e.getMessage());
        }
    }
}