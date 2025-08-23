package org.joda.time.field;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for the utility methods in {@link FieldUtils}.
 * This test focuses on the input validation of the getWrappedValue method.
 */
public class FieldUtilsTest {

    /**
     * Tests that getWrappedValue() throws an IllegalArgumentException when the
     * minimum value is equal to the maximum value, which constitutes an invalid range.
     */
    @Test
    public void getWrappedValue_shouldThrowException_whenMinValueEqualsMaxValue() {
        // Arrange: Define an invalid range where minValue is equal to maxValue.
        // The method requires minValue to be strictly less than maxValue.
        final int currentValue = 5;
        final int wrapValue = 0;
        final int minValue = 10;
        final int maxValue = 10; // This creates the invalid range.

        // Act & Assert
        try {
            FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue);
            fail("Expected an IllegalArgumentException because minValue cannot be equal to maxValue.");
        } catch (IllegalArgumentException e) {
            // Assert that the correct exception message is thrown.
            assertEquals("The exception message should indicate an invalid range.", "MIN > MAX", e.getMessage());
        }
    }
}