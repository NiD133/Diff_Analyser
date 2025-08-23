package org.joda.time.field;

import org.joda.time.DateTimeFieldType;
import org.junit.Test;

/**
 * Unit tests for {@link FieldUtils}.
 * This class focuses on the verifyValueBounds method.
 */
public class FieldUtilsTest {

    /**
     * Tests that verifyValueBounds does not throw an exception when the value
     * is equal to both the lower and upper bounds. This is a valid boundary condition.
     */
    @Test
    public void verifyValueBounds_shouldNotThrowException_whenValueIsOnBoundary() {
        // Arrange: Define the field type and a value that is on the boundary.
        DateTimeFieldType fieldType = DateTimeFieldType.minuteOfHour();
        int boundaryValue = -5550;
        int lowerBound = -5550;
        int upperBound = -5550;

        // Act & Assert: The method should execute without throwing an exception.
        // The test will pass if no IllegalFieldValueException is thrown, which is the
        // expected behavior since the value is within the inclusive bounds.
        FieldUtils.verifyValueBounds(fieldType, boundaryValue, lowerBound, upperBound);
    }
}