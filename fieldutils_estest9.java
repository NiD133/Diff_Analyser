package org.joda.time.field;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * A test suite for the utility methods in the {@link FieldUtils} class.
 */
public class FieldUtilsTest {

    /**
     * Verifies that safeMultiply(long, int) throws an ArithmeticException
     * when the result of the multiplication would overflow the range of a long.
     * This is tested using the edge case of Long.MIN_VALUE.
     */
    @Test
    void safeMultiply_whenOverflowOccurs_throwsArithmeticException() {
        // Arrange: Define inputs that are known to cause an overflow.
        // Multiplying Long.MIN_VALUE by a negative number is a classic overflow case.
        long value = Long.MIN_VALUE;
        int multiplier = -7;

        // Act & Assert: Call the method and verify that it throws the expected exception.
        ArithmeticException thrown = assertThrows(
            ArithmeticException.class,
            () -> FieldUtils.safeMultiply(value, multiplier),
            "Expected safeMultiply to throw an exception due to overflow, but it did not."
        );

        // Assert: Further verify that the exception message is correct and informative.
        String expectedMessage = "Multiplication overflows a long: -9223372036854775808 * -7";
        assertEquals(expectedMessage, thrown.getMessage());
    }
}