package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Contains tests for the {@link Seconds} class, focusing on comparison methods.
 */
public class SecondsTest {

    /**
     * Verifies that a Seconds instance is never considered less than itself.
     */
    @Test
    public void isLessThan_shouldReturnFalse_whenComparingInstanceToItself() {
        // Arrange: Create a Seconds instance. Using a non-zero, arbitrary value
        // demonstrates the principle holds for any instance, not just a special case like ZERO.
        Seconds fortyTwoSeconds = Seconds.seconds(42);
        
        // The original test implicitly used Seconds.ZERO. We can test that as well for completeness.
        Seconds zeroSeconds = Seconds.ZERO;

        // Act & Assert: An object should never be less than itself.
        assertFalse("A non-zero Seconds instance should not be less than itself.", fortyTwoSeconds.isLessThan(fortyTwoSeconds));
        assertFalse("Seconds.ZERO should not be less than itself.", zeroSeconds.isLessThan(zeroSeconds));
    }
}