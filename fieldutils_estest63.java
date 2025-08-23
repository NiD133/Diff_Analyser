package org.joda.time.field;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for the safe multiplication methods in {@link FieldUtils}.
 *
 * Note: This class replaces the auto-generated "FieldUtils_ESTestTest63".
 * A descriptive name improves project clarity.
 */
public class FieldUtilsSafeMultiplyTest {

    /**
     * Verifies that safeMultiply(long, long) throws an ArithmeticException
     * when multiplying Long.MIN_VALUE by -1.
     *
     * This is a critical edge case because the mathematical result (2^63)
     * is one greater than Long.MAX_VALUE (2^63 - 1), causing an overflow.
     */
    @Test
    public void safeMultiply_shouldThrowExceptionOnMinValueAndMinusOne() {
        try {
            FieldUtils.safeMultiply(Long.MIN_VALUE, -1L);
            fail("Expected an ArithmeticException for long overflow, but none was thrown.");
        } catch (ArithmeticException e) {
            // Success: The expected exception was thrown.
            // We can optionally verify the message for more robust testing.
            String expectedMessage = "Multiplication overflows a long: " + Long.MIN_VALUE + " * -1";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}