package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Minutes} class.
 */
public class MinutesTest {

    /**
     * Verifies that converting from Minutes to Hours correctly truncates any
     * remaining minutes, effectively performing an integer division.
     */
    @Test
    public void toStandardHours_shouldConvertMinutesToWholeHoursAndTruncateRemainder() {
        // Arrange
        // Create a Minutes instance that is not an exact multiple of hours.
        // 1431 minutes is equivalent to 23 hours and 51 minutes.
        final int hoursComponent = 23;
        final int minutesComponent = 51;
        final int totalMinutes = (hoursComponent * 60) + minutesComponent; // 1431
        
        Minutes minutes = Minutes.minutes(totalMinutes);

        // Act
        // Convert the Minutes object to Hours.
        Hours resultingHours = minutes.toStandardHours();

        // Assert
        // The conversion should result in 23 whole hours, with the remaining
        // 51 minutes being truncated.
        assertEquals(hoursComponent, resultingHours.getHours());
    }
}