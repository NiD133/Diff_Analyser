package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.DateTimeException;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link JulianChronology} class, focusing on invalid date creation.
 */
public class JulianChronologyTest {

    /**
     * Tests that creating a date with an invalid month value throws a DateTimeException.
     * The proleptic year and day-of-month are also invalid, but the month
     * is expected to be validated first.
     */
    @Test
    public void date_whenMonthIsInvalid_throwsException() {
        // Arrange
        JulianChronology julianChronology = JulianChronology.INSTANCE;
        int invalidMonth = -3115;
        int year = -3115;
        int dayOfMonth = -3115;

        // Act & Assert
        DateTimeException thrown = assertThrows(
            DateTimeException.class,
            () -> julianChronology.date(year, invalidMonth, dayOfMonth)
        );

        // Verify that the exception message clearly indicates the error
        assertTrue(
            "Exception message should indicate an invalid month.",
            thrown.getMessage().contains("Invalid value for MonthOfYear")
        );
    }
}