package org.joda.time.field;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Unit tests for the safeAdd(long, long) method in {@link FieldUtils}.
 */
public class FieldUtilsSafeAddTest {

    /**
     * Tests that safeAdd throws an ArithmeticException when adding two long values
     * results in a negative overflow (underflow).
     */
    @Test
    public void safeAdd_whenLongAdditionCausesNegativeOverflow_throwsArithmeticException() {
        // Arrange: Define the values that will cause an overflow.
        // Using Long.MIN_VALUE makes the intent clear without magic numbers.
        final long value1 = Long.MIN_VALUE;
        final long value2 = Long.MIN_VALUE;
        final String expectedMessage = "The calculation caused an overflow: " + value1 + " + " + value2;

        // Act & Assert: Verify that the correct exception is thrown with the expected message.
        // assertThrows is a modern, clear way to test for exceptions.
        ArithmeticException exception = assertThrows(
            ArithmeticException.class,
            () -> FieldUtils.safeAdd(value1, value2)
        );

        assertEquals(expectedMessage, exception.getMessage());
    }
}