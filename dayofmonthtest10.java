package org.threeten.extra;

import static org.junit.jupiter.api.Assertions.assertSame;

import java.time.temporal.TemporalAccessor;
import org.junit.jupiter.api.Test;

/**
 * Tests for the factory method {@link DayOfMonth#from(TemporalAccessor)}.
 */
class DayOfMonthFromTest {

    @Test
    void from_shouldReturnSameInstance_whenInputIsAlreadyDayOfMonth() {
        // Arrange
        DayOfMonth inputDayOfMonth = DayOfMonth.of(6);

        // Act
        DayOfMonth result = DayOfMonth.from(inputDayOfMonth);

        // Assert
        // The from() method is optimized to return the same instance
        // if a DayOfMonth is passed in, so we use assertSame.
        assertSame(inputDayOfMonth, result, "DayOfMonth.from() should return the same instance for a DayOfMonth input");
    }
}