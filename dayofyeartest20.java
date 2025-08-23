package org.threeten.extra;

import static java.time.temporal.ChronoField.MONTH_OF_YEAR;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.temporal.TemporalField;
import java.time.temporal.UnsupportedTemporalTypeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DayOfYear#get(TemporalField)}.
 */
@DisplayName("DayOfYear.get(TemporalField)")
class DayOfYearTest {

    @Test
    @DisplayName("should throw UnsupportedTemporalTypeException for a field it does not support")
    void get_withUnsupportedField_shouldThrowException() {
        // Arrange: Create a DayOfYear instance and select an unsupported field.
        // A DayOfYear only supports the DAY_OF_YEAR field.
        DayOfYear dayOfYear = DayOfYear.of(12);
        TemporalField unsupportedField = MONTH_OF_YEAR;

        // Act & Assert: Verify that calling get() with the unsupported field throws the correct exception.
        assertThrows(
                UnsupportedTemporalTypeException.class,
                () -> dayOfYear.get(unsupportedField),
                "Expected get() to throw for an unsupported field, but it didn't."
        );
    }
}