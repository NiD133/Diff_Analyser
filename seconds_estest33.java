package org.joda.time;

import org.junit.Test;

/**
 * A test suite for the {@link Seconds} class, focusing on edge cases.
 */
public class SecondsTest {

    /**
     * Verifies that adding a negative number to Seconds.MIN_VALUE correctly
     * throws an ArithmeticException due to integer underflow.
     */
    @Test(expected = ArithmeticException.class)
    public void plus_whenResultUnderflows_throwsArithmeticException() {
        // Arrange: Start with the minimum possible value for Seconds.
        Seconds minValue = Seconds.MIN_VALUE;

        // Act: Attempt to subtract, which should cause an integer underflow.
        // The test will pass if an ArithmeticException is thrown.
        minValue.plus(-1);
    }
}