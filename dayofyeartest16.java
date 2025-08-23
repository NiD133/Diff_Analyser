package org.threeten.extra;

import static java.time.temporal.ChronoField.DAY_OF_YEAR;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.temporal.ValueRange;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link DayOfYear#range(TemporalField)} method.
 */
class DayOfYearTest {

    private static final DayOfYear TEST_DAY_OF_YEAR = DayOfYear.of(12);

    @Test
    @DisplayName("range(DAY_OF_YEAR) should return the field's standard range")
    void range_forDayOfYearField_returnsCorrectRange() {
        // Arrange
        // The standard range for the DAY_OF_YEAR field is 1-366,
        // accommodating both standard and leap years.
        ValueRange expectedRange = DAY_OF_YEAR.range();

        // Act
        // The range for DAY_OF_YEAR on a DayOfYear instance should be constant
        // regardless of the specific day value.
        ValueRange actualRange = TEST_DAY_OF_YEAR.range(DAY_OF_YEAR);

        // Assert
        assertEquals(expectedRange, actualRange);
    }
}