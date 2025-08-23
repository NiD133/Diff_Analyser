package org.threeten.extra;

import static java.time.temporal.ChronoField.DAY_OF_YEAR;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link DayOfYear#getLong(java.time.temporal.TemporalField)} method.
 */
class DayOfYearTest {

    @Test
    void getLongForDayOfYearFieldReturnsCorrectValue() {
        // Arrange
        int dayValue = 12;
        DayOfYear dayOfYear = DayOfYear.of(dayValue);
        long expectedValue = 12L;

        // Act
        long actualValue = dayOfYear.getLong(DAY_OF_YEAR);

        // Assert
        assertEquals(expectedValue, actualValue, "getLong(DAY_OF_YEAR) should return the day's value.");
    }
}