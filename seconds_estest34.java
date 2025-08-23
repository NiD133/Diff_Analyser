package org.joda.time;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for the Seconds class.
 */
public class SecondsTest {

    /**
     * Tests that subtracting MIN_VALUE from MAX_VALUE throws an ArithmeticException.
     * This is a known integer overflow edge case.
     */
    @Test
    public void minus_whenSubtractingMinValueFromMaxValue_throwsArithmeticException() {
        // Arrange: Define the two edge-case values for Seconds.
        // The operation will be MAX_VALUE - MIN_VALUE.
        Seconds maxSeconds = Seconds.MAX_VALUE; // Represents Integer.MAX_VALUE
        Seconds minSeconds = Seconds.MIN_VALUE; // Represents Integer.MIN_VALUE

        // Act & Assert: Attempt the subtraction and verify the expected exception.
        try {
            maxSeconds.minus(minSeconds);
            fail("Expected an ArithmeticException to be thrown due to integer overflow.");
        } catch (ArithmeticException e) {
            // The subtraction (MAX_VALUE - MIN_VALUE) is implemented internally
            // in a way that requires negating MIN_VALUE. However, Integer.MIN_VALUE
            // cannot be safely negated because its positive counterpart is too large
            // to fit in an int. This is what triggers the exception.
            assertEquals("Integer.MIN_VALUE cannot be negated", e.getMessage());
        }
    }
}