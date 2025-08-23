package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for the {@link Minutes} class.
 */
public class MinutesTest {

    /**
     * Tests that attempting to negate the minimum possible value of Minutes
     * throws an ArithmeticException, as this operation would cause an integer overflow.
     */
    @Test
    public void negatingMinValueThrowsArithmeticException() {
        // Arrange: The minimum possible value for Minutes, which corresponds to Integer.MIN_VALUE.
        Minutes minValue = Minutes.MIN_VALUE;

        // Act & Assert: Verify that negating this value throws the expected exception.
        try {
            minValue.negated();
            fail("Expected an ArithmeticException to be thrown, but no exception occurred.");
        } catch (ArithmeticException e) {
            // This is the expected outcome.
            // We can also assert the exception message for a more specific test.
            assertEquals("Integer.MIN_VALUE cannot be negated", e.getMessage());
        }
    }
}