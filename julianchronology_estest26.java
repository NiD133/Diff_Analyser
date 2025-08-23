package org.threeten.extra.chrono;

import org.junit.Test;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAccessor;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * This test class contains improved versions of tests for {@link JulianChronology}.
 * The original test class name, JulianChronology_ESTestTest26, suggests it was
 * automatically generated.
 */
public class JulianChronology_ESTestTest26 {

    /**
     * Verifies that creating a JulianDate from a TemporalAccessor fails when the
     * year is outside the supported range.
     * <p>
     * The valid proleptic year range for JulianChronology is -999,998 to 999,999.
     */
    @Test
    public void dateFromTemporalAccessor_whenYearIsTooLarge_throwsDateTimeException() {
        // Arrange: Create a standard date with a year that exceeds the JulianChronology's maximum.
        // The maximum supported year is 999,999, so we use 1,000,000.
        final int outOfRangeYear = 1_000_000;
        TemporalAccessor dateWithYearOutOfRange = LocalDate.of(outOfRangeYear, Month.JANUARY, 1);
        JulianChronology julianChronology = JulianChronology.INSTANCE;

        // Act & Assert: Attempting to convert this date to a JulianDate should throw an exception.
        DateTimeException exception = assertThrows(
                DateTimeException.class,
                () -> julianChronology.date(dateWithYearOutOfRange)
        );

        // Assert: The exception message should clearly indicate an invalid year value.
        assertTrue(
                "Exception message should contain 'Invalid value for Year'",
                exception.getMessage().contains("Invalid value for Year")
        );
    }
}