package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Weeks} class.
 */
public class WeeksTest {

    /**
     * Tests that {@link Weeks#standardWeeksIn(ReadablePeriod)} correctly calculates
     * the number of weeks from the minimum possible number of hours.
     * This serves as a test for boundary conditions.
     */
    @Test
    public void standardWeeksIn_withMinimumValueHours_calculatesCorrectly() {
        // Arrange
        // A standard week has 7 days, and a standard day has 24 hours.
        final int HOURS_PER_WEEK = 24 * 7;
        
        // Use Hours.MIN_VALUE to test the lower boundary condition.
        Hours minHours = Hours.MIN_VALUE;
        
        // The expected result is the total number of hours divided by the hours in a week.
        // This is an integer division, so any remainder is truncated.
        int expectedWeeks = Integer.MIN_VALUE / HOURS_PER_WEEK;

        // Act
        Weeks actualWeeks = Weeks.standardWeeksIn(minHours);

        // Assert
        assertEquals("The number of weeks should be correctly calculated from MIN_VALUE hours.",
                expectedWeeks, actualWeeks.getWeeks());
    }
}