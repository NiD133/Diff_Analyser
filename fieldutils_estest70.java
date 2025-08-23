package org.joda.time.field;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for the utility methods in {@link FieldUtils}.
 */
public class FieldUtilsTest {

    /**
     * Tests that safeMultiply(long, int) throws an ArithmeticException
     * when the result of the multiplication exceeds Long.MAX_VALUE.
     */
    @Test
    public void safeMultiplyLongByIntShouldThrowExceptionOnPositiveOverflow() {
        // Arrange: Define two numbers whose product is guaranteed to exceed the maximum long value.
        // Using Long.MAX_VALUE makes the intent of testing an overflow condition explicit.
        final long largeValue = Long.MAX_VALUE;
        final int multiplier = 2;

        // Act & Assert
        try {
            FieldUtils.safeMultiply(largeValue, multiplier);
            fail("Expected an ArithmeticException to be thrown for long overflow.");
        } catch (ArithmeticException e) {
            // Verify that the exception message is correct and informative.
            final String expectedMessage = "Multiplication overflows a long: " + largeValue + " * " + multiplier;
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}