package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for edge cases in the {@link Minutes} class.
 */
public class MinutesTest {

    /**
     * Tests that subtracting {@code Minutes.MIN_VALUE} throws an {@link ArithmeticException}.
     * <p>
     * The internal implementation of {@code minus(Minutes)} works by negating the subtrahend
     * and adding it to the minuend. However, negating {@code Integer.MIN_VALUE} results in
     * an integer overflow. This test verifies that the method correctly throws an
     * {@code ArithmeticException} for this specific edge case.
     */
    @Test
    public void shouldThrowExceptionWhenSubtractingMinValue() {
        // Arrange: Set up the values for the subtraction.
        // The minuend can be any value, as the exception is triggered by the subtrahend.
        Minutes minuend = Minutes.ZERO;
        Minutes subtrahend = Minutes.MIN_VALUE;

        // Act & Assert: Perform the operation and verify the expected exception.
        try {
            minuend.minus(subtrahend);
            fail("Expected an ArithmeticException but none was thrown.");
        } catch (ArithmeticException e) {
            // Verify that the exception message clearly indicates the cause of the error.
            assertEquals("Integer.MIN_VALUE cannot be negated", e.getMessage());
        }
    }
}