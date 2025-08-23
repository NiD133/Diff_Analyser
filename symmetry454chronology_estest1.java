package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.DateTimeException;
import java.time.chrono.Era;
import java.time.chrono.IsoEra;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the date creation logic in {@link Symmetry454Chronology}.
 */
public class Symmetry454ChronologyTest {

    /**
     * Verifies that calling the date() factory method with a month value
     * outside the valid range of 1-12 throws a DateTimeException.
     */
    @Test
    public void date_whenMonthIsInvalid_throwsDateTimeException() {
        // Arrange: Set up the chronology and invalid date components.
        // The original test used a large negative number for the month, which we
        // define as a constant to improve readability.
        Symmetry454Chronology chronology = Symmetry454Chronology.INSTANCE;
        final int year = 3322;
        final int day = 8;
        final int invalidMonth = -2136356788;
        final Era era = IsoEra.BCE;

        // Act & Assert: We expect a DateTimeException when trying to create a date with the invalid month.
        DateTimeException thrown = assertThrows(
            DateTimeException.class,
            () -> chronology.date(era, year, invalidMonth, day)
        );

        // Further Assert: The exception message should confirm that the month value was the problem.
        assertTrue(
            "Exception message should indicate an invalid month",
            thrown.getMessage().contains("Invalid value for MonthOfYear")
        );
    }
}