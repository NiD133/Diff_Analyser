package org.joda.time.field;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link FieldUtils} class.
 */
public class FieldUtilsTest {

    /**
     * Tests that getWrappedValue correctly wraps a value that is below the minimum boundary.
     * The value should wrap around from the maximum end of the range.
     */
    @Test
    public void getWrappedValue_shouldWrapAround_whenValueIsBelowMinimum() {
        // Arrange: Define a range and a value that falls below this range.
        final int minValue = -4536;
        final int maxValue = -317;
        final int valueToWrap = -4697;

        // The value is 161 less than the minimum (-4697 - (-4536) = -161).
        // It should wrap around from the maximum end of the range. For example,
        // a value of (minValue - 1) would wrap to maxValue.
        // Following this logic, the expected result is -477.
        final int expectedWrappedValue = -477;

        // Act: Call the method under test.
        int actualWrappedValue = FieldUtils.getWrappedValue(valueToWrap, minValue, maxValue);

        // Assert: Verify the result is as expected.
        assertEquals(expectedWrappedValue, actualWrappedValue);
    }
}