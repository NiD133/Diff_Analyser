package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link Weeks} class, focusing on conversion methods.
 */
public class WeeksTest {

    /**
     * Tests that converting a negative number of weeks to standard minutes
     * produces the correct negative minute count.
     */
    @Test
    public void toStandardMinutes_forNegativeWeeks_returnsCorrectlyCalculatedMinutes() {
        // Arrange
        final int numberOfWeeks = -3765;
        Weeks negativeWeeks = Weeks.weeks(numberOfWeeks);

        // The expected number of minutes is calculated based on standard conversions.
        // 1 week = 7 days, 1 day = 24 hours, 1 hour = 60 minutes.
        final int MINUTES_PER_WEEK = 7 * 24 * 60;
        final int expectedMinutes = numberOfWeeks * MINUTES_PER_WEEK; // -3765 * 10080 = -37951200

        // Act
        Minutes actualMinutes = negativeWeeks.toStandardMinutes();

        // Assert
        assertEquals("The total number of minutes should be correctly calculated.",
                expectedMinutes, actualMinutes.getMinutes());
    }
}