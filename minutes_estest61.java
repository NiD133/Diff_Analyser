package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Minutes} class.
 */
public class MinutesTest {

    /**
     * Tests that converting the minimum possible number of minutes to hours
     * correctly handles integer division and potential overflow.
     */
    @Test
    public void toStandardHours_withMinValue_returnsCorrectlyCalculatedHours() {
        // Arrange
        // This is an edge case test using the smallest possible integer value for minutes.
        Minutes minMinutes = Minutes.minutes(Integer.MIN_VALUE);

        // The expected number of hours is calculated using integer division,
        // which truncates any fractional part.
        // -2,147,483,648 / 60 = -35,791,394
        int expectedHours = Integer.MIN_VALUE / 60;

        // Act
        Hours actualHours = minMinutes.toStandardHours();

        // Assert
        assertEquals(expectedHours, actualHours.getHours());
    }
}