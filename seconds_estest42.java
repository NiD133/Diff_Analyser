package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link Seconds} class, focusing on comparison methods.
 */
public class SecondsTest {

    /**
     * Verifies that isGreaterThan() returns true when comparing a larger Seconds
     * instance to a smaller one.
     */
    @Test
    public void isGreaterThan_shouldReturnTrue_whenComparingLargerToSmaller() {
        // Arrange: Define two Seconds instances where one is clearly greater than the other.
        Seconds greaterSeconds = Seconds.seconds(2);
        Seconds lesserSeconds = Seconds.MIN_VALUE;

        // Act & Assert: Directly assert the result of the comparison.
        assertTrue("Expected 2 seconds to be greater than MIN_VALUE seconds",
                   greaterSeconds.isGreaterThan(lesserSeconds));
    }
}