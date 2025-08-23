package org.joda.time.field;

import org.joda.time.DateTimeFieldType;
import org.junit.Test;

/**
 * Unit tests for the utility methods in {@link FieldUtils}.
 */
public class FieldUtilsTest { // Renamed from FieldUtils_ESTestTest49 for clarity

    /**
     * Tests that verifyValueBounds() does not throw an exception when the value
     * being checked is equal to the maximum allowed value (the upper bound).
     */
    @Test
    public void verifyValueBounds_shouldNotThrowException_whenValueIsAtUpperBound() {
        // Arrange: Define the field type, bounds, and a value at the upper bound.
        final DateTimeFieldType fieldType = DateTimeFieldType.weekOfWeekyear();
        final int lowerBound = -2144353229;
        final int upperBound = 1537;
        final int value = upperBound; // The value to check is the upper bound itself.

        // Act & Assert: The method should execute without throwing an exception.
        // The test will fail automatically if an IllegalFieldValueException is thrown.
        FieldUtils.verifyValueBounds(fieldType, value, lowerBound, upperBound);
    }
}