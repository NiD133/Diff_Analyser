package org.joda.time;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Unit tests for the Minutes class, focusing on the plus(int) method.
 */
public class MinutesTest {

    @Test
    public void plus_shouldAddTheSpecifiedNumberOfMinutes() {
        // Arrange
        final Minutes twoMinutes = Minutes.minutes(2);
        final int minutesToAdd = 3;
        final int expectedTotalMinutes = 5;

        // Act
        Minutes result = twoMinutes.plus(minutesToAdd);

        // Assert
        // A new instance with the correct value should be returned.
        assertEquals(expectedTotalMinutes, result.getMinutes());
        
        // The original instance should remain unchanged (verifying immutability).
        assertEquals(2, twoMinutes.getMinutes());
    }

    @Test
    public void plus_whenAddingZero_shouldReturnAnEquivalentInstance() {
        // Arrange
        final Minutes oneMinute = Minutes.ONE;

        // Act
        Minutes result = oneMinute.plus(0);

        // Assert
        assertEquals(1, result.getMinutes());
    }

    @Test(expected = ArithmeticException.class)
    public void plus_whenResultingInOverflow_shouldThrowArithmeticException() {
        // Arrange
        final Minutes maxMinutes = Minutes.MAX_VALUE;

        // Act
        // This action is expected to throw an ArithmeticException.
        maxMinutes.plus(1);
    }
}