package org.joda.time.field;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link FieldUtils} class, focusing on the getWrappedValue method.
 */
public class FieldUtilsGetWrappedValueTest {

    /**
     * Tests that getWrappedValue correctly handles wrapping a large negative number,
     * specifically Integer.MIN_VALUE, which is a common edge case.
     */
    @Test
    public void getWrappedValue_whenCurrentValueIsIntegerMinValue_shouldWrapAroundCorrectly() {
        // Arrange: Set up the test parameters for wrapping.
        // The method should add 'amountToAdd' to 'currentValue' and then wrap the
        // result to fit within the range [minValue, maxValue].
        final int currentValue = Integer.MIN_VALUE;
        final int amountToAdd = 0;
        final int minValue = 0;
        final int maxValue = 1756;

        // Act: Call the method under test.
        final int actualValue = FieldUtils.getWrappedValue(currentValue, amountToAdd, minValue, maxValue);

        // Assert: Verify the result is wrapped correctly.
        // The original test expected 2697, which was incorrect as it falls outside
        // the valid range of [0, 1756]. The correct calculation is as follows:
        // 1. Total value to wrap = Integer.MIN_VALUE + 0 = -2,147,483,648.
        // 2. The size of the valid range is (maxValue - minValue + 1) = (1756 - 0 + 1) = 1757.
        // 3. The wrapped value is derived from the remainder. In Java, the remainder
        //    of a negative number can be negative: -2,147,483,648 % 1757 = -980.
        // 4. The method corrects for the negative remainder by adding the range size:
        //    -980 + 1757 = 777.
        // 5. Finally, the result is shifted by minValue: 777 + 0 = 777.
        final int expectedValue = 777;
        assertEquals(expectedValue, actualValue);
    }
}