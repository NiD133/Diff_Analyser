package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This test class focuses on the behavior of the Weeks.minus() method
 * under specific edge-case conditions.
 */
// Note: The original test class name and scaffolding are kept for context.
// In a real-world scenario, they would likely be simplified to "WeeksTest".
public class Weeks_ESTestTest33 extends Weeks_ESTest_scaffolding {

    /**
     * Verifies that subtracting Weeks.MIN_VALUE from another Weeks object
     * throws an ArithmeticException.
     * <p>
     * This behavior is expected because the internal implementation of the minus() method
     * negates the subtrahend (the value being subtracted). Attempting to negate
     * Integer.MIN_VALUE results in an integer overflow, as its positive counterpart
     * is larger than Integer.MAX_VALUE.
     */
    @Test
    public void minus_whenSubtractingMinValue_throwsArithmeticException() {
        // Arrange
        Weeks baseWeeks = Weeks.MIN_VALUE;
        Weeks weeksToSubtract = Weeks.MIN_VALUE;
        String expectedErrorMessage = "Integer.MIN_VALUE cannot be negated";

        // Act & Assert
        try {
            baseWeeks.minus(weeksToSubtract);
            fail("Expected an ArithmeticException to be thrown due to integer overflow on negation.");
        } catch (ArithmeticException e) {
            // Verify that the correct exception was thrown with the expected message.
            assertEquals(expectedErrorMessage, e.getMessage());
        }
    }
}