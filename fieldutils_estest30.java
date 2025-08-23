package org.joda.time.field;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link FieldUtils} class.
 */
public class FieldUtilsTest {

    /**
     * Tests that getWrappedValue correctly wraps a value that is below the minimum
     * boundary of the specified range. The value should wrap around from the maximum end.
     */
    @Test
    public void getWrappedValue_whenValueIsBelowMinimum_shouldWrapAroundFromMaximum() {
        // Arrange: Define the test parameters with descriptive names.
        // The value to be wrapped is less than the minimum value of the range.
        final int valueToWrap = 1055;
        final int minValue = 1671;
        final int maxValue = 352831696;

        // The value is (1671 - 1055) = 616 units below the minimum.
        // Therefore, it should wrap around to 616 units "before" the minimum,
        // which corresponds to a value near the maximum end of the range.
        // Calculation: maxValue - (distance_below_min - 1)
        // => 352831696 - (616 - 1) = 352831081
        final int expectedWrappedValue = 352831081;

        // Act: Call the method under test.
        int actualWrappedValue = FieldUtils.getWrappedValue(valueToWrap, minValue, maxValue);

        // Assert: Verify that the actual result matches the expected wrapped value.
        assertEquals("Value should wrap around from the maximum when it's below the minimum",
                     expectedWrappedValue, actualWrappedValue);
    }
}