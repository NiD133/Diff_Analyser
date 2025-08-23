package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * A test suite for the {@link Seconds} class, focusing on the minus operation.
 */
public class SecondsTest {

    /**
     * Tests that subtracting a positive integer from zero seconds
     * results in a negative Seconds value.
     */
    @Test
    public void minus_whenSubtractingOneFromZero_shouldReturnNegativeOne() {
        // Arrange
        Seconds zeroSeconds = Seconds.ZERO;
        int secondsToSubtract = 1;

        // Act
        Seconds result = zeroSeconds.minus(secondsToSubtract);

        // Assert
        int expectedSeconds = -1;
        assertEquals(expectedSeconds, result.getSeconds());
    }
}