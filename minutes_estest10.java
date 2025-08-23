package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Minutes} class.
 */
public class MinutesTest {

    /**
     * Tests that converting the maximum possible Minutes value to a standard duration
     * correctly calculates the equivalent number of standard days.
     */
    @Test
    public void toStandardDuration_fromMaxValue_convertsToCorrectNumberOfDays() {
        // Arrange
        Minutes maxMinutes = Minutes.MAX_VALUE;
        
        // A standard day has 24 hours * 60 minutes/hour = 1440 minutes.
        // The expected number of days is the total minutes (Integer.MAX_VALUE)
        // divided by the number of minutes in a day.
        long expectedDays = (long) Integer.MAX_VALUE / (24 * 60);

        // Act
        Duration duration = maxMinutes.toStandardDuration();
        long actualDays = duration.getStandardDays();

        // Assert
        assertEquals(expectedDays, actualDays);
    }
}