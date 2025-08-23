package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Minutes} class.
 */
public class MinutesTest {

    /**
     * Tests that adding a negative integer to a Minutes object correctly
     * decreases its value.
     */
    @Test
    public void plus_whenAddingNegativeValue_thenResultIsDecreased() {
        // Arrange
        final Minutes zeroMinutes = Minutes.ZERO;
        final int negativeValueToAdd = -2104;
        
        // Act
        Minutes result = zeroMinutes.plus(negativeValueToAdd);
        
        // Assert
        assertEquals(negativeValueToAdd, result.getMinutes());
    }
}