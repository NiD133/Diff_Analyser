package org.joda.time;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the {@link Minutes} class.
 */
public class MinutesTest {

    private static final int MINUTES_PER_HOUR = 60;

    @Test
    public void toStandardHours_convertsCorrectly() {
        // Arrange
        final int hoursValue = 3;
        final Minutes threeHoursInMinutes = Minutes.minutes(hoursValue * MINUTES_PER_HOUR);
        final Hours expected = Hours.hours(hoursValue);

        // Act
        final Hours actual = threeHoursInMinutes.toStandardHours();

        // Assert
        assertEquals("Conversion from minutes to hours should be correct", expected, actual);
    }
}