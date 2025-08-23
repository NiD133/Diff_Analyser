package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test cases for the {@link Minutes#minutesBetween(ReadableInstant, ReadableInstant)} factory method.
 */
public class MinutesTest {

    // Using a constant for the time zone is good practice to avoid test flakiness.
    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");

    @Test
    public void minutesBetween_shouldCalculatePositiveDifference() {
        // Arrange
        DateTime start = new DateTime(2006, 6, 9, 12, 3, 0, 0, PARIS);
        DateTime end = new DateTime(2006, 6, 9, 12, 6, 0, 0, PARIS);

        // Act
        Minutes result = Minutes.minutesBetween(start, end);

        // Assert
        assertEquals(3, result.getMinutes());
    }

    @Test
    public void minutesBetween_shouldBeZeroForSameInstant() {
        // Arrange
        DateTime instant = new DateTime(2006, 6, 9, 12, 3, 0, 0, PARIS);

        // Act
        Minutes result = Minutes.minutesBetween(instant, instant);

        // Assert
        assertEquals(0, result.getMinutes());
    }

    @Test
    public void minutesBetween_shouldCalculateNegativeDifferenceWhenEndIsBeforeStart() {
        // Arrange
        DateTime start = new DateTime(2006, 6, 9, 12, 6, 0, 0, PARIS);
        DateTime end = new DateTime(2006, 6, 9, 12, 3, 0, 0, PARIS);

        // Act
        Minutes result = Minutes.minutesBetween(start, end);

        // Assert
        assertEquals(-3, result.getMinutes());
    }

    @Test
    public void minutesBetween_shouldCalculateLargerPositiveDifference() {
        // Arrange
        DateTime start = new DateTime(2006, 6, 9, 12, 3, 0, 0, PARIS);
        DateTime end = new DateTime(2006, 6, 9, 12, 9, 0, 0, PARIS);

        // Act
        Minutes result = Minutes.minutesBetween(start, end);

        // Assert
        assertEquals(6, result.getMinutes());
    }
}