package org.joda.time.field;

import org.junit.Test;

/**
 * Unit tests for the {@link FieldUtils} class.
 */
public class FieldUtilsTest {

    /**
     * Tests that safeSubtract throws an ArithmeticException when the subtraction
     * results in a value less than Long.MIN_VALUE (underflow).
     */
    @Test(expected = ArithmeticException.class)
    public void safeSubtract_shouldThrowExceptionOnUnderflow() {
        // Subtracting any positive number from Long.MIN_VALUE will cause an underflow.
        FieldUtils.safeSubtract(Long.MIN_VALUE, 11L);
    }
}