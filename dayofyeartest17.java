package org.threeten.extra;

import static java.time.temporal.ChronoField.MONTH_OF_YEAR;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.temporal.UnsupportedTemporalTypeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * This class contains tests for the {@link DayOfYear#range(TemporalField)} method.
 * Note: The original class name 'DayOfYearTestTest17' was kept for consistency.
 */
public class DayOfYearTestTest17 {

    /**
     * Tests that calling range() with a field not supported by DayOfYear throws an exception.
     * The DayOfYear class is designed to only support the DAY_OF_YEAR field.
     */
    @Test
    @DisplayName("range() should throw UnsupportedTemporalTypeException for an unsupported field")
    void range_whenFieldIsUnsupported_throwsException() {
        // Arrange: Create a sample DayOfYear instance. The specific value is not important.
        DayOfYear dayOfYear = DayOfYear.of(12);

        // Act & Assert: Verify that calling range() with an unsupported field like MONTH_OF_YEAR
        // throws the expected exception.
        assertThrows(
                UnsupportedTemporalTypeException.class,
                () -> dayOfYear.range(MONTH_OF_YEAR),
                "Expected range() to throw for a field that is not DAY_OF_YEAR"
        );
    }
}