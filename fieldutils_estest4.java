package org.joda.time.field;

import org.junit.Test;

/**
 * Unit tests for the {@link FieldUtils} class.
 */
public class FieldUtilsTest {

    /**
     * Tests that verifyValueBounds() does not throw an exception when the provided
     * value is valid and equal to the lower bound of the specified range.
     */
    @Test
    public void verifyValueBounds_shouldNotThrowException_whenValueIsAtLowerBound() {
        // Arrange: Define a value that is exactly at the lower boundary of the valid range.
        final String fieldName = "testField";
        final int value = 1;
        final int lowerBound = 1;
        final int upperBound = 2000;

        // Act & Assert: The method should execute without throwing an exception.
        // The test will fail automatically if an IllegalFieldValueException is thrown,
        // which is the expected behavior for a valid value.
        FieldUtils.verifyValueBounds(fieldName, value, lowerBound, upperBound);
    }
}