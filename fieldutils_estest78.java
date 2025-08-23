package org.joda.time.field;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Unit tests for the {@link FieldUtils} class.
 */
public class FieldUtilsTest {

    /**
     * Tests that safeMultiply(int, int) throws an ArithmeticException
     * when the result of the multiplication overflows the integer range.
     */
    @Test
    public void safeMultiplyByInt_shouldThrowException_whenResultOverflows() {
        // Arrange: These two integers are chosen specifically because their product
        // (-5,432,624,001,937) is far outside the valid range for an int
        // (from -2,147,483,648 to 2,147,483,647), causing an overflow.
        final int largePositiveValue = 2146641827;
        final int negativeMultiplier = -2531;

        // Act & Assert: Verify that the expected exception is thrown.
        // The assertThrows method is a modern, clear way to test for exceptions.
        ArithmeticException exception = assertThrows(
            ArithmeticException.class,
            () -> FieldUtils.safeMultiply(largePositiveValue, negativeMultiplier)
        );

        // Assert: Verify the exception message is correct to ensure the right error is reported.
        String expectedMessage = "Multiplication overflows an int: 2146641827 * -2531";
        assertEquals(expectedMessage, exception.getMessage());
    }
}