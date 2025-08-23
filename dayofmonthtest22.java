package org.threeten.extra;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.temporal.ChronoField;
import java.time.temporal.UnsupportedTemporalTypeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@code getLong(TemporalField)} method in {@link DayOfMonth}.
 */
class DayOfMonthGetLongTest {

    @Test
    @DisplayName("getLong() with DAY_OF_MONTH field returns the correct day value")
    void getLong_withSupportedField_shouldReturnCorrectValue() {
        // Arrange
        DayOfMonth dayOfMonth12 = DayOfMonth.of(12);
        long expectedDay = 12L;

        // Act
        long actualDay = dayOfMonth12.getLong(ChronoField.DAY_OF_MONTH);

        // Assert
        assertEquals(expectedDay, actualDay, "The day value should be 12");
    }

    @Test
    @DisplayName("getLong() with an unsupported field throws UnsupportedTemporalTypeException")
    void getLong_withUnsupportedField_shouldThrowException() {
        // Arrange
        DayOfMonth dayOfMonth = DayOfMonth.of(23);
        // A DayOfMonth instance only supports the DAY_OF_MONTH field.
        // Any other field, like YEAR, should be rejected.
        ChronoField unsupportedField = ChronoField.YEAR;

        // Act & Assert
        assertThrows(
                UnsupportedTemporalTypeException.class,
                () -> dayOfMonth.getLong(unsupportedField),
                "Expected getLong() to throw for an unsupported field");
    }
}