package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Seconds} class.
 */
public class SecondsTest {

    /**
     * Tests that the toStandardHours() method correctly converts a large number of seconds
     * into hours by performing integer division.
     */
    @Test
    public void toStandardHours_shouldConvertSecondsToHoursUsingIntegerDivision() {
        // Arrange
        // There are 3600 seconds in one hour (60 seconds/minute * 60 minutes/hour).
        final int SECONDS_PER_HOUR = 3600;
        
        final int inputInSeconds = 690562340;
        Seconds seconds = Seconds.seconds(inputInSeconds);
        
        // The conversion to hours is expected to use integer division, truncating any remainder.
        int expectedHours = inputInSeconds / SECONDS_PER_HOUR; // 690562340 / 3600 = 191822
        
        // Act
        Hours actualHours = seconds.toStandardHours();
        
        // Assert
        assertEquals("The number of hours should be calculated via integer division.",
                expectedHours, actualHours.getHours());
    }
}