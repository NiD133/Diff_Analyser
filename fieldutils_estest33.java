package org.joda.time.field;

import org.junit.Test;

/**
 * Unit tests for the {@link FieldUtils} class.
 */
public class FieldUtilsTest {

    /**
     * Verifies that safeDivide() throws an ArithmeticException when the divisor is zero,
     * as specified by the method's contract.
     */
    @Test(expected = ArithmeticException.class)
    public void safeDivideByZeroThrowsArithmeticException() {
        // The safeDivide method must throw an ArithmeticException when attempting to divide by zero.
        FieldUtils.safeDivide(10L, 0L);
    }
}