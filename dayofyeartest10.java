package org.threeten.extra;

import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link DayOfYear} class, focusing on the {@code from(TemporalAccessor)} factory method.
 */
class DayOfYearFromTest {

    @Test
    void from_whenPassedDayOfYearInstance_returnsSameInstance() {
        // Arrange
        DayOfYear sourceDayOfYear = DayOfYear.of(6);

        // Act
        // The from() method should perform an identity conversion for a DayOfYear instance.
        DayOfYear result = DayOfYear.from(sourceDayOfYear);

        // Assert
        // The factory method is expected to return the exact same instance, not just an equal one.
        assertSame(sourceDayOfYear, result, "DayOfYear.from() should return the same instance if a DayOfYear is passed in.");
    }
}