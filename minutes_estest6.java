package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link Minutes} class.
 */
public class MinutesTest {

    /**
     * Tests that converting the minimum possible number of minutes to weeks
     * results in the correct, large negative number of weeks.
     */
    @Test
    public void toStandardWeeks_whenConvertingMinValue_calculatesCorrectNegativeWeeks() {
        // Arrange
        // The number of minutes in a standard week is 7 days * 24 hours * 60 minutes.
        final int MINUTES_PER_WEEK = 10080;
        
        // The expected result is Integer.MIN_VALUE / MINUTES_PER_WEEK.
        // Joda-Time uses integer division, so the fractional part is truncated.
        // -2,147,483,648 / 10,080 = -213,044
        final int expectedWeeks = Integer.MIN_VALUE / MINUTES_PER_WEEK;
        
        Minutes minMinutes = Minutes.MIN_VALUE;

        // Act
        Weeks actualWeeks = minMinutes.toStandardWeeks();

        // Assert
        assertEquals("The number of weeks should be the result of integer division.",
                expectedWeeks, actualWeeks.getWeeks());
    }
}