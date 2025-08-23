package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the factory method {@link Seconds#secondsBetween(ReadableInstant, ReadableInstant)}.
 */
public class SecondsTest {

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");

    @Test
    public void secondsBetween_shouldReturnPositiveValue_whenEndIsAfterStart() {
        // Arrange
        DateTime startTime = new DateTime(2006, 6, 9, 12, 0, 3, 0, PARIS);
        DateTime endTime = new DateTime(2006, 6, 9, 12, 0, 6, 0, PARIS);
        int expectedSeconds = 3;

        // Act
        Seconds result = Seconds.secondsBetween(startTime, endTime);

        // Assert
        assertEquals("The number of seconds should be the difference between end and start.",
                     expectedSeconds, result.getSeconds());
    }

    @Test
    public void secondsBetween_shouldReturnNegativeValue_whenStartIsAfterEnd() {
        // Arrange
        DateTime endTime = new DateTime(2006, 6, 9, 12, 0, 3, 0, PARIS);
        DateTime startTime = new DateTime(2006, 6, 9, 12, 0, 6, 0, PARIS);
        int expectedSeconds = -3;

        // Act
        Seconds result = Seconds.secondsBetween(startTime, endTime);

        // Assert
        assertEquals("A negative value should be returned when the start instant is after the end instant.",
                     expectedSeconds, result.getSeconds());
    }

    @Test
    public void secondsBetween_shouldReturnZero_whenInstantsAreTheSame() {
        // Arrange
        DateTime instant = new DateTime(2006, 6, 9, 12, 0, 3, 0, PARIS);
        int expectedSeconds = 0;

        // Act
        Seconds result = Seconds.secondsBetween(instant, instant);

        // Assert
        assertEquals("Zero seconds should be returned when the start and end instants are identical.",
                     expectedSeconds, result.getSeconds());
    }
}