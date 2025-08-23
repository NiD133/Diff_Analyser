package org.joda.time;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the {@link Minutes#minutesIn(ReadableInterval)} factory method.
 */
@DisplayName("Minutes.minutesIn(ReadableInterval)")
class Minutes_MinutesInTest {

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");

    @Test
    @DisplayName("should return zero minutes for a null interval")
    void returnsZeroForNullInterval() {
        // Act
        Minutes result = Minutes.minutesIn(null);

        // Assert
        assertEquals(Minutes.ZERO, result);
    }

    @Test
    @DisplayName("should return zero minutes for a zero-length interval")
    void returnsZeroForZeroLengthInterval() {
        // Arrange
        DateTime pointInTime = new DateTime(2006, 6, 9, 12, 0, 0, 0, PARIS);
        Interval zeroLengthInterval = new Interval(pointInTime, pointInTime);

        // Act
        Minutes result = Minutes.minutesIn(zeroLengthInterval);

        // Assert
        assertEquals(Minutes.ZERO, result);
    }

    @Test
    @DisplayName("should return the correct number of minutes for a standard interval")
    void returnsCorrectMinutesForPositiveInterval() {
        // Arrange
        DateTime start = new DateTime(2006, 6, 9, 12, 3, 0, 0, PARIS);
        DateTime end = new DateTime(2006, 6, 9, 12, 6, 0, 0, PARIS);
        Interval threeMinuteInterval = new Interval(start, end);

        // Act
        Minutes result = Minutes.minutesIn(threeMinuteInterval);

        // Assert
        assertEquals(Minutes.THREE, result);
    }

    @Test
    @DisplayName("should calculate minutes correctly for a longer interval")
    void returnsCorrectMinutesForLongerInterval() {
        // Arrange
        DateTime start = new DateTime(2006, 6, 9, 12, 3, 0, 0, PARIS);
        DateTime end = new DateTime(2006, 6, 9, 12, 9, 0, 0, PARIS);
        Interval sixMinuteInterval = new Interval(start, end);

        // Act
        Minutes result = Minutes.minutesIn(sixMinuteInterval);

        // Assert
        assertEquals(6, result.getMinutes());
    }
}