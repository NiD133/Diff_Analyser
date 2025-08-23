package org.joda.time.field;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link FieldUtils} class.
 */
public class FieldUtilsTest {

    /**
     * Tests that getWrappedValue correctly handles wrapping a large negative number.
     * The wrapping logic should work correctly even for values near Integer.MIN_VALUE.
     */
    @Test
    public void getWrappedValue_withLargeNegativeValue_wrapsIntoRangeCorrectly() {
        // Arrange: Define the inputs for the test.
        // We are testing the wrapping of a large negative number.
        final int currentValue = -2147483647;
        final int amountToAdd = 0;
        final int minValue = -1;
        final int maxValue = 0;
        
        // The expected result after wrapping the currentValue into the range [-1, 0] is -1.
        final int expectedValue = -1;

        // Act: Call the method under test.
        int actualValue = FieldUtils.getWrappedValue(currentValue, amountToAdd, minValue, maxValue);

        // Assert: Verify the result is as expected.
        assertEquals(expectedValue, actualValue);
    }
}