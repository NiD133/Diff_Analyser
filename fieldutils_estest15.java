package org.joda.time.field;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link FieldUtils} class.
 */
public class FieldUtilsTest {

    /**
     * Tests that subtracting Long.MIN_VALUE from itself using safeSubtract
     * correctly results in 0 and does not throw an ArithmeticException.
     * This is an important edge case for safe subtraction logic.
     */
    @Test
    public void safeSubtract_longMinValueMinusItself_returnsZero() {
        // Arrange
        final long value = Long.MIN_VALUE;
        final long expectedResult = 0L;

        // Act
        long actualResult = FieldUtils.safeSubtract(value, value);

        // Assert
        assertEquals(expectedResult, actualResult);
    }
}