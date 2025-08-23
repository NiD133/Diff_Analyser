package org.joda.time.field;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link FieldUtils} class.
 */
public class FieldUtilsTest {

    /**
     * Tests that {@code safeSubtract} correctly computes the difference
     * when subtracting a positive value from zero. This covers a basic
     * subtraction case with no risk of overflow.
     */
    @Test
    public void safeSubtract_shouldCorrectlySubtractPositiveValueFromZero() {
        // Arrange
        final long minuend = 0L;
        final long subtrahend = Integer.MAX_VALUE; // Using a large positive value
        final long expectedResult = -(long) Integer.MAX_VALUE;

        // Act
        final long actualResult = FieldUtils.safeSubtract(minuend, subtrahend);

        // Assert
        assertEquals(expectedResult, actualResult);
    }
}