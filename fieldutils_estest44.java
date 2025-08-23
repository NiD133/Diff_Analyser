package org.joda.time.field;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link FieldUtils} class.
 */
public class FieldUtilsTest {

    /**
     * Tests that getWrappedValue correctly handles the edge case of Integer.MIN_VALUE.
     *
     * The value Integer.MIN_VALUE is a special case in integer arithmetic because its
     * absolute value cannot be represented as a positive integer. This test verifies
     * that when wrapping this value within a range of [0, 1], it correctly maps to 0,
     * as Integer.MIN_VALUE is an even number.
     */
    @Test
    public void getWrappedValue_withIntegerMinValue_shouldWrapCorrectly() {
        // Arrange
        int valueToWrap = Integer.MIN_VALUE;
        int minValue = 0;
        int maxValue = 1;
        int expectedValue = 0; // Since MIN_VALUE is even, it should wrap to the start of the [0, 1] range.

        // Act
        int wrappedValue = FieldUtils.getWrappedValue(valueToWrap, minValue, maxValue);

        // Assert
        assertEquals(expectedValue, wrappedValue);
    }
}