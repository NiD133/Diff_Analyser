package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link Minutes} class.
 */
public class MinutesTest {

    /**
     * Tests that a {@link Minutes} value representing a whole number of standard days
     * is correctly converted to a {@link Days} object.
     */
    @Test
    public void toStandardDays_convertsMinutesEquivalentToTwoDays() {
        // Arrange: Create a Minutes instance equivalent to 2 standard days.
        // A standard day has a fixed number of minutes (24 hours * 60 minutes).
        final int twoDaysInMinutes = DateTimeConstants.MINUTES_PER_DAY * 2;
        Minutes periodInMinutes = Minutes.minutes(twoDaysInMinutes);
        Days expectedPeriodInDays = Days.days(2);

        // Act: Convert the Minutes object to Days.
        Days actualPeriodInDays = periodInMinutes.toStandardDays();

        // Assert: The result should be exactly 2 days.
        assertEquals(expectedPeriodInDays, actualPeriodInDays);
    }
}