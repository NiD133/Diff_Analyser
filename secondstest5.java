package org.joda.time;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Test suite for the Seconds class, focusing on the secondsIn(ReadableInterval) factory method.
 */
public class SecondsTest {

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");

    @Test
    public void secondsIn_givenNullInterval_returnsZero() {
        // Act
        Seconds result = Seconds.secondsIn(null);

        // Assert
        assertEquals(Seconds.ZERO, result);
    }

    @Test
    public void secondsIn_givenZeroDurationInterval_returnsZero() {
        // Arrange
        DateTime time = new DateTime(2006, 6, 9, 12, 0, 5, 0, PARIS);
        ReadableInterval interval = new Interval(time, time);

        // Act
        Seconds result = Seconds.secondsIn(interval);

        // Assert
        assertEquals(Seconds.ZERO, result);
    }

    @Test
    public void secondsIn_givenPositiveDurationInterval_calculatesDifference() {
        // Arrange
        DateTime start = new DateTime(2006, 6, 9, 12, 0, 3, 0, PARIS);
        DateTime end = new DateTime(2006, 6, 9, 12, 0, 9, 0, PARIS); // 6 seconds later
        ReadableInterval interval = new Interval(start, end);
        
        Seconds expected = Seconds.seconds(6);

        // Act
        Seconds result = Seconds.secondsIn(interval);

        // Assert
        assertEquals(expected, result);
    }
}