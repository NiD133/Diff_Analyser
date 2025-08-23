package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the Seconds class.
 */
public class SecondsTest {

    /**
     * Tests that adding a negative Seconds value to itself results in a new
     * Seconds object with the correct doubled negative value.
     * It also verifies the immutability of the original Seconds object.
     */
    @Test
    public void plus_withNegativeValue_shouldReturnCorrectSumAndPreserveImmutability() {
        // Arrange: Create a Seconds instance with a negative value.
        final int initialValue = -2530;
        final Seconds negativeSeconds = Seconds.seconds(initialValue);
        final int expectedSum = initialValue * 2; // -5060

        // Act: Add the Seconds instance to itself.
        Seconds result = negativeSeconds.plus(negativeSeconds);

        // Assert: Verify the result and the immutability of the original object.
        
        // 1. The new Seconds object should have the correct sum.
        assertEquals("The sum of two negative Seconds objects should be correct.",
                     expectedSum, result.getSeconds());
        
        // 2. The original Seconds object should remain unchanged.
        assertEquals("The original Seconds object should be immutable.",
                     initialValue, negativeSeconds.getSeconds());
    }
}