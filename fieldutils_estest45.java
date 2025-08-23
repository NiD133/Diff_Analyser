package org.joda.time.field;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the utility methods in {@link FieldUtils}.
 */
public class FieldUtilsTest {

    /**
     * Tests that getWrappedValue correctly wraps a value that exceeds the maximum
     * boundary of the specified range. The result should circle back to the start
     * of the range.
     */
    @Test
    public void getWrappedValue_shouldWrapAroundWhenSumExceedsMaximum() {
        // Arrange
        // Define a simple range of [-1, 0]. The total size of this range is 2.
        final int minValue = -1;
        final int maxValue = 0;

        // The sum of the current value and the amount to add (1 + 3 = 4) is well outside the range.
        final int currentValue = 1;
        final int amountToAdd = 3;

        // The expected result after wrapping the value 4 within the [-1, 0] range is 0.
        // The wrapping calculation is: ((value - minValue) % rangeSize) + minValue
        // ((4 - (-1)) % 2) + (-1)  =>  (5 % 2) - 1  =>  1 - 1 = 0
        final int expectedValue = 0;

        // Act
        int actualValue = FieldUtils.getWrappedValue(currentValue, amountToAdd, minValue, maxValue);

        // Assert
        assertEquals("The wrapped value should be the maximum of the range", expectedValue, actualValue);
    }
}