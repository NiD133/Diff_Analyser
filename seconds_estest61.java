package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link Seconds} class.
 */
public class SecondsTest {

    /**
     * Tests that converting the minimum possible number of seconds to hours
     * results in the correct, truncated value.
     */
    @Test
    public void toStandardHours_fromMinValue_calculatesCorrectly() {
        // Arrange
        final int SECONDS_PER_HOUR = 3600; // 60 seconds/minute * 60 minutes/hour
        Seconds minSeconds = Seconds.MIN_VALUE;

        // The conversion uses integer division, so any fractional part is truncated.
        int expectedHours = Integer.MIN_VALUE / SECONDS_PER_HOUR;

        // Act
        Hours actualHours = minSeconds.toStandardHours();

        // Assert
        assertEquals("Conversion from minimum seconds to hours should be correct",
                expectedHours, actualHours.getHours());
    }
}