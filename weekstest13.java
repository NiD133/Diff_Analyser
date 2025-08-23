package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Weeks#toString()} method.
 */
public class WeeksTest {

    @Test
    public void toString_returnsCorrectISO8601FormatForPositiveWeeks() {
        // Arrange
        Weeks twentyWeeks = Weeks.weeks(20);
        String expectedString = "P20W";

        // Act
        String actualString = twentyWeeks.toString();

        // Assert
        assertEquals(expectedString, actualString);
    }

    @Test
    public void toString_returnsCorrectISO8601FormatForNegativeWeeks() {
        // Arrange
        Weeks negativeTwentyWeeks = Weeks.weeks(-20);
        String expectedString = "P-20W";

        // Act
        String actualString = negativeTwentyWeeks.toString();

        // Assert
        assertEquals(expectedString, actualString);
    }
}