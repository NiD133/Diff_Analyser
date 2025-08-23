package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Minutes} class.
 */
public class MinutesTest {

    @Test
    public void plus_whenAddingNegativeValue_shouldReturnCorrectlySubtractedMinutes() {
        // Arrange
        Minutes twoMinutes = Minutes.TWO;
        Minutes expectedResult = Minutes.ZERO;

        // Act
        // Adding a negative value is equivalent to subtraction.
        Minutes actualResult = twoMinutes.plus(-2);

        // Assert
        assertEquals(expectedResult, actualResult);
    }
}