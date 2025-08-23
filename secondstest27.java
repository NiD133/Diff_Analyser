package org.joda.time;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Unit tests for the {@link Seconds} class.
 */
public class SecondsTest {

    @Test
    public void whenAddingToLocalDateTime_thenDateTimeIsCorrectlyAdvanced() {
        // Arrange
        final Seconds secondsPeriod = Seconds.seconds(26);
        final LocalDateTime startDateTime = new LocalDateTime(2006, 6, 1, 0, 0, 0, 0);
        final LocalDateTime expectedDateTime = new LocalDateTime(2006, 6, 1, 0, 0, 26, 0);

        // Act
        final LocalDateTime actualDateTime = startDateTime.plus(secondsPeriod);

        // Assert
        assertEquals(expectedDateTime, actualDateTime);
    }
}