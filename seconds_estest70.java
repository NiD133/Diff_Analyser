package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link Seconds} class.
 */
public class SecondsTest {

    /**
     * Tests that converting Seconds.MAX_VALUE to a standard duration
     * correctly calculates the equivalent number of standard days.
     */
    @Test
    public void toStandardDuration_withMaxValue_convertsToCorrectNumberOfDays() {
        // Arrange
        Seconds maxSeconds = Seconds.MAX_VALUE;
        // A standard day has 24 hours * 60 minutes * 60 seconds = 86400 seconds.
        final int SECONDS_PER_DAY = 86400;
        long expectedDays = (long) Integer.MAX_VALUE / SECONDS_PER_DAY;

        // Act
        Duration duration = maxSeconds.toStandardDuration();
        long actualDays = duration.getStandardDays();

        // Assert
        assertEquals(expectedDays, actualDays);
    }
}