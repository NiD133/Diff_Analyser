package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Weeks} class.
 */
public class WeeksTest {

    /**
     * Tests that converting a negative number of weeks to standard seconds
     * yields the correct negative number of seconds.
     */
    @Test
    public void toStandardSeconds_withNegativeWeeks_returnsCorrectSeconds() {
        // Arrange
        final int numberOfWeeks = -2820;
        Weeks weeks = Weeks.weeks(numberOfWeeks);

        // The expected number of seconds is calculated based on the standard definition:
        // -2820 weeks * (7 days/week * 24 hours/day * 60 minutes/hour * 60 seconds/minute)
        final int secondsInAWeek = 7 * 24 * 60 * 60;
        final int expectedSeconds = numberOfWeeks * secondsInAWeek;

        // Act
        Seconds actualSeconds = weeks.toStandardSeconds();

        // Assert
        assertEquals("The number of seconds should match the expected calculation.",
                     expectedSeconds, actualSeconds.getSeconds());
    }
}