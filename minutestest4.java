package org.joda.time;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Test cases for the factory method {@link Minutes#minutesBetween(ReadablePartial, ReadablePartial)}.
 */
public class MinutesTest {

    @Test
    public void minutesBetween_shouldReturnPositiveMinutes_whenEndIsAfterStart() {
        // Arrange
        LocalTime startTime = new LocalTime(12, 3);
        LocalTime endTime = new LocalTime(12, 6);

        // Act
        Minutes result = Minutes.minutesBetween(startTime, endTime);

        // Assert
        assertEquals(3, result.getMinutes());
    }

    @Test
    public void minutesBetween_shouldReturnZeroMinutes_whenStartAndEndAreSame() {
        // Arrange
        LocalTime time = new LocalTime(12, 3);

        // Act
        Minutes result = Minutes.minutesBetween(time, time);

        // Assert
        assertEquals(0, result.getMinutes());
    }

    @Test
    public void minutesBetween_shouldReturnNegativeMinutes_whenStartIsAfterEnd() {
        // Arrange
        LocalTime startTime = new LocalTime(12, 6);
        LocalTime endTime = new LocalTime(12, 3);

        // Act
        Minutes result = Minutes.minutesBetween(startTime, endTime);

        // Assert
        assertEquals(-3, result.getMinutes());
    }

    @Test
    @SuppressWarnings("deprecation") // Testing interoperability with deprecated TimeOfDay
    public void minutesBetween_shouldCalculateCorrectly_forDifferentReadablePartialTypes() {
        // Arrange
        LocalTime startTime = new LocalTime(12, 3);
        // TimeOfDay is a deprecated ReadablePartial implementation.
        TimeOfDay endTime = new TimeOfDay(12, 9);

        // Act
        Minutes result = Minutes.minutesBetween(startTime, endTime);

        // Assert
        assertEquals(6, result.getMinutes());
    }
}