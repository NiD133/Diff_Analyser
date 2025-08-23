package org.threeten.extra.chrono;

import org.junit.jupiter.api.Test;
import java.time.DateTimeException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * A test suite for the {@link BritishCutoverChronology} class.
 */
class BritishCutoverChronologyTest {

    /**
     * Tests that calling dateYearDay() with a day-of-year value that is clearly
     * out of the valid range (1-366) throws a DateTimeException.
     */
    @Test
    void dateYearDayWithInvalidDayOfYearThrowsException() {
        // Arrange: Set up the test data.
        // A day-of-year of 874 is invalid for any year.
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;
        int year = 874;
        int invalidDayOfYear = 874;

        // Act & Assert: Execute the method and verify that it throws the expected exception.
        DateTimeException thrown = assertThrows(DateTimeException.class, () -> {
            chronology.dateYearDay(year, invalidDayOfYear);
        });

        // Assert on the exception message for more precise validation.
        // This confirms the exception was thrown for the expected reason.
        assertTrue(
            thrown.getMessage().contains("Invalid value for DayOfYear"),
            "Exception message should indicate that the DayOfYear is invalid."
        );
    }
}