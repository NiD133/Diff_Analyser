package org.joda.time;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Unit tests for the {@link Seconds#secondsBetween(ReadablePartial, ReadablePartial)} factory method.
 */
public class SecondsTest {

    @Test
    public void secondsBetween_withReadablePartial_calculatesPositiveDifference() {
        // Arrange
        LocalTime start = new LocalTime(12, 0, 3);
        LocalTime end = new LocalTime(12, 0, 6);

        // Act
        Seconds result = Seconds.secondsBetween(start, end);

        // Assert
        assertEquals(3, result.getSeconds());
    }

    @Test
    public void secondsBetween_withSameReadablePartial_isZero() {
        // Arrange
        LocalTime time = new LocalTime(12, 0, 3);

        // Act
        Seconds result = Seconds.secondsBetween(time, time);

        // Assert
        assertEquals(0, result.getSeconds());
    }

    @Test
    public void secondsBetween_withEndBeforeStart_calculatesNegativeDifference() {
        // Arrange
        LocalTime start = new LocalTime(12, 0, 6);
        LocalTime end = new LocalTime(12, 0, 3);

        // Act
        Seconds result = Seconds.secondsBetween(start, end);

        // Assert
        assertEquals(-3, result.getSeconds());
    }

    @Test
    @SuppressWarnings("deprecation")
    public void secondsBetween_withDifferentReadablePartialTypes_calculatesCorrectly() {
        // Arrange
        LocalTime start = new LocalTime(12, 0, 3);
        // TimeOfDay is a deprecated class, but should be compatible with LocalTime.
        TimeOfDay end = new TimeOfDay(12, 0, 9);

        // Act
        Seconds result = Seconds.secondsBetween(start, end);

        // Assert
        assertEquals(6, result.getSeconds());
    }
}