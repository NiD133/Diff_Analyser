package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Minutes} class.
 */
public class MinutesTest {

    @Test
    public void minus_shouldReturnNegativeResult_whenSubtractingLargerValue() {
        // Arrange
        Minutes twoMinutes = Minutes.TWO;
        Minutes threeMinutes = Minutes.THREE;
        int expectedResult = -1;

        // Act
        Minutes result = twoMinutes.minus(threeMinutes);

        // Assert
        assertEquals(expectedResult, result.getMinutes());
    }
}