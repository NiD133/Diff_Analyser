package org.joda.time.field;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the utility methods in {@link FieldUtils}.
 */
public class FieldUtilsTest {

    /**
     * Tests that safeToInt correctly converts a long that is exactly at the
     * lower boundary of the integer range (Integer.MIN_VALUE).
     * The method should return the correct integer value without throwing an exception.
     */
    @Test
    public void safeToInt_shouldCorrectlyConvertIntegerMinValue() {
        // Arrange: Define the input value as the minimum integer value, cast to a long.
        // This represents the lower boundary for a valid conversion.
        long valueToConvert = (long) Integer.MIN_VALUE;

        // Act: Call the method under test.
        int result = FieldUtils.safeToInt(valueToConvert);

        // Assert: Verify that the converted value is equal to Integer.MIN_VALUE.
        assertEquals(Integer.MIN_VALUE, result);
    }
}