package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the Seconds class.
 */
public class SecondsTest {

    /**
     * Tests that the negated() method is immutable. It should return a new instance
     * with the negated value and leave the original instance unchanged.
     */
    @Test
    public void negated_shouldReturnNewInstanceWithOppositeValue() {
        // Arrange: Create a Seconds instance with a specific value.
        final int initialValue = -2530;
        Seconds originalSeconds = Seconds.seconds(initialValue);

        // Act: Call the method under test.
        Seconds negatedSeconds = originalSeconds.negated();

        // Assert: Verify the behavior.
        // 1. The original object must remain unchanged (immutability).
        assertEquals("The original Seconds object should not be modified.",
                initialValue, originalSeconds.getSeconds());

        // 2. The new object should have the correctly negated value.
        assertEquals("The new Seconds object should have the negated value.",
                -initialValue, negatedSeconds.getSeconds());

        // 3. The method should return a new instance, not the same one.
        assertNotSame("negated() should return a new instance.",
                originalSeconds, negatedSeconds);
    }
}