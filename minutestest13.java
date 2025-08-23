package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Minutes#toString()} method.
 */
public class MinutesTest {

    @Test
    public void toString_shouldReturnCorrectFormat_forPositiveValue() {
        // Arrange
        Minutes twentyMinutes = Minutes.minutes(20);
        String expectedFormat = "PT20M";

        // Act
        String actualFormat = twentyMinutes.toString();

        // Assert
        assertEquals(expectedFormat, actualFormat);
    }

    @Test
    public void toString_shouldReturnCorrectFormat_forNegativeValue() {
        // Arrange
        Minutes negativeTwentyMinutes = Minutes.minutes(-20);
        String expectedFormat = "PT-20M";

        // Act
        String actualFormat = negativeTwentyMinutes.toString();

        // Assert
        assertEquals(expectedFormat, actualFormat);
    }
}