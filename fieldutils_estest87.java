package org.joda.time.field;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link FieldUtils} class.
 */
public class FieldUtilsTest {

    /**
     * Tests that safeNegate correctly negates a valid, positive integer.
     * The method should behave like the standard unary minus operator for any integer
     * except Integer.MIN_VALUE.
     */
    @Test
    public void safeNegate_shouldReturnCorrectlyNegatedValue_forPositiveInteger() {
        // Arrange
        final int positiveValue = 2147483639;
        final int expectedNegatedValue = -2147483639;

        // Act
        final int actualNegatedValue = FieldUtils.safeNegate(positiveValue);

        // Assert
        assertEquals(expectedNegatedValue, actualNegatedValue);
    }
}