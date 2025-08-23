package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Minutes} class.
 */
public class MinutesTest {

    @Test
    public void standardMinutesIn_shouldCorrectlyConvertDaysToMinutes() {
        // Arrange
        Days threeDays = Days.THREE;
        // There are 1440 minutes in a day (24 hours * 60 minutes).
        // For 3 days, the expected total is 3 * 1440 = 4320 minutes.
        int expectedMinutes = 3 * 24 * 60;

        // Act
        Minutes result = Minutes.standardMinutesIn(threeDays);

        // Assert
        assertEquals(expectedMinutes, result.getMinutes());
    }
}