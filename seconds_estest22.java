package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link Seconds} class, focusing on arithmetic operations.
 */
public class SecondsTest {

    /**
     * Tests that subtracting a larger Seconds object from a smaller one
     * correctly results in a negative value. This verifies the behavior of
     * the minus(Seconds) method.
     */
    @Test
    public void minus_whenSubtractingLargerValue_shouldReturnCorrectNegativeResult() {
        // Arrange: Set up the initial state and values for the test.
        final int baseValue = 352831696;
        final Seconds baseSeconds = Seconds.seconds(baseValue);
        
        final int amountToAdd = 3058;
        
        // Create a larger Seconds instance by adding a positive integer.
        // This will be used as the subtrahend in the 'minus' operation.
        final Seconds largerSeconds = baseSeconds.plus(amountToAdd);
        
        // Act: Perform the operation under test.
        final Seconds difference = baseSeconds.minus(largerSeconds);

        // Assert: Verify the outcome is as expected.
        // The expected result is the negation of the amount that was added.
        final int expectedDifference = -amountToAdd;
        assertEquals(
            "Subtracting a larger Seconds object should yield a negative result equal in magnitude to the difference.",
            expectedDifference,
            difference.getSeconds()
        );
    }
}