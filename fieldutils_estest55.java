package org.joda.time.field;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Unit tests for the {@link FieldUtils} class, focusing on safe conversion methods.
 */
public class FieldUtilsTest {

    @Test
    public void safeToInt_shouldThrowException_whenValueIsTooLargeToFitInInt() {
        // Arrange: Define a test value that is explicitly one greater than the maximum integer value.
        // This makes the boundary condition being tested obvious.
        final long valueGreaterThanIntMax = (long) Integer.MAX_VALUE + 1;

        // Act: Call the method and capture the expected exception.
        ArithmeticException exception = assertThrows(
            ArithmeticException.class,
            () -> FieldUtils.safeToInt(valueGreaterThanIntMax)
        );

        // Assert: Verify that the exception message is informative and correct.
        assertEquals("Value cannot fit in an int: " + valueGreaterThanIntMax, exception.getMessage());
    }

    @Test
    public void safeToInt_shouldThrowException_whenValueIsTooSmallToFitInInt() {
        // Arrange: Define a test value that is one less than the minimum integer value
        // to test the lower boundary condition.
        final long valueLessThanIntMin = (long) Integer.MIN_VALUE - 1;

        // Act: Call the method and capture the expected exception.
        ArithmeticException exception = assertThrows(
            ArithmeticException.class,
            () -> FieldUtils.safeToInt(valueLessThanIntMin)
        );

        // Assert: Verify the exception message.
        assertEquals("Value cannot fit in an int: " + valueLessThanIntMin, exception.getMessage());
    }
}