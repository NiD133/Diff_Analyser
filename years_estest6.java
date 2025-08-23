package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Years} class, focusing on arithmetic operations.
 */
public class YearsTest {

    @Test
    public void plus_shouldReturnCorrectSum_whenAddingMinValue() {
        // Arrange
        Years twoYears = Years.TWO;
        Years minYears = Years.MIN_VALUE;
        int expectedYearsValue = 2 + Integer.MIN_VALUE; // Explicitly shows the calculation

        // Act
        Years result = twoYears.plus(minYears);

        // Assert
        assertEquals(expectedYearsValue, result.getYears());
    }
}