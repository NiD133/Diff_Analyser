package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link Minutes} class.
 */
public class MinutesTest {

    /**
     * Tests that converting the maximum possible number of minutes to weeks
     * results in the correct number of whole weeks.
     */
    @Test
    public void toStandardWeeks_whenConvertingMaxValue_calculatesWeeksCorrectly() {
        // Arrange: Define the conversion factor and the expected result.
        // A standard week contains 7 days * 24 hours/day * 60 minutes/hour = 10080 minutes.
        final int MINUTES_PER_WEEK = 7 * 24 * 60;
        
        // The method performs integer division, so we expect the integer part of the result.
        int expectedWeeks = Integer.MAX_VALUE / MINUTES_PER_WEEK;
        
        Minutes maxMinutes = Minutes.MAX_VALUE;

        // Act: Call the method under test.
        Weeks resultingWeeks = maxMinutes.toStandardWeeks();

        // Assert: Verify that the actual number of weeks matches the calculated expected value.
        assertEquals(expectedWeeks, resultingWeeks.getWeeks());
    }
}