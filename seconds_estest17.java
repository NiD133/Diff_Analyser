package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Seconds} class.
 */
public class SecondsTest {

    /**
     * Tests that negating a Seconds object with a value of zero
     * results in a new object that is also zero.
     * It also verifies the immutability of the original object.
     */
    @Test
    public void negatedOnZeroReturnsZero() {
        // Arrange: Create a Seconds object representing zero seconds.
        // Using the constant Seconds.ZERO is the clearest way to do this.
        Seconds zeroSeconds = Seconds.ZERO;

        // Act: Call the negated() method and capture the new instance.
        Seconds result = zeroSeconds.negated();

        // Assert: Verify the result and the original object's state.
        // 1. The new object should have a value of 0.
        assertEquals("Negating zero should result in zero.", 0, result.getSeconds());
        
        // 2. The original object should remain unchanged, confirming immutability.
        assertEquals("Original object should not be mutated.", 0, zeroSeconds.getSeconds());
    }
}