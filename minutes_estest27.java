package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Minutes} class.
 */
public class MinutesTest {

    @Test
    public void minus_whenSubtractingLargerInteger_shouldReturnNegativeResult() {
        // Arrange
        final Minutes oneMinute = Minutes.ONE;
        final int minutesToSubtract = 192;
        final int expectedResult = -191;

        // Act
        Minutes result = oneMinute.minus(minutesToSubtract);

        // Assert
        assertEquals(expectedResult, result.getMinutes());
    }
}