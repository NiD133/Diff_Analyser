package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;

import org.joda.time.TimeOfDay;
import org.junit.Test;

/**
 * Tests for adding durations to a {@link TimeOfDay} instance.
 * This test class focuses on verifying the wrap-around behavior of time arithmetic
 * when the result crosses midnight.
 */
public class TimeOfDayAdditionTest {

    @Test
    public void plusHours_shouldWrapAroundPastMidnight() {
        // Arrange
        TimeOfDay startTime = new TimeOfDay(12, 30);
        // 12:30 + 22 hours = 34:30, which wraps around to 10:30 on the "next day".
        TimeOfDay expectedTime = new TimeOfDay(10, 30);

        // Act
        TimeOfDay resultTime = startTime.plusHours(22);

        // Assert
        assertEquals(expectedTime, resultTime);
    }

    @Test
    public void minusHours_shouldWrapAroundBeforePreviousMidnight() {
        // Arrange
        TimeOfDay startTime = new TimeOfDay(10, 30);
        // 10:30 - 22 hours = -11:30, which wraps around to 12:30 on the "previous day".
        TimeOfDay expectedTime = new TimeOfDay(12, 30);

        // Act
        TimeOfDay resultTime = startTime.minusHours(22);

        // Assert
        assertEquals(expectedTime, resultTime);
    }

    @Test
    public void plusMinutes_shouldWrapAroundPastMidnight() {
        // Arrange
        TimeOfDay startTime = new TimeOfDay(12, 30);
        int hoursToAddInMinutes = 22 * 60;
        // 12:30 + 22 hours (in minutes) = 34:30, which wraps around to 10:30.
        TimeOfDay expectedTime = new TimeOfDay(10, 30);

        // Act
        TimeOfDay resultTime = startTime.plusMinutes(hoursToAddInMinutes);

        // Assert
        assertEquals(expectedTime, resultTime);
    }

    @Test
    public void minusMinutes_shouldWrapAroundBeforePreviousMidnight() {
        // Arrange
        TimeOfDay startTime = new TimeOfDay(10, 30);
        int hoursToSubtractInMinutes = 22 * 60;
        // 10:30 - 22 hours (in minutes) = -11:30, which wraps around to 12:30.
        TimeOfDay expectedTime = new TimeOfDay(12, 30);

        // Act
        TimeOfDay resultTime = startTime.minusMinutes(hoursToSubtractInMinutes);

        // Assert
        assertEquals(expectedTime, resultTime);
    }
}