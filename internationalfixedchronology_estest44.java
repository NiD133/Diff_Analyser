package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.DateTimeException;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests for {@link InternationalFixedChronology}.
 * This focuses on validating the creation of dates with invalid parameters.
 */
public class InternationalFixedChronologyTest {

    /**
     * Tests that creating a date with a month value outside the valid range (1-13)
     * throws a DateTimeException.
     */
    @Test
    public void date_shouldThrowException_whenMonthIsOutOfRange() {
        // Arrange: Get an instance of the chronology and define invalid input.
        // The International Fixed calendar has 13 months, so 14 is an invalid value.
        InternationalFixedChronology chronology = InternationalFixedChronology.INSTANCE;
        int year = 2023;
        int invalidMonth = 14;
        int dayOfMonth = 1;

        // Act & Assert: Attempt to create the date and verify the correct exception is thrown.
        try {
            chronology.date(year, invalidMonth, dayOfMonth);
            fail("Expected DateTimeException was not thrown for month value: " + invalidMonth);
        } catch (DateTimeException e) {
            // The exception is expected. For a more robust test, we verify its message.
            String expectedMessageContent = "Invalid value for MonthOfYear";
            assertTrue(
                "The exception message should indicate an invalid month. Actual: " + e.getMessage(),
                e.getMessage().contains(expectedMessageContent)
            );
        }
    }
}