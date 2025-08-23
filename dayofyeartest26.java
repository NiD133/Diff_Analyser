package org.threeten.extra;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.temporal.TemporalField;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@code getLong(TemporalField)} method in {@link DayOfYear}.
 */
class DayOfYearTest {

    @Test
    void getLong_whenFieldIsNull_throwsNullPointerException() {
        // Arrange
        DayOfYear dayOfYear = DayOfYear.of(12);
        TemporalField nullField = null;

        // Act & Assert
        // The method contract requires the field to be non-null.
        assertThrows(NullPointerException.class, () -> dayOfYear.getLong(nullField));
    }
}