package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Weeks} class.
 */
public class WeeksTest {

    @Test
    public void toStandardDuration_withMinValue_returnsCorrectDurationInDays() {
        // Arrange
        Weeks minWeeks = Weeks.MIN_VALUE;
        
        // The expected number of days is the minimum integer value multiplied by 7 (days in a week).
        // We cast to long to prevent potential integer overflow during the calculation.
        long expectedDays = (long) Integer.MIN_VALUE * 7;

        // Act
        Duration duration = minWeeks.toStandardDuration();

        // Assert
        assertEquals("The duration in days should be Integer.MIN_VALUE * 7",
                expectedDays, duration.getStandardDays());
    }
}