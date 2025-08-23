package org.joda.time.field;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for the {@link FieldUtils} class.
 */
public class FieldUtilsTest {

    /**
     * Tests that safeNegate() throws an ArithmeticException when attempting to negate
     * Integer.MIN_VALUE, as its positive counterpart is outside the valid integer range.
     */
    @Test
    public void safeNegate_shouldThrowExceptionForIntegerMinValue() {
        try {
            FieldUtils.safeNegate(Integer.MIN_VALUE);
            fail("Expected an ArithmeticException to be thrown but it wasn't.");
        } catch (ArithmeticException e) {
            // This is the expected behavior.
            // Now, we verify that the exception message is clear and correct.
            assertEquals("Integer.MIN_VALUE cannot be negated", e.getMessage());
        }
    }
}