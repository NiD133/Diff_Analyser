package org.joda.time.field;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for the {@link FieldUtils} class.
 */
public class FieldUtilsTest {

    /**
     * Tests that safeMultiply(int, int) throws an ArithmeticException
     * when the multiplication result overflows the integer range.
     */
    @Test
    public void safeMultiply_whenIntOverflow_shouldThrowArithmeticException() {
        // Arrange: Define two integer values whose product will exceed Integer.MAX_VALUE.
        // Multiplying the smallest possible integer by itself is a classic overflow case.
        final int value1 = Integer.MIN_VALUE;
        final int value2 = Integer.MIN_VALUE;

        // Act & Assert
        try {
            FieldUtils.safeMultiply(value1, value2);
            fail("Expected an ArithmeticException to be thrown due to integer overflow.");
        } catch (ArithmeticException e) {
            // Verify that the correct exception was thrown with the expected detailed message.
            final String expectedMessage = "Multiplication overflows an int: -2147483648 * -2147483648";
            assertEquals("The exception message did not match the expected overflow details.",
                         expectedMessage, e.getMessage());
        }
    }
}