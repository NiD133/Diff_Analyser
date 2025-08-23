package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Minutes} class.
 */
public class MinutesTest {

    @Test
    public void toString_forMaxValue_shouldReturnCorrectISO8601Format() {
        // Arrange
        // The Minutes.MAX_VALUE constant holds the largest possible number of minutes.
        Minutes maxMinutes = Minutes.MAX_VALUE;
        
        // The expected string follows the ISO 8601 period format for minutes: "PT<number of minutes>M".
        // We construct it programmatically to make the test's intent clear.
        String expectedIsoString = "PT" + Integer.MAX_VALUE + "M";

        // Act
        String actualIsoString = maxMinutes.toString();

        // Assert
        assertEquals(expectedIsoString, actualIsoString);
    }
}